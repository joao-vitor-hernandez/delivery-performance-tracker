package com.entregas.dto.response;

import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;

public class EntregaResponseDTO {
    @Schema(
    description = "Identificador da entrega",
    example = "15"
    )
    private Long id;

    @Schema(
        description = "ID do usuário",
        example = "1"
    )
    private Long usuarioId;

    @Schema(
        description = "Data da entrega",
        example = "2026-07-28"
    )
    private LocalDate data;

    @Schema(
        description = "Entregas concluídas com sucesso",
        example = "95"
    )
    private int sucessos;

    @Schema(
        description = "Entregas com falha",
        example = "5"
    )
    private int falhas;

    public EntregaResponseDTO() {
    }

    public EntregaResponseDTO(
            Long id,
            Long usuarioId,
            LocalDate data,
            int sucessos,
            int falhas) {

        this.id = id;
        this.usuarioId = usuarioId;
        this.data = data;
        this.sucessos = sucessos;
        this.falhas = falhas;
    }

    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public LocalDate getData() {
        return data;
    }

    public int getSucessos() {
        return sucessos;
    }

    public int getFalhas() {
        return falhas;
    }
}