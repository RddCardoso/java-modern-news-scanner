package com.newsscanner.controller;

import com.newsscanner.entity.Article;
import com.newsscanner.repository.ArticleRepository;
import com.newsscanner.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/articles")
@Tag(name = "News Scanner Controller", description = "Endpoints for triggering and managing the news scraper")
public class ArticleController {

    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    private final ArticleRepository articleRepository;
    private final NewsService newsService;

    public ArticleController(ArticleRepository articleRepository, NewsService newsService) {
        this.articleRepository = articleRepository;
        this.newsService = newsService;
    }

    @GetMapping
    public Page<Article> getAllArticles(
            @RequestParam(required = false)  String title,
            @PageableDefault(size = 10) Pageable pageable) {

        if(title != null && !title.trim().isEmpty()) {
            log.info("\uD83C\uDF10 API:Searching articles with title containing: '{}' (Page: {})", title, pageable.getPageNumber());
            return articleRepository.findByTitleContainingIgnoreCase(title, pageable);
        }

        log.info("\uD83C\uDF10 API: No filters applied. Listing all articles (Page: {})",  pageable.getPageNumber());
        return articleRepository.findAll(pageable);
    }

    /// MANUAL TRIGGER API
    /// Access:http://localhost:8080/api/articles/sync?pages=10&force=true
    @PostMapping("/sync")
    @Operation(
            summary = "Trigger news scraping manually",
            description = "Triggers the scraper for a specific number of pages. Can bypass the dynamic stop using historical mode."
    )
    public ResponseEntity<String> manualSync(
            @RequestParam(defaultValue = "1") int pages,
            @RequestParam(defaultValue = "false") boolean force) {

        log.info("\uD83D\uDD79\uFE0F API: Manual synchronization forced for {} pages!", pages);

        newsService.processNews(pages, force, null);

        return ResponseEntity.ok("Synchronization of " + pages + " page(s) successfully completed!");
    }

    @PostMapping("/historical")
    @Operation(
            summary = "Fetch historical news since a specific date",
            description = "Triggers the scraper to go back in time and fetch all articles published since the provided date string (YYYY-MM-DD)."
    )
    public ResponseEntity<String> loadHistorical(
            @RequestParam @Parameter(description = "Target date in format YYYY-MM-DD", example = "2026-06-01")
            String sinceDate) {

        log.info("ℹ\uFE0F API: Starting historical news backfill since: {}", sinceDate);

        // 1. Convert received string to LocalDate
        LocalDate targetDate = LocalDate.parse(sinceDate);

        // 2. Call service
        int capturedTotal = newsService.loadNewsSince(targetDate);

        log.info("✅ API: Historical backfill finished. Saved {} new articles.",  capturedTotal);

        return ResponseEntity.ok("Historical synchronization completed. Added " + capturedTotal + " new articles.");
    }

}
