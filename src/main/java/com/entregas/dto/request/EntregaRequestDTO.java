package com.entregas.dto.request;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class EntregaRequestDTO {

    @NotNull(message = "O ID do usuário é obrigatório")
    private Long usuarioId;
    @NotNull(message = "A data é obrigatória")
    private LocalDate data;
    @Min(value = 0, message = "Sucessos não pode ser negativo")
    private int sucessos;
    @Min(value = 0, message = "Falhas não pode ser negativo")
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