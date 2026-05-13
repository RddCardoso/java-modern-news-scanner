package com.newsscanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        // Esta linha inicia o motor do Spring e liga-se à Base de Dados
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        System.out.println("\uD83D\uDE80 O motor Spring Boot está a funcionar e ligado ao Docker!");
    }
}