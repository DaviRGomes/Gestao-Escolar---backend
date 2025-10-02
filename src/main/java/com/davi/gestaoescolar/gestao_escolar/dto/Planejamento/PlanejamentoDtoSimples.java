package com.davi.gestaoescolar.gestao_escolar.dto.Planejamento;

import com.davi.gestaoescolar.gestao_escolar.dto.Disciplina.DisciplinaDtoSimples;
import com.davi.gestaoescolar.gestao_escolar.dto.Turma.TurmaDtoSimples;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlanejamentoDtoSimples {
    private Long id;
    private String descricao;
    private String semestre;
    private Integer ano;
    private DisciplinaDtoSimples disciplina;
    private TurmaDtoSimples turma;
   
}