package com.davi.gestaoescolar.gestao_escolar.dto.Turma;

import com.davi.gestaoescolar.gestao_escolar.model.enums.Periodo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TurmaDtoOut {
    private Long id;
    private String nome;
    private String anoLetivo;
    private String semestre;
    private Periodo periodo;
    private Boolean ativo;
    
    // Construtor padrão
    public TurmaDtoOut() {}

    // Construtor para uso em outros DTOs
    public TurmaDtoOut(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}