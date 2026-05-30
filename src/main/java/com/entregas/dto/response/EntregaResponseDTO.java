package com.entregas.dto.response;

import java.time.LocalDate;

public class EntregaResponseDTO {
    
    private Long id;
    private Long usuarioId;
    private LocalDate data;
    private int sucessos;
    private int falhas;

    public EntregaResponseDTO() {}
    public EntregaResponseDTO(Long id, Long usuarioId, LocalDate data, int sucessos, int falhas) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.data = data;
        this.sucessos = sucessos;
        this.falhas = falhas;
    }

    public Long getId() {return id;}
    public Long getUsuarioId() {return usuarioId;}
    public LocalDate getData() {return data;}
    public int getSucessos() {return sucessos;}
    public int getFalhas() {return falhas;}
}
