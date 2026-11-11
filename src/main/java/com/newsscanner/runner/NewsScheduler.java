package com.newsscanner.runner;

import com.newsscanner.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NewsScheduler {
    private static final Logger log = LoggerFactory.getLogger(NewsScheduler.class);
    private final NewsService newsService;

    public NewsScheduler(NewsService newsService) {
        this.newsService = newsService;
    }

    // initialDelay = 300000 ms(5 minutes)
    // fixedDelay = 3600000 ms Runs every hour
    @Scheduled(initialDelay = 300000, fixedDelay = 3600000)
    public void runSchedulerScraper() {
        log.info("⏰ SCHEDULER: The clock ticked! Starting automatic synchronization...");
        try {
            newsService.processNews(1, false, null);

            log.info("⏰ SCHEDULER: Synchronization successfully completed. Next execution in 1 hour...");
        } catch (Exception e) {
            log.error("❌ SCHEDULER: Error executing scheduled scraper:", e);
        }
    }
}
