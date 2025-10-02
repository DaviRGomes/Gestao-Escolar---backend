package com.davi.gestaoescolar.gestao_escolar.dto.Responsavel;

import com.davi.gestaoescolar.gestao_escolar.dto.Aluno.AlunoDtoSimples;

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
    private List<AlunoDtoSimples> responsaveis;
    
    // Construtor padrão
    public ResponsavelDtoOut() {}

}