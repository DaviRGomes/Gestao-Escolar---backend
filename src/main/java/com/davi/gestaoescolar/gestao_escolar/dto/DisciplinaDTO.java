package com.davi.gestaoescolar.gestao_escolar.dto;

public record DisciplinaDTO(
    Long id,
    String nome,
    Integer cargaHoraria,
    String descricao,
    Boolean ativo,
    Long professorId
) {}