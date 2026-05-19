package com.newsscanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NewsService {
    @Autowired private ScraperService scraper;
    @Autowired private ArtigoRepository repository;

    public void processarNoticias(int numPaginas) {
        Set<String> linksProcessadosNestaSessao = new HashSet<>();

        for(int i = 0; i <= numPaginas; i++) {
            try {
                String url = "https://pplware.sapo.pt/page/" + i + "/";
                // 1. Extração
                List<Artigo> capturadas = scraper.extrairNoticias(url);

                // 2. Filtro de duplicados locais e persistentes
                List<Artigo> paraGuardar = capturadas.stream()
                        .filter(a -> {
                            String link = a.getLink();
                            // 1. Verifica se já processámos este link no loop
                            if(linksProcessadosNestaSessao.contains(link)) return false;

                            // 2. Verifica se já existe na base de dados
                            boolean existeNaBD = repository.existsByLink(link);

                            if(!existeNaBD) {
                                linksProcessadosNestaSessao.add(link);
                                return true;
                            }
                            return false;
                        })
                        .collect(Collectors.toList());

                // 3. Gravação em lote (mais eficiente)
                if(!paraGuardar.isEmpty()) {
                    repository.saveAll(paraGuardar);
                    System.out.println("✅ Página " + i + ": " + paraGuardar.size() + " novas notícias.");
                }
            } catch (Exception e) {
                System.out.println("❌ Erro na página " + i + ": " + e.getMessage());
            }
        }
    }
}
