package com.davi.gestaoescolar.gestao_escolar.dto.Professor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProfessorDtoSimples {
    private Long id;
    private String nome;
    
    // Construtor padrão
    public ProfessorDtoSimples() {}
}