package com.newsscanner.controller;

import com.newsscanner.entity.Artigo;
import com.newsscanner.repository.ArtigoRepository;
import com.newsscanner.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/artigos")
@Tag(name = "News Scanner Controller", description = "Endpoints for triggering and managing the news scraper")
public class ArtigoController {

    private static final Logger log = LoggerFactory.getLogger(ArtigoController.class);
    private final ArtigoRepository artigoRepository;
    private final NewsService newsService;

    // Construtor para injetar o Repositório
    public ArtigoController(ArtigoRepository artigoRepository, NewsService newsService) {
        this.artigoRepository = artigoRepository;
        this.newsService = newsService;
    }

    @GetMapping
    public Page<Artigo> listarTodos(
            @RequestParam(required = false)  String titulo,
            @PageableDefault(size = 10) Pageable pageable) {

        if(titulo != null && !titulo.trim().isEmpty()) {
            log.info("\uD83C\uDF10 API: A pesquisar artigos com o título contendo: '{}' (Página: {})", titulo, pageable.getPageNumber());
            return artigoRepository.findByTituloContainingIgnoreCase(titulo, pageable);
        }

        log.info("\uD83C\uDF10 API: Nenhum filtro aplicado. A listar todos os artigos (Oágina: {})",  pageable.getPageNumber());
        return artigoRepository.findAll(pageable);
    }

    /// GATILHO MANUAL VIA API
    /// Aceder a http://localhost:8080/api/artigos/sincronizar?paginas=10
    @PostMapping("/sincronizar")
    @Operation(
            summary = "Trigger news scraping manually",
            description = "Triggers the scraper for a specific number of pages. Can bypass the dynamic stop using historical mode."
    )
    public ResponseEntity<String> sincronizarManualmente(
            @RequestParam(defaultValue = "1") int paginas,
            @RequestParam(defaultValue = "false") boolean forcar) {

        log.info("\uD83D\uDD79\uFE0F API: Sincronização manual forçada para {} páginas!", paginas);

        /// Executa o scraper à mão com o número de páginas que escolher
        newsService.processarNoticias(paginas, forcar, null);

        return ResponseEntity.ok("Sincronização de " + paginas + " página(s) concluída com sucesso!");
    }

    @PostMapping("/historico")
    @Operation(
            summary = "Fetch historical news since a specific date",
            description = "Triggers the scraper to go back in time and fetch all articles published since the provided date string (YYYY-MM-DD)."
    )
    public ResponseEntity<String> carregarHistorico(
            @RequestParam @Parameter(description = "Target date in format YYYY-MM-DD", example = "2026-06-01")
            String desdeData) {

        log.info("ℹ\uFE0F API: A iniciar carga histórica de notícias desde: {}", desdeData);

        // 1. Converter a String recebida para LocalDate
        LocalDate dataLimite = LocalDate.parse(desdeData);

        // 2. Chamar o serviço
        int totalCapturado = newsService.carregarNoticiasDesde(dataLimite);

        log.info("✅ API: Carga histórica finalizada. Foram guardados {} novos artigos.",  totalCapturado);

        return ResponseEntity.ok("Sincronização histórica concluída. Foram adicionados " + totalCapturado + " novos artigos.");
    }

}
