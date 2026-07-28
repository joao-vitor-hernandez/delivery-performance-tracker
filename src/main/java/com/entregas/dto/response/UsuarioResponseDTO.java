package com.entregas.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;

public class UsuarioResponseDTO {

    @Schema(
    description = "Identificador único do usuário",
    example = "1"
    )
    private Long id;

    @Schema(
        description = "Nome do usuário",
        example = "joaovitor"
    )
    private String username;

    public UsuarioResponseDTO() {
    }

    public UsuarioResponseDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}