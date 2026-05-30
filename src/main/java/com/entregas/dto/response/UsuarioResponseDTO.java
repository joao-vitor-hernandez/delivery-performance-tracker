package com.entregas.dto.response;

public class UsuarioResponseDTO {
    private Long id;
    private String username;

    public UsuarioResponseDTO() {}
    public UsuarioResponseDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {return id;}
    
    public String getUsername() {return username;}
}
