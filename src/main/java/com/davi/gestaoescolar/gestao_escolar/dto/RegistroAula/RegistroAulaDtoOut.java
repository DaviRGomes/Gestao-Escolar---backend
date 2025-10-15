package com.davi.gestaoescolar.gestao_escolar.dto.RegistroAula;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class RegistroAulaDtoOut {
    
    private Long id;
    private LocalDate data;
    private String descricao;
    private String observacoes;
    
    // Construtor com parâmetros
    public RegistroAulaDtoOut(Long id, LocalDate data, String descricao) {
        this.id = id;
        this.data = data;
        this.descricao = descricao;
    }

    public RegistroAulaDtoOut() {
    }

    
}