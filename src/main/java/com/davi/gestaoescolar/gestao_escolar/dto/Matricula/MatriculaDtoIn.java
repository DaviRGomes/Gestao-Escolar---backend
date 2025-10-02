package com.davi.gestaoescolar.gestao_escolar.dto.Matricula;

import com.davi.gestaoescolar.gestao_escolar.model.enums.SituacaoMatricula;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MatriculaDtoIn {

    private LocalDate dataMatricula;
    private SituacaoMatricula situacao;
    private Long alunoId;
    private Long turmaId;

    // Construtor padrão
    public MatriculaDtoIn() {}

    // Construtor com parâmetros
    public MatriculaDtoIn(LocalDate dataMatricula, SituacaoMatricula situacao, Long alunoId, Long turmaId) {
        this.dataMatricula = dataMatricula;
        this.situacao = situacao;
        this.alunoId = alunoId;
        this.turmaId = turmaId;
    }


}