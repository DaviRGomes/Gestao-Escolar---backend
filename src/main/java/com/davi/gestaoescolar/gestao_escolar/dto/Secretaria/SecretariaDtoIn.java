package com.davi.gestaoescolar.gestao_escolar.dto.Secretaria;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SecretariaDtoIn {
    private String email;
    private String senha;
    private String nome;
    private String cpf;
    private String telefone;
    private LocalDate dataContratacao;
    private String cargo;
    private String matricula;
    private LocalDate dataNascimento;
    private String endereco;
    private Boolean ativo;
    
    // Construtor padrão
    public SecretariaDtoIn() {}
    
    // Construtor com parâmetros essenciais
    public SecretariaDtoIn(String email, String senha, String nome) {
        this.email = email;
        this.senha = senha;
        this.nome = nome;
        this.ativo = true;
    }
}