package com.entregas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.entregas.model.Entrega;
import com.entregas.service.EntregaService;

import java.util.List;

@RestController
@RequestMapping("/api/entregas")
public class EntregaController {
    private final EntregaService entregaService;

    public EntregaController(EntregaService entregaService){
        this.entregaService = entregaService;
    }

    @PostMapping
    public ResponseEntity<Entrega> registrarEntrega(@RequestBody Entrega entrega){
        try {
            Entrega novaEntrega = entregaService.salvarOuAtualizar(entrega);
            // Retorna o HTTP status 201 (Created)
            return new ResponseEntity<>(novaEntrega, HttpStatus.CREATED);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Entrega>> listarPorUsuario(@PathVariable Long usuarioId){
        try{ 
            List<Entrega> entregas = entregaService.obterEntregasDoMesAtual(usuarioId);
            // Retorna HTTP Status 200 (OK)
            return new ResponseEntity<>(entregas, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/usuario/{usuarioId}/projecao")
    public ResponseEntity<Integer> obterProjecaoPlatina(@PathVariable Long usuarioId){
        try {
            List<Entrega> entregas = entregaService.obterEntregasDoMesAtual(usuarioId);
            int entregasFaltantes = entregaService.calcularProjecaoPlatina(entregas, 0.98);
            //Retorna HTTP Status 200 (OK)
            return new ResponseEntity<>(entregasFaltantes, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
