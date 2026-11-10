package com.newsscanner.service;

import com.newsscanner.entity.Artigo;
import com.newsscanner.repository.ArtigoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NewsService {

    private static final Logger log = LoggerFactory.getLogger(NewsService.class);

    @Autowired private ScraperService scraper;
    @Autowired private ArtigoRepository repository;

    public synchronized void processarNoticias(int numPaginas, boolean forcarCargaHistorica, LocalDateTime dataLimiteExterna) {
        Set<String> linksProcessadosNestaSessao = new HashSet<>();

        // Se vier uma data da API, usa-a. Se não, vai buscara última da BD ou o default de 3 dias.
        LocalDateTime  dataLimiteBD = (dataLimiteExterna != null) ? dataLimiteExterna : repository.findTopByOrderByDataPublicacaoDesc()
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
                // 1. Extração
                List<Artigo> capturadas = scraper.extrairNoticias(url);

                List<Artigo> paraGuardar = new ArrayList<>();

                // 2. Iteracao tradicional para permitir o uso de 'break' dinâmico
                for(Artigo a : capturadas) {

                    // CRITÉRIO DE DATA: Se a notícia for anterior à data limite, activa o stop
                    if(a.getDataPublicacao().isBefore(dataLimiteBD)) {
                        if(forcarCargaHistorica || dataLimiteExterna != null) {
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
                       // Só activa a paragem de segurança do Scheduler se NÃO for um backfill por API
                        if(dataLimiteExterna == null){
                            log.warn("\uD83D\uDD34 Artigo já existe na BD. Paragem dinâmica activada para evitar duplicados.");
                            alcancouLimiteDeData = true;
                            break;
                        } else {
                            // Se for carga histórica por API, saltamos o artigo duplicado e continuamos a escavar!
                            continue;
                        }
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

    public int carregarNoticiasDesde(LocalDate dataLimite) {
        // 1. Converter para LocalDateTime (00:00:00 do dia indicado) para bater certo com o isBefore()
        LocalDateTime limiteComHora = dataLimite.atStartOfDay();

        // 2. Guardar o número de registos antes da importação para sabermos quantos novos entraram
        long antesDaCarga = repository.count();

        log.info("🚀 A iniciar backfill planeado para ler até: {}", limiteComHora);

        // 3. Chamamos o processador com um tecto alto de páginas (ex: 50 ou 100), falso para forçar o histórico (para ele respeitar o break por data)
        processarNoticias(100, false, limiteComHora);

        // 4. Calcular o delta de novos artigos inseridos
        long depoisDaCarga = repository.count();
        return (int) (depoisDaCarga - antesDaCarga);
    }
}
