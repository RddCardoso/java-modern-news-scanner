package com.newsscanner.repository;

import com.newsscanner.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    boolean existsByLink(String link);

    // Finds the latest article based on a publication date
    Optional<Article> findTopByOrderByPublicationDateDesc();

    Page<Article> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
