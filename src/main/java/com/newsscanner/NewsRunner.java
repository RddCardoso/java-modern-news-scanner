package com.newsscanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component // Isto dizso Spring: "Gere tu esta classe"
public class NewsRunner implements CommandLineRunner {
    @Autowired private NewsService newsService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- Iniciando Scraper Profissional ---");
        newsService.processarNoticias(10);
        System.out.println("--- Processo Concluído ---");
    }
}
