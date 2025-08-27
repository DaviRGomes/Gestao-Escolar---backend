package com.davi.gestaoescolar.gestao_escolar.dto;

import java.time.LocalDate;

public record ConteudoPlanejadoDTO(
    Long id,
    String conteudo,
    LocalDate dataPrevista,
    LocalDate dataConclusao,
    Boolean concluido,
    String observacoes,
    Integer ordemApresentacao,
    Long planejamentoId
) {}