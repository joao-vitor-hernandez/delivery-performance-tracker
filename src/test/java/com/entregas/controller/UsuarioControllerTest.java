package com.entregas.controller;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.entregas.model.Usuario;
import com.entregas.service.UsuarioService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UsuarioService usuarioService;

    @Test
    @DisplayName("Deve retornar 201 ao cadastrar usuário válido")
    void deveCadastrarUsuarioValido() throws Exception {
        Usuario usuarioSalvo = new Usuario("joao", "hash");
        when(usuarioService.cadastrarUsuario(any())).thenReturn(usuarioSalvo);
        String json = """
            {
                "username": "joao",
                "senha": "senha123"
            }    
                """;
        mockMvc.perform(post("/api/usuarios/registrar").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve retornar 400 quando username estiver vazio")
    void deveRetornar400UsernameVazio() throws Exception {
        String json = """
            {
                "username": "",
                "senha": "senha123"
            }
                """;
        mockMvc.perform(post("/api/usuarios/registrar").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 400 quando usuário já existir")
    void deveRetornar400UsuarioExistente() throws Exception {
        when(usuarioService.cadastrarUsuario(any())).thenThrow(new IllegalArgumentException("Este usuário já está cadastrado no sistema!"));
        String json = """
            {
                "username": "joao",
                "senha": "senha123"
            }
                """;
        mockMvc.perform(post("/api/usuarios/registrar").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
    }
}
