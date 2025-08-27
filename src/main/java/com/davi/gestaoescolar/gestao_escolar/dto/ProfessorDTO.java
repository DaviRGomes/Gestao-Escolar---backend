package com.davi.gestaoescolar.gestao_escolar.dto;

public record ProfessorDTO(
    Long id,
    String nome,
    String cpf,
    String formacao,
    String telefone,
    String email,
    String senha,
    Long escolaId,
    Boolean ativo
) {}