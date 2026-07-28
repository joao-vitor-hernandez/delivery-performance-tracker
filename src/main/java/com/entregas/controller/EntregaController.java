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

// Importações do Swagger/OpenAPI
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
    name = "Entregas",
    description = "Gerenciamento das entregas dos usuários"
)
@RestController
@RequestMapping("/api/entregas")
public class EntregaController {

    private final EntregaService entregaService;

    public EntregaController(EntregaService entregaService) {
        this.entregaService = entregaService;
    }

    @Operation(
        summary = "Cadastrar entrega",
        description = "Cria uma nova entrega ou atualiza a entrega existente para a mesma data."
    )
    @ApiResponse(
        responseCode = "201",
        description = "Entrega registrada com sucesso."
    )
    @ApiResponse(
        responseCode = "400",
        description = "Dados inválidos para cadastro da entrega."
    )
    @PostMapping
    public ResponseEntity<EntregaResponseDTO> registrarEntrega(
            @Valid @RequestBody EntregaRequestDTO dto) {

        Entrega entrega = EntregaMapper.toEntity(dto);
        Entrega entregaSalva = entregaService.salvarOuAtualizar(entrega);
        EntregaResponseDTO responseDTO = EntregaMapper.toResponseDTO(entregaSalva);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }

    @Operation(
        summary = "Listar entregas do usuário",
        description = "Retorna todas as entregas do mês atual para o usuário informado."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de entregas retornada com sucesso."
    )
    @ApiResponse(
        responseCode = "404",
        description = "Usuário não encontrado."
    )
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<EntregaResponseDTO>> listarPorUsuario(
            @PathVariable Long usuarioId) {

        List<Entrega> entregas = entregaService.obterEntregasDoMesAtual(usuarioId);

        List<EntregaResponseDTO> response = entregas.stream()
                .map(EntregaMapper::toResponseDTO)
                .toList();

        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Calcular projeção Platina",
        description = "Calcula quantas entregas perfeitas ainda são necessárias para atingir a meta de 98%."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Projeção calculada com sucesso."
    )
    @ApiResponse(
        responseCode = "404",
        description = "Usuário não encontrado."
    )
    @GetMapping("/usuario/{usuarioId}/projecao")
    public ResponseEntity<Integer> obterProjecaoPlatina(
            @PathVariable Long usuarioId) {

        List<Entrega> entregas = entregaService.obterEntregasDoMesAtual(usuarioId);

        int entregasFaltantes = entregaService.calcularProjecaoPlatina(
                entregas,
                0.98
        );

        return ResponseEntity.ok(entregasFaltantes);
    }
}