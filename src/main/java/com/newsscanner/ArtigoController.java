package com.newsscanner;

import com.newsscanner.Artigo;
import com.newsscanner.ArtigoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/artigos")
public class ArtigoController {

    private static final Logger log = LoggerFactory.getLogger(ArtigoController.class);
    private final ArtigoRepository artigoRepository;

    // Construtor para injetar o Repositório
    public ArtigoController(ArtigoRepository artigoRepository) {
        this.artigoRepository = artigoRepository;
    }

    @GetMapping
    public Page<Artigo> listarTodos(@PageableDefault(size = 10) Pageable pageable) {
        log.info("\uD83C\uDF10 API: Requisição GET paginada recebida. Página: {}, Tamanho: {}",
                pageable.getPageNumber(), pageable.getPageSize());
        return artigoRepository.findAll(pageable);
    }
}
