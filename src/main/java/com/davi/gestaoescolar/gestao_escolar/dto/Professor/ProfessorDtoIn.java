package com.davi.gestaoescolar.gestao_escolar.dto.Professor;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProfessorDtoIn {
    private String email;
    private String senha;
    private String nome;
    private String cpf;
    private String formacao;
    private String telefone;
    private LocalDate dataContratacao;
    private String cargo;
    private LocalDate dataNascimento;
    private String endereco;
    private Boolean ativo;
    
    // Construtor padrão
    public ProfessorDtoIn() {}
    
    // Construtor com parâmetros essenciais
    public ProfessorDtoIn(String email, String senha, String nome, String formacao) {
        this.email = email;
        this.senha = senha;
        this.nome = nome;
        this.formacao = formacao;
        this.ativo = true;
    }
}