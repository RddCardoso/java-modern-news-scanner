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

    public void processarNoticias(int numPaginas, boolean forcarCargaHistorica) {
        Set<String> linksProcessadosNestaSessao = new HashSet<>();

        // 1. Definir data limite dinâmica (ex: ir buscar à BD ou fixar 3 dias para testes)
        LocalDateTime  dataLimiteBD = repository.findTopByOrderByDataPublicacaoDesc()
                .map(Artigo::getDataPublicacao)
                .orElse(LocalDateTime.now().minusDays(3));

        boolean alcancouLimiteDeData = false;

        for(int i = 1; i <= numPaginas; i++) {

            if(alcancouLimiteDeData && !forcarCargaHistorica) {
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
                        if(forcarCargaHistorica) {
                            // Se for historico, ignoramos o limite de data e CONTINUAMOS a avaliar as outras desta página
                            log.debug("Notícia anterior à data limite, mas a continuar devido ao modo histórico.");
                            // Não fazemos break nem alteramos 'alcancouLimiteDeData' para true
                        } else {
                            log.warn("🛑 Chegámos à notícias antigas/processadas. A activar paragem dinâmica...");
                            alcancouLimiteDeData = true;
                            break;
                        }
                    }

                    String link = a.getLink();

                    // Filtro de duplicados
                    if(linksProcessadosNestaSessao.contains(link)) continue;

                    boolean existeNaBD = repository.existsByLink(link);

                    if(!existeNaBD) {
                        linksProcessadosNestaSessao.add(link);
                        paraGuardar.add(a);
                    } else if (forcarCargaHistorica) {
                        // Se já existe na BD e estamos em modo histórico, apenas saltamos para a próxima sem parar o scraper
                        continue;
                    } else {
                        // Comportamento normal do Scheduler: se já existe uma notícia na BD na página 1,
                        // assumimos que as seguintes também já existem. Activa a paragem de segurança.
                        log.warn("\uD83D\uDED1 Artigo já existe na BD. Paragem dinâmica ativada para evitar duplicados.");
                        alcancouLimiteDeData = true;
                        break;
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
