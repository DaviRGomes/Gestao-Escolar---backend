package com.davi.gestaoescolar.gestao_escolar.dto;

public record AlunoResponsavelDTO(
    Long id,
    Long alunoId,
    Long responsavelId,
    Boolean principal
) {}