package com.newsscanner;

import jakarta.persistence.*;

@Entity // Define que esta classe é uma tabela no banco de dados
@Table(name= "artigos")
public  class Artigo{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // O Postgres gera o ID sozinho (1, 2, 3...)
    private Long id;

    private String titulo;

    @Column(columnDefinition = "TEXT") // TEXT permite textos longos (links ou descrições)
    private String link;

    // Construtor  vazio obrigatório para o JPA
    public Artigo() {}

    public Artigo(String titulo, String link) {
        this.titulo = titulo;
        this.link = link;
    }

    // Getters e Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}