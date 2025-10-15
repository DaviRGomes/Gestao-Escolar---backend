package com.davi.gestaoescolar.gestao_escolar.dto.Aluno;

import com.davi.gestaoescolar.gestao_escolar.dto.Responsavel.ResponsavelDtoOut;
import com.davi.gestaoescolar.gestao_escolar.dto.Matricula.MatriculaDtoOut;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AlunoDtoOut {
    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    private String cpf;
    private String observacoes;
    private Boolean ativo;
    // Removido: private String email;
    private List<MatriculaDtoOut> matriculas;
    private List<ResponsavelDtoOut> responsaveis;
    
    public AlunoDtoOut(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    

}