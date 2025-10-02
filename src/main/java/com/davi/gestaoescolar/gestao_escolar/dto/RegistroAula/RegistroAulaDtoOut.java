package com.davi.gestaoescolar.gestao_escolar.dto.RegistroAula;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegistroAulaDtoOut {
    
    private Long id;
    private LocalDate data;
    private String descricao;
    private String observacoes;

    
    // Construtor com parâmetros
    public RegistroAulaDtoOut(Long id, LocalDate data, String descricao, String observacoes) {
        this.id = id;
        this.data = data;
        this.descricao = descricao;
        this.observacoes = observacoes;
    }
}