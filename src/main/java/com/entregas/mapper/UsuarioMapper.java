package com.entregas.mapper;

import com.entregas.dto.request.UsuarioRequestDTO;
import com.entregas.dto.response.UsuarioResponseDTO;
import com.entregas.model.Usuario;

public class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static Usuario toEntity(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        
        usuario.setUsername(dto.getUsername());

        usuario.setSenhaHash(dto.getSenha());
        return usuario;
    }

    public static UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getUsername()
        );
    }
}