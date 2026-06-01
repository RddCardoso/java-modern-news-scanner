package com.newsscanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

//@Component // Isto dizso Spring: "Gere tu esta classe"
public class NewsRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(NewsRunner.class);

    private final NewsService newsService;
    private final ArtigoRepository artigoRepository;

    // O Spring Boot detecta este construtor automaticamente e injecta as duas dependências
    public NewsRunner(NewsService newsService, ArtigoRepository artigoRepository) {
        this.newsService = newsService;
        this.artigoRepository = artigoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("=== A verificar controlo de execução do Scraper ===");

        long totalArtigos = artigoRepository.count();

        //if(totalArtigos == 0) {
           log.info("--- Iniciando Scraper Profissional ---");
            newsService.processarNoticias(10, false);
//        } else {
//            System.out.println("Controlo do Runner: Já existem  " + " artigos na base de dados.");
//            System.out.println("[SKIP] Scraper ignorado para evitar chamadas desnecessárias no desenvolvimento.]");
//        }
    }
}
