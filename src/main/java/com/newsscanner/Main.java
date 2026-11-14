package com.newsscanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Main entry point for the News Scanner Spring Boot application.
 * Enables component scanning, autoconfiguration, and scheduling capabilities
 */
@SpringBootApplication
@EnableScheduling
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    /**
     * Application bootstrap method.
     *
     * @param args Command line arguments passed during startup
     */
    public static void main(String[] args) {
        // Initialize Spring Ioc Container and connect to the PostgreSQL database
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        log.info("[BOOT] Spring Boot application is up and running, connected to Docker infrastructure!");
    }
}