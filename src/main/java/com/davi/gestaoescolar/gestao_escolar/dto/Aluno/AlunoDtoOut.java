package com.davi.gestaoescolar.gestao_escolar.dto.Aluno;

import com.davi.gestaoescolar.gestao_escolar.dto.Responsavel.ResponsavelDtoOut;
import com.davi.gestaoescolar.gestao_escolar.dto.Matricula.MatriculaDtoOut;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    public AlunoDtoOut(Long id, String nome, LocalDate dataNascimento, String cpf, String observacoes, Boolean ativo,
            List<MatriculaDtoOut> matriculas, List<ResponsavelDtoOut> responsaveis) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.observacoes = observacoes;
        this.ativo = ativo;
        this.matriculas = matriculas;
        this.responsaveis = responsaveis;
    }

    



    

}