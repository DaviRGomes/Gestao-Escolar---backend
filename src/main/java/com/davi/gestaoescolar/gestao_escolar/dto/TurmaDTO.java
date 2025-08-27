package com.davi.gestaoescolar.gestao_escolar.dto;

import com.davi.gestaoescolar.gestao_escolar.model.enums.Periodo;

public record TurmaDTO(
    Long id,
    String nome,
    String anoLetivo,
    String semestre,
    Periodo periodo,
    Boolean ativo
) {}