package com.newsscanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableScheduling
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // Esta linha inicia o motor do Spring e liga-se à Base de Dados
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        log.info("\uD83D\uDE80 O motor Spring Boot está a funcionar e ligado ao Docker!");
    }
}