package com.davi.gestaoescolar.gestao_escolar.dto.Presenca;

import com.davi.gestaoescolar.gestao_escolar.dto.Aluno.AlunoDtoSimples;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PresencaDtoSimples {
    private Long id;
    private Boolean presente;
    private AlunoDtoSimples aluno;
    
    // Construtor padrão
    public PresencaDtoSimples() {}
    
}