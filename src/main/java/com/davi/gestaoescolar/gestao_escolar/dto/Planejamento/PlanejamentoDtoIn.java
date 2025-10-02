package com.davi.gestaoescolar.gestao_escolar.dto.Planejamento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PlanejamentoDtoIn {
    private String descricao;
    private String semestre;
    private Integer ano;
    private Long disciplinaId;
    private Long turmaId;
    private LocalDateTime DataCriacao;
}