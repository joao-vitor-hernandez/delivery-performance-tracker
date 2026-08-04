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
import com.entregas.model.Usuario;
import com.entregas.service.EntregaService;
import com.entregas.service.UsuarioService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class EntregaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EntregaService entregaService;

    @MockBean
    private UsuarioService usuarioService;

        @Test
        @WithMockUser(username = "teste2")
        @DisplayName("Deve cadastrar entrega válida")
        void deveCadastrarEntregaValida() throws Exception {

        Usuario usuario = new Usuario(
                1L,
                "teste2",
                "senhaHash"
        );

        Entrega entrega = new Entrega(
                1L,
                1L,
                LocalDate.of(2026, 7, 21),
                50,
                2
        );

        when(usuarioService.buscarPorUsername("teste2"))
                .thenReturn(usuario);

        when(entregaService.salvarOuAtualizar(any()))
                .thenReturn(entrega);

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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.usuarioId").value(1))
                .andExpect(jsonPath("$.sucessos").value(50))
                .andExpect(jsonPath("$.falhas").value(2));
        }

        @Test
        @WithMockUser(username = "teste2")
        @DisplayName("Deve listar entregas do usuário autenticado")
        void deveListarEntregasDoUsuario() throws Exception {

        Usuario usuario = new Usuario(
                1L,
                "teste2",
                "senhaHash"
        );

        List<Entrega> lista = List.of(
                new Entrega(
                        1L,
                        1L,
                        LocalDate.of(2026, 7, 20),
                        40,
                        1
                )
        );

        when(usuarioService.buscarPorUsername("teste2"))
                .thenReturn(usuario);

        when(entregaService.obterEntregasDoMesAtual(1L))
                .thenReturn(lista);

        mockMvc.perform(get("/api/entregas/minhas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioId").value(1))
                .andExpect(jsonPath("$[0].sucessos").value(40))
                .andExpect(jsonPath("$[0].falhas").value(1));
        }

        @Test
        @WithMockUser(username = "teste2")
        @DisplayName("Deve retornar projeção Platina do usuário autenticado")
        void deveRetornarProjecaoPlatina() throws Exception {

        Usuario usuario = new Usuario(
                1L,
                "teste2",
                "senhaHash"
        );

        List<Entrega> lista = List.of(
                new Entrega(
                        1L,
                        1L,
                        LocalDate.now(),
                        80,
                        20
                )
        );

        when(usuarioService.buscarPorUsername("teste2"))
                .thenReturn(usuario);

        when(entregaService.obterEntregasDoMesAtual(1L))
                .thenReturn(lista);

        when(entregaService.calcularProjecaoPlatina(anyList(), eq(0.98)))
                .thenReturn(900);

        mockMvc.perform(get("/api/entregas/minhas/projecao"))
                .andExpect(status().isOk())
                .andExpect(content().string("900"));
        }

        @Test
        @WithMockUser(username = "teste2")
        @DisplayName("Deve retornar 400 quando data não for informada")
        void deveRetornar400QuandoDataNaoInformada() throws Exception {

        String json = """
                {
                "sucessos":50,
                "falhas":2
                }
                """;

        mockMvc.perform(post("/api/entregas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("A data é obrigatória"));
        }
}