package com.davi.gestaoescolar.gestao_escolar.dto;

import com.davi.gestaoescolar.gestao_escolar.model.enums.Perfil;

import java.time.LocalDateTime;

public record UsuarioDTO(
    Long id,
    String email,
    String senha,
    Perfil perfil,
    LocalDateTime ultimoAcesso,
    Boolean ativo
) {}