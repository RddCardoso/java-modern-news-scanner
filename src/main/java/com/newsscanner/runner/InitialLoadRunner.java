package com.newsscanner.runner;

import com.newsscanner.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 *  Bootstraps initial news loading when the application starts up.
 *  Ensure the database is populated with the latest RSS feed entries on startup.
 */
@Component
public class InitialLoadRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(InitialLoadRunner.class);
    private final NewsService newsService;

    /**
     * Constructor-based dependency injection.
     *
     * @param newsService The core news processing service
     */
    public InitialLoadRunner(NewsService newsService) {
        this.newsService = newsService;
    }

    /**
     * Executes the initial ingestion workflow upon application startup.
     *
     * @param args Command line arguments passed during startup
     * @throws Exception if any error occurs during execution
     */
    @Override
    public void run(String... args) throws Exception {
        log.info("\uD83D\uDE80 [INITIAL LOAD] Starting initial news synchronization from RSS feed...");

        try {
            // Trigger incremental synchronization using decoupled RSS feed service
            // Passing false for forceHistoricalLoad since RSS fetch provides the active recent batch
            newsService.processNews(false, null);
            log.info("[INITIAL LOAD] Successfully completed! Database is up to date.");
        } catch (Exception e) {
            log.error("[INITIAL LOAD] Error executing startup load: {}", e.getMessage());
        }
    }
}
