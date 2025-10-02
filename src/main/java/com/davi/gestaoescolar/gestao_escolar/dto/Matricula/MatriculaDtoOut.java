package com.davi.gestaoescolar.gestao_escolar.dto.Matricula;

import com.davi.gestaoescolar.gestao_escolar.dto.Aluno.AlunoDtoSimples;
import com.davi.gestaoescolar.gestao_escolar.dto.Turma.TurmaDtoSimples;
import com.davi.gestaoescolar.gestao_escolar.model.enums.SituacaoMatricula;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class MatriculaDtoOut {

    private Long id;
    private LocalDate dataMatricula;
    private SituacaoMatricula situacao;
    private AlunoDtoSimples aluno;
    private TurmaDtoSimples turma;

    // Construtor padrão
    public MatriculaDtoOut() {}

}