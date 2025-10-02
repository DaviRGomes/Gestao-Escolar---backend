package com.davi.gestaoescolar.gestao_escolar.dto.ConteudoPlanejado;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ConteudoPlanejadoDtoSimples {
    private Long id;
    private String conteudo;
    private LocalDate dataPlanejada;
}