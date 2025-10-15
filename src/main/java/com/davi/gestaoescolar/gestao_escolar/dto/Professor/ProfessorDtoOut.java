package com.davi.gestaoescolar.gestao_escolar.dto.Professor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ProfessorDtoOut {
    private Long id;
    private String nome;
    private String cpf;
    private String formacao;
    private String telefone;
    private LocalDate dataContratacao;
    private String cargo;
    private Boolean ativo;
    
    public ProfessorDtoOut(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

}