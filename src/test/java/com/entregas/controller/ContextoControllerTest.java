package com.entregas.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.entregas.service.UsuarioService;

@WebMvcTest(UsuarioController.class)
class ContextoControllerTest {

    @MockBean
    private UsuarioService usuarioService;

    @Test
    void contextoCarrega() {
    }
}