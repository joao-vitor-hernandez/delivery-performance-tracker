package com.entregas.controller;

import com.entregas.service.EntregaService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(EntregaController.class)
class EntregaControllerTest {

    @MockBean
    private EntregaService entregaService;

    @Test
    void contextoCarrega() {
    }
}