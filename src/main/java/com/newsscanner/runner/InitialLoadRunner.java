package com.newsscanner.runner;

import com.newsscanner.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitialLoadRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(InitialLoadRunner.class);
    private final NewsService newsService;

    public InitialLoadRunner(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("\uD83D\uDE80 [INITIAL LOAD] Starting initial historical loading of 10 pages...");

        try {
            newsService.processNews(10, true, null);
            log.info("✅ [INITIAL LOAD] Successfully completed! Database is up to date.");
        } catch (Exception e) {
            log.error("❌ [INITIAL LOAD] Error executing startup load: ", e);
        }
    }
}
