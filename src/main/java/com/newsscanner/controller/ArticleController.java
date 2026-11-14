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
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/articles")
@Tag(name = "News Scanner Controller", description = "Endpoints for triggering and managing the news scraper")
public class ArticleController {

    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    private final ArticleRepository articleRepository;
    private final NewsService newsService;

    /**
     * Constructor-based dependency injection for clean, test-friendly architecture.
     *
     * @param articleRepository The repository for article data access
     * @param newsService       The core news processing service
     */
    public ArticleController(ArticleRepository articleRepository, NewsService newsService) {
        this.articleRepository = articleRepository;
        this.newsService = newsService;
    }

    /**
     * Retrieves paginated articles, with optional filtering by title.
     *
     * @param title     Optional title filter keyword
     * @param pageable  Pagination and sorting configuration
     * @return A page of articles matching the criteria
     */
    @GetMapping
    @Operation(summary = "Get all articles", description = "Retrieves a paginated list of articles, supporting optional title filtering.")
    public Page<Article> getAllArticles(
            @RequestParam(required = false)  String title,
            @PageableDefault(size = 10) Pageable pageable) {

        if(title != null && !title.trim().isEmpty()) {
            log.info("API:Searching articles with title containing: '{}' (Page: {})", title, pageable.getPageNumber());
            return articleRepository.findByTitleContainingIgnoreCase(title, pageable);
        }

        log.info("API: Listing all articles (Page: {})",  pageable.getPageNumber());
        return articleRepository.findAll(pageable);
    }

    /**
     * Manually triggers an immediate RSS feed synchronization pass.
     *
     * @return Response entity confirming the synchronization execution.
     */
    @PostMapping("/sync")
    @Operation(
            summary = "Trigger manual news synchronization",
            description = "Triggers an immediate fetch and synchronization from the RSS feed endpoint."
    )
    public ResponseEntity<String> manualSync() {
        log.info("API: Manual RSS synchronization triggered via API endpoint");

        // Trigger incremental synchronization using the decoupled RSS feed service
        newsService.processNews(false, null);

        return ResponseEntity.ok("RSS feed synchronization successfully completed!");
    }

    /**
     * Triggers a historical backfill load of news articles up to a specified target date.
     *
     * @param sinceDate Target date string in format YYYY-MM-DD
     * @return Response entity with the count of newly saved articles
     */
    @PostMapping("/historical")
    @Operation(
            summary = "Fetch historical news since a specific date",
            description = "Triggers the scraper to go back in time and fetch all articles published since the provided date string (YYYY-MM-DD)."
    )
    public ResponseEntity<String> loadHistorical(
            @RequestParam @Parameter(description = "Target date in format YYYY-MM-DD", example = "2026-06-01")
            String sinceDate) {

        log.info("API: Starting historical news backfill since: {}", sinceDate);

        // Convert received string date to LocalDateTime (start of the day)
        LocalDate targetLocalDate = LocalDate.parse(sinceDate);
        LocalDateTime targetDateTime = targetLocalDate.atStartOfDay();

        // Call service with LocalDateTime argument
        int capturedTotal = newsService.loadNewsSince(targetDateTime);

        log.info("API: Historical backfill finished. Saved {} new articles.",  capturedTotal);

        return ResponseEntity.ok("Historical synchronization completed. Added " + capturedTotal + " new articles.");
    }

}
