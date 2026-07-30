package com.entregas.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deve retornar 201 ao cadastrar usuário válido")
    void deveCadastrarUsuarioValido() throws Exception {

        String json = """
        {
            "username":"joao",
            "senha":"senha123"
        }
        """;

        mockMvc.perform(
                post("/api/usuarios/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve retornar 400 quando username estiver vazio")
    void deveRetornar400UsernameVazio() throws Exception {

        String json = """
        {
            "username":"",
            "senha":"senha123"
        }
        """;

        mockMvc.perform(
                post("/api/usuarios/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

}