package com.davi.gestaoescolar.gestao_escolar.dto.Planejamento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import com.davi.gestaoescolar.gestao_escolar.dto.Disciplina.DisciplinaDtoOut;
import com.davi.gestaoescolar.gestao_escolar.dto.Turma.TurmaDtoOut;

@Getter
@Setter
@AllArgsConstructor
public class PlanejamentoDtoOut {
    private Long id;
    private String descricao;
    private String semestre;
    private Integer ano;
    private DisciplinaDtoOut disciplina;    
    private TurmaDtoOut turma;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public PlanejamentoDtoOut() {
    }
    
    // Construtor com todos os campos
    public PlanejamentoDtoOut(Long id, String descricao, String semestre, Integer ano, DisciplinaDtoOut disciplina,
            TurmaDtoOut turma) {
        this.id = id;
        this.descricao = descricao;
        this.semestre = semestre;
        this.ano = ano;
        this.disciplina = disciplina;
        this.turma = turma;
    }
  

    

}