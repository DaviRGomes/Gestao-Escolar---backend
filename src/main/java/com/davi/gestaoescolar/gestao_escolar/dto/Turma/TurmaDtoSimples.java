package com.davi.gestaoescolar.gestao_escolar.dto.Turma;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TurmaDtoSimples {
    private Long id;
    private String nome;
    
    // Construtor padrão
    public TurmaDtoSimples() {}
}