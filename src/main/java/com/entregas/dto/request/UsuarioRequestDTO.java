package com.entregas.dto.request;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
public class UsuarioRequestDTO {

    @Schema(description = "Nome do usuário", example = "joao")
    @NotBlank(message = "O username é obrigatório")
    private String username;

    @Schema(description = "Senha do usuário", example = "senha123@")
    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    public UsuarioRequestDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}