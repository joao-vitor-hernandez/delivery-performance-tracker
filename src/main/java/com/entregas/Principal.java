package com.entregas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.entregas.repository")
@EntityScan(basePackages = "com.entregas.model")
public class Principal {

    public static void main(String[] args) {
        // Agora o Spring Boot apenas liga o servidor na porta 8080
        SpringApplication.run(Principal.class, args);
        System.out.println("\n🚀 Servidor REST iniciado com sucesso na porta 8080!");
    }
}