package com.newsscanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

@Service
public class NewsService {

    private static final Logger log = LoggerFactory.getLogger(NewsService.class);

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
                        log.warn("\uD83D\uDED1 Chegámos a notícias antigas/processadas. A activar paragem dinâmica...");
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

                // 3. Gravação em lote (mais eficiente)
                if(!paraGuardar.isEmpty()) {
                    repository.saveAll(paraGuardar);
                    log.info("✅ Página {}: {} novas notícias adicionadas.", i, paraGuardar.size());
                }
            } catch (Exception e) {
                log.error("❌ Erro ao processar a página {}: {}", i, e.getMessage(), e);
            }
        }
        log.info("\uD83D\uDD04 Sincronização incremental terminada.");
    }
}
