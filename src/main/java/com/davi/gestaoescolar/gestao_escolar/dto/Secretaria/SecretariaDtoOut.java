package com.davi.gestaoescolar.gestao_escolar.dto.Secretaria;

import com.davi.gestaoescolar.gestao_escolar.model.enums.Perfil;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class SecretariaDtoOut {
    private Long id;
    private String email;
    private String nome;
    private String cpf;
    private String telefone;
    private LocalDate dataContratacao;
    private String cargo;
    private String matricula;
    private Perfil perfil;
    private LocalDateTime ultimoAcesso;
    private LocalDate dataNascimento;
    private String endereco;
    private Boolean ativo;
    
    // Construtor padrão
    public SecretariaDtoOut() {}
    
    // Construtor com parâmetros
    public SecretariaDtoOut(Long id, String nome, String cargo) {
        this.id = id;
        this.nome = nome;
        this.cargo = cargo;
    }
}