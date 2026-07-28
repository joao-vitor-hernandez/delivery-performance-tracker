package com.entregas.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;

public class LoginResponseDTO {
    @Schema(
    description = "JWT gerado após autenticação",
    example = "eyJhbGciOiJIUzI1NiJ9..."
    )
    private String token;

    @Schema(
        description = "Tipo de token gerado",
        example = "Bearer"
    )
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
