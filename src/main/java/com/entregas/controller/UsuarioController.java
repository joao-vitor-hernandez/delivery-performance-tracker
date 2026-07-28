package com.entregas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.entregas.model.Usuario;
import com.entregas.service.UsuarioService;
import jakarta.validation.Valid;
import com.entregas.dto.request.UsuarioRequestDTO;
import com.entregas.dto.response.UsuarioResponseDTO;
import com.entregas.mapper.UsuarioMapper;
//import com.entregas.model.Usuario;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Tag(
    name = "Usuários",
    description = "Operações relacionadas aos usuários"
)
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    
    @Autowired
    private final UsuarioService usuarioService;
    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    //Endpoint para criar novo usuário
    //Rota: POST http://localhost:8080/api/usuarios/registrar

    @Operation(
        summary = "Cadastrar usuário",
        description = "Cria um novo usuário no sistema"
    )
    @ApiResponse(
        responseCode = "201",
        description = "Usuário criado com sucesso"
    )
    @ApiResponse(
        responseCode = "400",
        description = "Dados inválidos"
    )
    @PostMapping("/registrar")
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(
            @Valid @RequestBody UsuarioRequestDTO dto) {

        Usuario usuario = UsuarioMapper.toEntity(dto);
        Usuario usuarioSalvo = usuarioService.cadastrarUsuario(usuario);
        UsuarioResponseDTO responseDTO = UsuarioMapper.toResponseDTO(usuarioSalvo);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }
}
