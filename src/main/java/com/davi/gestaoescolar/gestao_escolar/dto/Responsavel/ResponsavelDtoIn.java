package com.davi.gestaoescolar.gestao_escolar.dto.Responsavel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponsavelDtoIn {
    private String nome;
    private String telefone;
    private String cpf;
    private String parentesco;
    
    // Construtor padrão
    public ResponsavelDtoIn() {}
    
    // Construtor com parâmetros
    public ResponsavelDtoIn(String nome, String telefone, String cpf, String parentesco) {
        this.nome = nome;
        this.telefone = telefone;
        this.cpf = cpf;
        this.parentesco = parentesco;
    }
}