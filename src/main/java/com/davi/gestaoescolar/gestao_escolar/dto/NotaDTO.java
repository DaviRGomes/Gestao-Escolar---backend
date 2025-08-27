package com.davi.gestaoescolar.gestao_escolar.dto;

import com.davi.gestaoescolar.gestao_escolar.model.enums.TipoAvaliacao;

import java.math.BigDecimal;

public record NotaDTO(
    Long id,
    BigDecimal valor,
    TipoAvaliacao tipo,
    BigDecimal peso,
    String observacao,
    Long registroAulaId,
    Long alunoId
) {}