package com.entregas.dto.request;

import jakarta.validation.constraints.NotBlank;
public class UsuarioRequestDTO {

    @NotBlank(message = "O username é obrigatório")
    private String username;
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