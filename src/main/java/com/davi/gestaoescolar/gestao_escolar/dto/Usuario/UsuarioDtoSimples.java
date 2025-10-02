package com.davi.gestaoescolar.gestao_escolar.dto.Usuario;

import com.davi.gestaoescolar.gestao_escolar.model.enums.Perfil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UsuarioDtoSimples {
    private Long id;
    private String email;
    private Perfil perfil;
    
    // Construtor padrão
    public UsuarioDtoSimples() {}
}