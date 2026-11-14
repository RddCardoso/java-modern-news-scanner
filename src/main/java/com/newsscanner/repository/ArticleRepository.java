package com.newsscanner.repository;

import com.newsscanner.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository interface for {@link Article} persistence and database queries.
 * Leverages Spring Data JPA derived queries for duplicate checks, date boundaries, and paginated search.
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    /**
     * Checks whether an article with the specified URL link already exists in the database.
     *
     * @param link The unique URL of the article
     * @return {@code true} if the article already exists, {@code false} otherwise
     */
    boolean existsByLink(String link);

    /**
     * Retrieves the most recently published article stored in the database.
     *
     * @return An {@link Optional} containing the latest article, or empty if the table is empty
     */
    Optional<Article> findTopByOrderByPublicationDateDesc();

    /**
     * Searches for articles whose title contains the given keyword, ignoring case, with pagination support.
     *
     * @param title     The keyword to filter titles
     * @param pageable  Pagination and sorting parameters
     * @return A {@link Page} of matching articles
     */
    Page<Article> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
