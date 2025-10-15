package com.davi.gestaoescolar.gestao_escolar.dto.Responsavel;

import com.davi.gestaoescolar.gestao_escolar.dto.Aluno.AlunoDtoOut;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ResponsavelDtoOut {
    private Long id;
    private String nome;
    private String telefone;
    private String cpf;
    private String parentesco;
    private List<AlunoDtoOut> responsaveis;


    
    public ResponsavelDtoOut(Long id, String nome, String parentesco) {
        this.id = id;
        this.nome = nome;
        this.parentesco = parentesco;
    }


    // Construtor padrão
    public ResponsavelDtoOut() {}

}