package com.entregas.mapper;

import com.entregas.dto.request.EntregaRequestDTO;
import com.entregas.dto.response.EntregaResponseDTO;
import com.entregas.model.Entrega;

public class EntregaMapper {

    private EntregaMapper() {
    }

    public static Entrega toEntity(EntregaRequestDTO dto) {
        return new Entrega(
                dto.getUsuarioId(),
                dto.getData(),
                dto.getSucessos(),
                dto.getFalhas()
        );
    }

    public static EntregaResponseDTO toResponseDTO(Entrega entrega) {
        return new EntregaResponseDTO(
                entrega.getId(),
                entrega.getUsuarioId(),
                entrega.getData(),
                entrega.getSucessos(),
                entrega.getFalhas()
        );
    }
}