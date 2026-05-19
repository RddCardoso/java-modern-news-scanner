package com.newsscanner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtigoRepository extends JpaRepository<Artigo, Long> {
    // Aqui já tens acesso a .save(), .findAll(), .delete(), etc.

    // Verifica se já existe o link
    boolean existsByLink(String link);

    // Procura o último artigo inserido com base no ID (que é autoincrement e sequencial)
    Optional<Artigo> findFirstByOrderByIdDesc();
}
