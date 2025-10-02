package com.davi.gestaoescolar.gestao_escolar.dto.Usuario;

import com.davi.gestaoescolar.gestao_escolar.model.enums.Perfil;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class UsuarioDtoOut {
    private Long id;
    private String email;
    private Perfil perfil;
    private LocalDateTime ultimoAcesso;
    private LocalDate dataNascimento;
    private String endereco;
    private Boolean ativo;
    
    // Construtor padrão
    public UsuarioDtoOut() {}
    
    // Construtor com parâmetros
    public UsuarioDtoOut(Long id, String email, Perfil perfil, LocalDateTime ultimoAcesso,
                        LocalDate dataNascimento, String endereco, Boolean ativo) {
        this.id = id;
        this.email = email;
        this.perfil = perfil;
        this.ultimoAcesso = ultimoAcesso;
        this.dataNascimento = dataNascimento;
        this.endereco = endereco;
        this.ativo = ativo;
    }
}