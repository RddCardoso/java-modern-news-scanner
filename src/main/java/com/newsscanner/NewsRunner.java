package com.newsscanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component // Isto dizso Spring: "Gere tu esta classe"
public class NewsRunner implements CommandLineRunner {

    private final NewsService newsService;
    private final ArtigoRepository artigoRepository;

    // O Spring Boot detecta este construtor automaticamente e injecta as duas dependências
    public NewsRunner(NewsService newsService, ArtigoRepository artigoRepository) {
        this.newsService = newsService;
        this.artigoRepository = artigoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== A verificar controlo de execução do Scraper ===");

        long totalArtigos = artigoRepository.count();

        //if(totalArtigos == 0) {
            System.out.println("--- Iniciando Scraper Profissional ---");
            newsService.processarNoticias(10);
//        } else {
//            System.out.println("Controlo do Runner: Já existem  " + " artigos na base de dados.");
//            System.out.println("[SKIP] Scraper ignorado para evitar chamadas desnecessárias no desenvolvimento.]");
//        }
    }
}
