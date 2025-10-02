package com.davi.gestaoescolar.gestao_escolar.dto.RegistroAula;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class RegistroAulaDtoSimples {
    private Long id;
    private LocalDate data;
    private String conteudoMinistrado;
    
    // Construtor padrão
    public RegistroAulaDtoSimples() {}
}