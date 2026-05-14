package com.newsscanner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtigoRepository extends JpaRepository<Artigo, Long> {
    // Aqui já tens acesso a .save(), .findAll(), .delete(), etc.
    boolean existsByLink(String link);
}
