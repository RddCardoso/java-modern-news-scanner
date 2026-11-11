package com.newsscanner.service;

import com.newsscanner.entity.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class ScraperService {

    public static final Logger log = LoggerFactory.getLogger(ScraperService.class);

    public List<Article> extractNews(String url) throws IOException{
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT10.0; Win64; x64) Chrome/120.0.0.0 Safari/537.36" )
                .timeout(10000)
                .get();

        // 1. Target the main container element for each article
        return doc.select("article").stream()
                .map(articleElement -> {
                    // 2. Extract link and title from the heading tag
                    Element linkElement = articleElement.selectFirst(".post-title a");
                    Element dataElement = articleElement.selectFirst("time");

                    String title = linkElement != null ? linkElement.text() : "";
                    String link = linkElement != null ? linkElement.attr("href") : "";

                    // 3. Parse published date from DOM
                    LocalDateTime publicationDate = LocalDateTime.now();

                    if(dataElement != null){
                        String datetimeAttr = dataElement.attr("datetime");
                        try {
                            // OffsetDateTime handles timezone offset (+01:00) and converts to LocalDateTime
                            publicationDate = OffsetDateTime.parse(datetimeAttr).toLocalDateTime();
                        } catch (Exception e) {
                            log.warn("Failed to parse date '{}'. Failling back to current timestamp.", datetimeAttr);
                        }
                    }

                    // 4. Instantiate JPA Entity
                    return new Article(title, link, publicationDate);
                })
                .distinct()
                .toList();
    }
}