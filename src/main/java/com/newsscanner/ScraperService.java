package com.newsscanner;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class ScraperService {
    // Removemos o 'static' para que isto seja um serviço que podemos instanciar
    public List<Artigo> extrairNoticias(String url) throws IOException{
        var doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT10.0; Win64; x64) Chrome/120.0.0.0 Safari/537.36" )
                .timeout(10000)
                .get();

        // 1. O selector passa a ser a tag "article" (o contendor principal)
        return doc.select("article").stream()
                .map(elementoArtigo -> {
                    // 2. Procura o link e título dentro da tag <h2> que está no article
                    var linkElement = elementoArtigo.selectFirst(".post-title a");

                    // 3. Procura a tag <time> no DOM
                    var dataElement = elementoArtigo.selectFirst("time");

                    String titulo = linkElement != null ? linkElement.text() : "";
                    String link = linkElement != null ? linkElement.attr("href") : "";

                    // 4. Converte o atributo 'datetime' para LocalDateTime
                    LocalDateTime dataObtida = LocalDateTime.now();
                    if(dataElement != null){
                        String datetimeAttr = dataElement.attr("datetime");
                        try {
                            // O OffsetDateTime sabe ler o fuso horário (+01:00) e converte de forma limpa
                            dataObtida = OffsetDateTime.parse(datetimeAttr).toLocalDateTime();
                        } catch (Exception e) {
                            // Logger profissional entraria aqui, por agora deixamos o fallback silencioso
                        }
                    }

                    // 5. Instancia a Entidade JPA com os 3 parâmetros agora obrigatórios
                    return new Artigo(titulo, link, dataObtida);
                })
                //.filter(artigo -> artigo.getTitulo().toLowerCase().contains("java") ||
                 //                       artigo.getTitulo().toLowerCase().contains("ai"))
                .distinct()
                .toList();
    }
}