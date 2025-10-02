package com.davi.gestaoescolar.gestao_escolar.dto.Disciplina;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DisciplinaDtoSimples {
    private Long id;
    private String nome;
    
    // Construtor padrão
    public DisciplinaDtoSimples() {}
}