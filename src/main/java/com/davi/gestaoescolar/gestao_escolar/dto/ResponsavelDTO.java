package com.davi.gestaoescolar.gestao_escolar.dto;

public record ResponsavelDTO(
    Long id,
    String nome,
    String telefone,
    String cpf,
    String parentesco,
    String email,
    String senha,
    Long escolaId,
    Boolean ativo
) {}