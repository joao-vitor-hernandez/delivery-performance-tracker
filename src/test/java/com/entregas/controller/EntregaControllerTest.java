package com.entregas.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import com.entregas.model.Entrega;
import com.entregas.service.EntregaService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class EntregaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EntregaService entregaService;

    @Test
    @DisplayName("Deve cadastrar entrega válida")
    void deveCadastrarEntregaValida() throws Exception {

        Entrega entrega = new Entrega(
                1L,
                LocalDate.of(2026,7,21),
                50,
                2);

        when(entregaService.salvarOuAtualizar(any()))
                .thenReturn(entrega);

        String json = """
            {
                "usuarioId":1,
                "data":"2026-07-21",
                "sucessos":50,
                "falhas":2
            }
            """;

        mockMvc.perform(post("/api/entregas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve retornar 400 quando usuarioId não for informado")
    void deveRetornar400QuandoUsuarioIdNaoInformado() throws Exception {

        String json = """
            {
                "data":"2026-07-21",
                "sucessos":50,
                "falhas":2
            }
            """;

        mockMvc.perform(post("/api/entregas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve listar entregas do usuário")
    void deveListarEntregasPorUsuario() throws Exception {

        List<Entrega> lista = List.of(

                new Entrega(
                        1L,
                        LocalDate.of(2026,7,20),
                        40,
                        1)

        );

        when(entregaService.obterEntregasDoMesAtual(1L))
                .thenReturn(lista);

        mockMvc.perform(get("/api/entregas/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioId").value(1))
                .andExpect(jsonPath("$[0].sucessos").value(40))
                .andExpect(jsonPath("$[0].falhas").value(1));
    }

    @Test
    @DisplayName("Deve retornar projeção Platina")
    void deveRetornarProjecaoPlatina() throws Exception {

        List<Entrega> lista = List.of(
                new Entrega(
                        1L,
                        LocalDate.now(),
                        80,
                        20));

        when(entregaService.obterEntregasDoMesAtual(1L))
                .thenReturn(lista);

        when(entregaService.calcularProjecaoPlatina(anyList(), eq(0.98)))
                .thenReturn(900);

        mockMvc.perform(get("/api/entregas/usuario/1/projecao"))
                .andExpect(status().isOk())
                .andExpect(content().string("900"));
    }
}