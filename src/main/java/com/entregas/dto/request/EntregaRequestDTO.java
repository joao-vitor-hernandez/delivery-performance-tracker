package com.entregas.dto.request;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import io.swagger.v3.oas.annotations.media.Schema;

public class EntregaRequestDTO {

    @Schema(
        description = "Data da entrega",
        example = "2026-07-28"
    )
    @NotNull(message = "A data é obrigatória")
    private LocalDate data;

    @Schema(
        description = "Quantidade de entregas realizadas com sucesso",
        example = "95"
    )
    @Min(value = 0, message = "Sucessos não pode ser negativo")
    private int sucessos;

    @Schema(
        description = "Quantidade de entregas com falha",
        example = "5"
    )
    @Min(value = 0, message = "Falhas não pode ser negativo")
    private int falhas;

    public EntregaRequestDTO() {
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