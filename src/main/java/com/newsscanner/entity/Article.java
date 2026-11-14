package com.newsscanner.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Represents a news article entity persisted in the database.
 * Maps directly to the 'articles' table, storing essential metadata fetched from the RSS feed.
 */
@Entity
@Table(name= "articles")
public  class Article{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT", unique = true, nullable = false)
    private String link;

    @Column(name = "publicationDate")
    private LocalDateTime publicationDate;

    @CreationTimestamp
    @Column(name = "collected_at", nullable = false, updatable = false)
    private LocalDateTime collectedAt;

    /**
     * Default protected constructor required by JPA specification.
     */
    public Article() {}

    /**
     * Constructs a new Article with essential publication metadata.
     * @param title             The title of the news articles
     * @param link              The unique URL link of the article
     * @param publicationDate   The original publication date and time from the RSS feed
     */
    public Article(String title, String link,  LocalDateTime publicationDate) {
        this.title = title;
        this.link = link;
        this.publicationDate = publicationDate;
    }

    // Getters and Setters...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public LocalDateTime getCollectedAt() {
        return collectedAt;
    }

    public void setCollectedAt(LocalDateTime collectedAt) {
        this.collectedAt = collectedAt;
    }
}