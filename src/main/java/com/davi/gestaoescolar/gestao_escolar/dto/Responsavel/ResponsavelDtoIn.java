package com.davi.gestaoescolar.gestao_escolar.dto.Responsavel;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponsavelDtoIn {
    private String nome;
    private String telefone;
    private String cpf;
    private String parentesco;
}