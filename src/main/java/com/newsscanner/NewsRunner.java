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

        int guardadas = 0;
        int ignoradas = 0;

        for(int i = 1; i <= 10; i++) {
            String url = "https://pplware.sapo.pt/page/" + i + "/";
            System.out.println("A ler página " + i + "...");

            List<Artigo> noticias = scraper.extrairNoticias(url, ".post-title a");

            // A MAGIA ACONTECE AQUI: Guardar na base de dados!
            for(Artigo artigo : noticias) {
                if(!repository.existsByLink(artigo.getLink())) {
                    repository.save(artigo);
                    guardadas++;
                    System.out.println("✅ Guardado: " + artigo.getTitulo());
                } else {
                    ignoradas++;
                    System.out.println("⚠\uFE0F Já existe: " + artigo.getTitulo());
                }
            }

            Thread.sleep(1000);
        }

        System.out.println("\n--- RESUMO DA OPERAÇÃO ---");
        System.out.println("Novas guardadas: " + guardadas);
        System.out.println("Ignoradas (duplicadas): " + ignoradas);
        System.out.println("Total processadas: " + (guardadas + ignoradas));
        System.out.println("✅ Processo concluído! Notícias guardadas no Docker.");
    }
}
