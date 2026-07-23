package com.entregas.dto.response;

public class LoginResponseDTO {
    private String token;

    public String tipo = "Bearer";

    public LoginResponseDTO() {
    }
    public LoginResponseDTO(String token){
        this.token = token;
    }

    public LoginResponseDTO(String token, String tipo) {
        this.token = token;
        this.tipo = tipo;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
