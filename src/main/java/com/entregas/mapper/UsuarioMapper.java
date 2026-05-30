package com.entregas.mapper;

import com.entregas.dto.request.UsuarioRequestDTO;
import com.entregas.dto.response.UsuarioResponseDTO;
import com.entregas.model.Usuario;

public class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static Usuario toEntity(UsuarioRequestDTO dto) {
        return new Usuario(
                dto.getUsername(),
                dto.getSenha()
        );
    }

    public static UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getUsername()
        );
    }
}