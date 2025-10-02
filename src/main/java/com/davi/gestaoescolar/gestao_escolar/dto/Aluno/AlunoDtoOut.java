package com.davi.gestaoescolar.gestao_escolar.dto.Aluno;

import com.davi.gestaoescolar.gestao_escolar.dto.Responsavel.ResponsavelDtoSimples;
import com.davi.gestaoescolar.gestao_escolar.dto.Matricula.MatriculaDtoSimples;


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
    private String email;
    private List<MatriculaDtoSimples> matriculas;
    private List<ResponsavelDtoSimples> responsaveis;

}