package com.entregas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.entregas.model.Entrega;
import com.entregas.service.EntregaService;
import jakarta.validation.Valid;
import com.entregas.dto.request.EntregaRequestDTO;
import com.entregas.dto.response.EntregaResponseDTO;
import com.entregas.mapper.EntregaMapper;

import java.util.List;

@RestController
@RequestMapping("/api/entregas")
public class EntregaController {
    private final EntregaService entregaService;

    public EntregaController(EntregaService entregaService){
        this.entregaService = entregaService;
    }

    @PostMapping
    public ResponseEntity<EntregaResponseDTO> registrarEntrega(@Valid @RequestBody EntregaRequestDTO dto){
        
        Entrega entrega = EntregaMapper.toEntity(dto);
        Entrega entregaSalva = entregaService.salvarOuAtualizar(entrega);
        EntregaResponseDTO responseDTO = EntregaMapper.toResponseDTO(entregaSalva);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<EntregaResponseDTO>> listarPorUsuario(@PathVariable Long usuarioId){
        List<Entrega> entregas = entregaService.obterEntregasDoMesAtual(usuarioId);
        List<EntregaResponseDTO> response = entregas.stream().map(EntregaMapper::toResponseDTO).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuario/{usuarioId}/projecao")
    public ResponseEntity<Integer> obterProjecaoPlatina(@PathVariable Long usuarioId){
        List<Entrega> entregas = entregaService.obterEntregasDoMesAtual(usuarioId);
        int entregasFaltantes = entregaService.calcularProjecaoPlatina(entregas, 0.98);
        return ResponseEntity.ok(entregasFaltantes);
    }
}
