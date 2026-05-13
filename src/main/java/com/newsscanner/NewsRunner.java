package com.newsscanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component // Isto dizso Spring: "Gere tu esta classe"
public class NewsRunner implements CommandLineRunner {

    private final ScraperService scraper;
    private final ArtigoRepository repository;

    // O Spring injeta o Scraper e o Repository aqui automaticamente
    public NewsRunner(ScraperService scraper, ArtigoRepository repository) {
        this.scraper = scraper;
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- A iniciar extração de notícias ---");

        for(int i = 1; i <= 3; i++) {
            String url = "https://pplware.sapo.pt/page/" + i + "/";
            System.out.println("A ler página " + i + "...");

            List<Artigo> noticias = scraper.extrairNoticias(url, ".post-title a");

            // A MAGIA ACONTECE AQUI: Guardar na base de dados!
            repository.saveAll(noticias);

            Thread.sleep(1000);
        }

        System.out.println("✅ Processo concluído! Notícias guardadas no Docker.");
    }
}
