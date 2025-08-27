package com.davi.gestaoescolar.gestao_escolar.dto;

import com.davi.gestaoescolar.gestao_escolar.model.enums.Gravidade;
import com.davi.gestaoescolar.gestao_escolar.model.enums.TipoComportamento;

import java.time.LocalDate;

public record ComportamentoDTO(
    Long id,
    String descricao,
    LocalDate date,
    TipoComportamento tipo,
    Gravidade nivel,
    Long professorId
) {}