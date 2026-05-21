package com.newsscanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        // 1. Definir data limite dinâmica (ex: ir buscar à BD ou fixar 3 dias para testes)
        LocalDateTime  dataLimiteBD = repository.findTopByOrderByDataPublicacaoDesc()
                .map(Artigo::getDataPublicacao)
                .orElse(LocalDateTime.now().minusDays(3));

        boolean alcancouLimiteDeData = false;

        for(int i = 1; i <= numPaginas; i++) {

            if(alcancouLimiteDeData) {
                break;
            }

            try {
                String url = "https://pplware.sapo.pt/page/" + i + "/";
                // 1. Extração
                List<Artigo> capturadas = scraper.extrairNoticias(url);

                List<Artigo> paraGuardar = new ArrayList<>();

                // 2. Iteracao tradicional para permitir o uso de 'break' dinâmico
                for(Artigo a : capturadas) {

                    // CRITÉRIO DE DATA: Se a notícia for anterior à data limite, activa o stop
                    if(a.getDataPublicacao().isBefore(dataLimiteBD)) {
                        System.out.println("\uD83D\uDED1 Chegámos a notícias antigas/processadas. A activar paragem dinâmica...");
                        alcancouLimiteDeData = true;
                        break;
                    }

                    String link = a.getLink();

                    // Filtro de duplicados
                    if(linksProcessadosNestaSessao.contains(link)) continue;

                    boolean existeNaBD = repository.existsByLink(link);

                    if(!existeNaBD) {
                        linksProcessadosNestaSessao.add(link);
                        paraGuardar.add(a);
                    }

                }



//                // 2. Filtro de duplicados locais e persistentes
//                List<Artigo> paraGuardar = capturadas.stream()
//                        .filter(a -> {
//                            String link = a.getLink();
//                            // 1. Verifica se já processámos este link no loop
//                            if(linksProcessadosNestaSessao.contains(link)) return false;
//
//                            // 2. Verifica se já existe na base de dados
//                            boolean existeNaBD = repository.existsByLink(link);
//
//                            if(!existeNaBD) {
//                                linksProcessadosNestaSessao.add(link);
//                                return true;
//                            }
//                            return false;
//                        })
//                        .collect(Collectors.toList());

                // 3. Gravação em lote (mais eficiente)
                if(!paraGuardar.isEmpty()) {
                    repository.saveAll(paraGuardar);
                    System.out.println("✅ Página " + i + ": " + paraGuardar.size() + " novas notícias.");
                }
            } catch (Exception e) {
                System.out.println("❌ Erro na página " + i + ": " + e.getMessage());
            }
        }
        System.out.println("\uD83D\uDD04 Sincronização incremental terminada.");
    }
}
