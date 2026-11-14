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

    /**
     *  Constructor-based dependency injection for clean, immutable, and test-friendly architecture.
     *
     * @param scraper    The service responsible for fetching and parsing the RSS/news feed
     * @param repository The JPA repository for article persistence
     */
    public  NewsService(ScraperService scraper, ArticleRepository repository) {
        this.scraper = scraper;
        this.repository = repository;
    }

    /**
     * Processes incoming news articles from the RSS feed endpoint.
     * Ensure high resilience, filters out duplicates within the session and database,
     * and respects publication date boundaries to prevent redundant processing.
     *
     * @param forceHistoricalLoad If true, forces processing past the date limit for backfill operations
     * @param externalDateLimit Optional custom date limit; if null, defaults to the latest DB article or 3 days ago
     */
    public synchronized void processNews(boolean forceHistoricalLoad, LocalDateTime externalDateLimit) {
        Set<String> processedLinksThisSession = new HashSet<>();

        // Determine the cutoff date limit for incremental synchronization
        LocalDateTime  dateLimit = (externalDateLimit != null) ? externalDateLimit :
                repository.findTopByOrderByPublicationDateDesc()
                .map(Article::getPublicationDate)
                .orElse(LocalDateTime.now().minusDays(3));

        try{
            // Official RSS feed endpoint provides a clean, decoupled, and structured XML payload
            String rssFeedUrl = "https://pplware.sapo.pt/feed";
            List<Article> fetchedArticles = scraper.extractNews(rssFeedUrl);

            List<Article> articlesToSave = new ArrayList<>();

            for (Article article : fetchedArticles) {
                //Check if the article is older than our threshold date limit
                if(article.getPublicationDate().isBefore(dateLimit)) {
                    if(forceHistoricalLoad || externalDateLimit != null){
                        log.debug("Article is older than date limit, but continuing due to historical/backfill mode.");
                    } else {
                        log.warn("[STOP] Reached older/processed articles. Dynamic stop triggered.");
                        break;
                    }
                }

                String link = article.getLink();

                // Skip duplicates processed within the same execution session
                if(processedLinksThisSession.contains(link)) {
                    continue;
                }

                boolean existsInDb = repository.existsByLink(link);

                if(!existsInDb) {
                    processedLinksThisSession.add(link);
                    articlesToSave.add(article);
                } else {
                    if(forceHistoricalLoad) {
                        continue; // Skip existing articles during backfill mode
                    } else{
                        if(externalDateLimit == null) {
                            log.warn("[DUPLICATE] Article already exists in DB. Skipping duplicate.");
                        } else {
                            continue;
                        }
                    }
                }
            }

            // Persist new unique articles in batch if any were found
            if(!articlesToSave.isEmpty()) {
                repository.saveAll(articlesToSave);
                log.info("Successfully persisted {} new articles from RSS feed.", articlesToSave.size());
            } else {
                log.info("No new articles to persist. Feed is up to date.");
            }

        } catch (Exception e) {
            log.error("Error processing news feed: {}", e.getMessage(), e);
        }

        log.info("News synchronization session finished.");
    }

    /**
     *  Triggers a historical backfill load of news articles up to a specified target date.
     *
     * @param targetDate The oldest target date to fetch news up to
     * @return The total number of newly saved articles
     */
    public int loadNewsSince(LocalDateTime targetDate) {
        long countBeforeLoad = repository.count();

        log.info("Starting scheduled backfill to fetch up to: {}", targetDate);

        // Process news with forced historical loading mode enabled
        processNews(true, targetDate);

        long countAfterLoad = repository.count();
        return (int) (countAfterLoad - countBeforeLoad);
    }
}
