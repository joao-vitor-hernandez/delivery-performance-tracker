package com.entregas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Principal {
    public static void main(String[] args) {
        // Agora o Spring Boot apenas liga o servidor na porta 8080
        SpringApplication.run(Principal.class, args);
        System.out.println("\n🚀 Servidor REST iniciado com sucesso na porta 8080!");
    }
}