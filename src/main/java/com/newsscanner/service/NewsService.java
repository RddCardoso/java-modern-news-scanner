package com.newsscanner.service;

import com.newsscanner.entity.Article;
import com.newsscanner.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NewsService {

    private static final Logger log = LoggerFactory.getLogger(NewsService.class);

    private final ScraperService scraper;
    private final ArticleRepository repository;

    // Constructor-based dependency injection (clean, immutable, and test-friendly)
    public  NewsService(ScraperService scraper, ArticleRepository repository) {
        this.scraper = scraper;
        this.repository = repository;
    }

    public synchronized void processNews(int totalPages, boolean forceHistoricalLoad, LocalDateTime externalDateLimit) {
        Set<String> processedLinksThisSession = new HashSet<>();

        LocalDateTime  dateLimit = (externalDateLimit != null) ? externalDateLimit : repository.findTopByOrderByPublicationDateDesc()
                .map(Article::getPublicationDate)
                .orElse(LocalDateTime.now().minusDays(3));

        boolean reachedDateLimit = false;

        for(int page = 1; page <= totalPages; page++) {
            if(reachedDateLimit && !forceHistoricalLoad) {
                break;
            }

            try {
                String targetUrl = "https://pplware.sapo.pt/page/" + page + "/";
                List<Article> fetchedArticles = scraper.extractNews(targetUrl);

                List<Article> articlesToSave = new ArrayList<>();

                for(Article article : fetchedArticles) {
                    if(article.getPublicationDate().isBefore(dateLimit)) {
                        if(forceHistoricalLoad || externalDateLimit != null) {
                            log.debug("Article is older than date limit, but continuing due to historicla/backfill mode.");
                        } else {
                            log.warn("🛑 Reached older/processed articles. Activating dynamic stop condition...");
                            reachedDateLimit = true;
                            break;
                        }
                    }

                    String link = article.getLink();

                    if(processedLinksThisSession.contains(link)) continue;

                    boolean existsInBD = repository.existsByLink(link);

                    if(!existsInBD) {
                        processedLinksThisSession.add(link);
                        articlesToSave.add(article);
                    } else {
                        if (forceHistoricalLoad) {
                            // If it already exists in DB, and we are in historical load mode, just skip to the next without stopping the scraper
                            continue;
                        } else {
                            // Only activates the Scheduler safety stop if it's NOT an API backfill
                            if (externalDateLimit == null) {
                                log.warn("\uD83D\uDD34 Article already exists in DB. Dynamic stop triggered to prevent duplicates.");
                                reachedDateLimit = true;
                                break;
                            } else {
                                // If it's a historical load via API, skip the duplicate and keep scraping!
                                continue;
                            }
                        }
                    }
                }

                if(!articlesToSave.isEmpty()) {
                    repository.saveAll(articlesToSave);
                    log.info("✅ Page {}: {} new articles added.", page, articlesToSave.size());
                }
            } catch (Exception e) {
                log.error("❌ Error processing page {}: {}", page, e.getMessage(), e);
            }
        }
        log.info("\uD83D\uDD04 Incremental synchronization finished.");
    }

    public int loadNewsSince(LocalDate targetDate) {
        LocalDateTime limitWithTime = targetDate.atStartOfDay();
        long countBeforeLoad = repository.count();

        log.info("🚀 Starting scheduled backfill to fetch up to: {}", limitWithTime);

        processNews(100, false, limitWithTime);

        long countAfterLoad = repository.count();
        return (int) (countAfterLoad - countBeforeLoad);
    }
}
