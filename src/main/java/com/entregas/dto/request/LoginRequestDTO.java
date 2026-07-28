package com.entregas.dto.request;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

public class LoginRequestDTO {
    @Schema(
    description = "Nome de usuário para autenticação",
    example = "joaovitor"
    )
    @NotBlank(message = "O username é obrigatório")
    private String username;

    @Schema(
        description = "Senha utilizada para autenticação",
        example = "Senha@123"
    )
    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    public LoginRequestDTO() {
    }

    public LoginRequestDTO(String username, String senha) {
        this.username = username;
        this.senha = senha;
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
