package com.davi.gestaoescolar.gestao_escolar.dto;

import com.davi.gestaoescolar.gestao_escolar.model.enums.SituacaoMatricula;

import java.time.LocalDate;

public record MatriculaDTO(
    Long id,
    LocalDate dataMatricula,
    SituacaoMatricula situacao,
    Long alunoId,
    Long turmaId
) {}