package com.newsscanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CargaInicialRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CargaInicialRunner.class);
    private final NewsService newsService;

    // O Spring injeta automaticamente o serviço aqui
    public CargaInicialRunner(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("\uD83D\uDE80 [CARGA INICIAL] A iniciar o carregamento histórico de 10 páginas...");

        try {
            newsService.processarNoticias(10, true);
            log.info("✅ [CARGA INICIAL] Concluída com sucesso! A base de dados está actualizada.");
        } catch (Exception e) {
            log.error("❌ [CARGA INICIAL] Erro ao executar a carga de arranque: ", e);
        }
    }
}
