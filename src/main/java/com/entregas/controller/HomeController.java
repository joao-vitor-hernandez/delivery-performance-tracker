package com.entregas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public String boasVindas(){
        return "API do Monitor de Desempenho de Entregas (Nível Platina) rodando com sucesso.";
    }
}
