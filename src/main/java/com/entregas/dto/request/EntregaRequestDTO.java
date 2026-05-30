package com.entregas.dto.request;

import java.time.LocalDate;

public class EntregaRequestDTO {

    private Long usuarioId;
    private LocalDate data;
    private int sucessos;
    private int falhas;

    public EntregaRequestDTO() {
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public int getSucessos() {
        return sucessos;
    }

    public void setSucessos(int sucessos) {
        this.sucessos = sucessos;
    }

    public int getFalhas() {
        return falhas;
    }

    public void setFalhas(int falhas) {
        this.falhas = falhas;
    }
}