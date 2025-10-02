package com.davi.gestaoescolar.gestao_escolar.dto.Secretaria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SecretariaDtoSimples {
    private Long id;
    private String nome;
    private String cargo;
    
    // Construtor padrão
    public SecretariaDtoSimples() {}
}