package com.entregas.controller;

import model.Entrega;
import service.EntregaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            Entrega novaEntrega = entregaService.salvarEntrega(entrega);
            // Retorna o HTTP status 201 (Created)
            return new ResponseEntity<>(novaEntrega, HttpStatus.CREATED);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Entrega>> listarPorUsuario(@PathVariable Long usuarioId){
        try{ 
            List<Entrega> entregas = entregaService.buscarPorUsuario(usuarioId);
            // Retorna HTTP Status 200 (OK)
            return new ResponseEntity<>(entregas, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
