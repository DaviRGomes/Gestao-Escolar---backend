package com.davi.gestaoescolar.gestao_escolar.dto.Responsavel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponsavelDtoSimples {
    private Long id;
    private String nome;
    private String parentesco;
    
    // Construtor padrão
    public ResponsavelDtoSimples() {}
}