package com.davi.gestaoescolar.gestao_escolar.dto;

public record PresencaDTO(
    Long id,
    Boolean presente,
    String justificativa,
    Long registroAulaId,
    Long alunoId
) {}