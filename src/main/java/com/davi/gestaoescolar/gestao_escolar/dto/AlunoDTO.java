package com.davi.gestaoescolar.gestao_escolar.dto;

import java.time.LocalDate;

public record AlunoDTO(
    Long id,
    String nome,
    LocalDate dataNascimento,
    String matricula,
    String observacoes,
    Boolean ativo,
    Long comportamentoId
) {}