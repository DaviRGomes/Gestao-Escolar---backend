package com.davi.gestaoescolar.gestao_escolar.dto.Disciplina;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DisciplinaDtoIn {
    
    private String nome;
    private Integer cargaHoraria;
    private String descricao;
    private Boolean ativo;
    private Long professorId;
    
    // Construtor padrão
    public DisciplinaDtoIn() {}
    
    // Construtor com parâmetros
    public DisciplinaDtoIn(String nome, Integer cargaHoraria, String descricao, 
                          Boolean ativo, Long professorId) {
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.descricao = descricao;
        this.ativo = ativo;
        this.professorId = professorId;
    }
}