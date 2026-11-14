package com.newsscanner.service;

import com.newsscanner.entity.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ScraperService {

    public static final Logger log = LoggerFactory.getLogger(ScraperService.class);

    /**
     *  Extracts news articles robustly using the official RSS/XML feed.
     *  This decouples the ingestion pipeline from frontend UI/CSS changes,
     *  ensuring high resilience for enterprise-grade applications.
     *
     * @param url The RSS feed URL (e.g., https://pplware.sapo.pt/feed/)
     * @return List of parsed Article entities
     * @throws IOException if connection or parsing fails
     */
    public List<Article> extractNews(String url) throws IOException{
        log.info("Fetching and parsing news feed from RSS endpoint: {}", url);

        // Connect and parse the document using XML parser for structured RSS tags
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36" )
                .parser(Parser.xmlParser())
                .timeout(10000)
                .get();

        // Each article in an RSS feed is encapsulated within an <item> tag
        return doc.select("item").stream()
                .map(item -> {
                    String title = extractText(item, "title");
                    String link = extractText(item, "link");
                    String pubDateStr = extractText(item, "pubDate");

                    LocalDateTime publicationDate = parseRssDate(pubDateStr);

                    return new Article(title, link, publicationDate);
                })
                .filter(article -> !article.getLink().isEmpty() && !article.getTitle().isEmpty())
                .distinct()
                .toList();
    }

    /**
     * Helper method to safely extract text from an XML tag.
     */
    private String extractText(Element parent, String tagName) {
        Element element = parent.selectFirst(tagName);
        return element != null ? element.text().trim() : "";
    }

    /**
     * Parses standard RFC-822 / RSS publication dates into LocalDateTime.
     * Falls back to current timestamp if parsing fails to prevent pipeline interruption.
     */
    private LocalDateTime parseRssDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return LocalDateTime.now();
        }
        try {
            // Standard RSS format: e.g., "Thu, 12 Nov 2026 19:45:00 +0000"
            DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
            return LocalDateTime.parse(dateStr, formatter);
        } catch (Exception e) {
            log.warn("Failed to parse RSS publication date '{}'. Falling back to current timestamp.", dateStr);
            return LocalDateTime.now();
        }
    }
}