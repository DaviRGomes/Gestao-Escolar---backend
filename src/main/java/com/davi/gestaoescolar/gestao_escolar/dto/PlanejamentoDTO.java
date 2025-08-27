package com.davi.gestaoescolar.gestao_escolar.dto;

import java.time.LocalDateTime;

public record PlanejamentoDTO(
    Long id,
    String descricao,
    String semestre,
    Integer ano,
    LocalDateTime dataCriacao,
    LocalDateTime dataAtualizacao,
    Long disciplinaId,
    Long turmaId
) {}