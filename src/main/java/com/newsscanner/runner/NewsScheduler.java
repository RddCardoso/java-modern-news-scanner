package com.newsscanner.runner;

import com.newsscanner.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled component that periodically triggers automatic news synchronization
 * from the RSS feed to keep the database constantly up to date.
 */
@Component
public class NewsScheduler {
    private static final Logger log = LoggerFactory.getLogger(NewsScheduler.class);

    private final NewsService newsService;

    /**
     * Constructor-based dependency injection for clean, immutable architecture.
     *
     * @param newsService The core news processing service
     */
    public NewsScheduler(NewsService newsService) {
        this.newsService = newsService;
    }

    /**
     * Automatically executes news synchronization at fixed intervals.
     * Initial delay of 5 minutes (300000 ms), followed by a fixed delay of 1 hour (3600000 ms)
     */
    @Scheduled(initialDelay = 300000, fixedDelay = 3600000)
    public void runSchedulerScraper() {
        log.info("[SCHEDULER] The clock ticked! Starting automatic synchronization...");
        try {
            // Trigger incremental synchronization using the decoupled RSS feed service
            newsService.processNews(false, null);

            log.info("[SCHEDULER] Synchronization successfully completed. Next execution in 1 hour...");
        } catch (Exception e) {
            log.error("[SCHEDULER] Error executing scheduled scraper: {}", e.getMessage(), e);
        }
    }
}
