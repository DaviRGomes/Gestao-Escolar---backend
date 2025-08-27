package com.davi.gestaoescolar.gestao_escolar.dto;

import java.time.LocalDate;

public record RegistroAulaDTO(
    Long id,
    LocalDate data,
    String descricao,
    String observacoes,
    Long turmaId,
    Long disciplinaId,
    Long conteudoPlanejadoId
) {}