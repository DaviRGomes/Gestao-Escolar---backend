package com.davi.gestaoescolar.gestao_escolar.dto.Disciplina;

import com.davi.gestaoescolar.gestao_escolar.dto.Professor.ProfessorDtoOut;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DisciplinaDtoOut {
    
    private Long id;
    private String nome;
    private Integer cargaHoraria;
    private String descricao;
    private Boolean ativo;
    private ProfessorDtoOut professor;
    
    // Construtor padrão
    public DisciplinaDtoOut() {}
    
    // Construtor com parâmetros
    public DisciplinaDtoOut(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }


    
}