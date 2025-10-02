package com.davi.gestaoescolar.gestao_escolar.dto.Usuario;

import com.davi.gestaoescolar.gestao_escolar.model.enums.Perfil;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UsuarioDtoIn {
    private String email;
    private String senha;
    private Perfil perfil;
    private LocalDate dataNascimento;
    private String endereco;
    private Boolean ativo;
    
    // Construtor padrão
    public UsuarioDtoIn() {}
    
    // Construtor com parâmetros essenciais
    public UsuarioDtoIn(String email, String senha, Perfil perfil) {
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
        this.ativo = true;
    }
}