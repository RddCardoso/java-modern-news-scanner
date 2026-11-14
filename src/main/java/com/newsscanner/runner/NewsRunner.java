package com.newsscanner.runner;

import com.newsscanner.repository.ArticleRepository;
import com.newsscanner.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

/**
 *  Runner component that executes conditional news scraping checks upon application startup.
 *  Prevents redundant scraping during development if the database is already populated.
 */
public class NewsRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(NewsRunner.class);

    private final NewsService newsService;
    private final ArticleRepository articleRepository;

    /**
     * Constructor-based dependency injection for clean, immutable architecture.
     *
     * @param newsService       The core news processing service
     * @param articleRepository The repository for article persistence checks
     */
    public NewsRunner(NewsService newsService, ArticleRepository articleRepository) {
        this.newsService = newsService;
        this.articleRepository = articleRepository;
    }

    /**
     * Executes the conditional execution check upon startup.
     * Checks if the database is empty before triggering an RSS synchronization pass.
     *
     * @param args Command line arguments passed during startup
     * @throws Exception if any error occurs during execution
     */
    @Override
    public void run(String... args) throws Exception {
        log.info("=== Checking Scraper Execution Control ===");

        long totalArticles = articleRepository.count();

        if(totalArticles == 0) {
           log.info("--- Starting Professional Scraper (Database is empty) ---");
           // Trigger incremental synchronization using decoupled RSS feed service
            newsService.processNews( false, null);
        } else {
            log.info("[SKIP] Database already contains {} articles. Scraper skipped to avoid unnecessary calls during development.", totalArticles);
        }
    }
}
