package com.newsscanner;

import com.newsscanner.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NewsScheduler {
    private static final Logger log = LoggerFactory.getLogger(NewsScheduler.class);
    private final NewsService newsService;

    // O Spring injecta o NewsService automaticamente aqui
    public NewsScheduler(NewsService newsService) {
        this.newsService = newsService;
    }

    // initialDelay = 300000 ms(5 minutos) -> O scheduler aguarda 5 minutos após o boot antes de correr a primeira vez
    // fixedDelay = 3600000 ms Corre de hora em hora
    @Scheduled(initialDelay = 300000, fixedDelay = 3600000)
    public void executarScraperAgendado() {
        log.info("⏰ SCHEDULER: O relógio disparou! Iniciando sincronização automática...");
        try {
            newsService.processarNoticias(1, false);

            log.info("⏰ SCHEDULER: Sincronização concluída com sucesso. Próxima execução em 1 hora...");
        } catch (Exception e) {
            log.error("❌ SCHEDULER: Erro ao executar o scraper agendado:", e);
        }
    }
}
