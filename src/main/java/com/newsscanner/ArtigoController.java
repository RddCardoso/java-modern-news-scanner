package com.newsscanner;

import com.newsscanner.Artigo;
import com.newsscanner.ArtigoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artigos")
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
    public ResponseEntity<String> sincronizarManualmente(
            @RequestParam(defaultValue = "1") int paginas,
            @RequestParam(defaultValue = "false") boolean forcar) {

        log.info("\uD83D\uDD79\uFE0F API: Sincronização manual forçada para {} páginas!", paginas);

        /// Executa o scraper à mão com o número de páginas que escolher
        newsService.processarNoticias(paginas, forcar);

        return ResponseEntity.ok("Sincronização de " + paginas + " página(s) concluída com sucesso!");
    }
}
