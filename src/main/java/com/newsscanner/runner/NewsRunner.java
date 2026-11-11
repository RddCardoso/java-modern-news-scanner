package com.newsscanner.runner;

import com.newsscanner.repository.ArticleRepository;
import com.newsscanner.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

public class NewsRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(NewsRunner.class);

    private final NewsService newsService;
    private final ArticleRepository articleRepository;

    public NewsRunner(NewsService newsService, ArticleRepository articleRepository) {
        this.newsService = newsService;
        this.articleRepository = articleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("=== Checking Scraper Execution Control ===");

        long totalArticles = articleRepository.count();

        if(totalArticles == 0) {
           log.info("--- Starting Professional Scraper (Database is empty) ---");
            newsService.processNews(10, false, null);
        } else {
            log.info("\uFE0F [SKIP] Database already contains {} articles. Scraper skipped to avoid unnecessary calls during development.", totalArticles);
        }
    }
}
