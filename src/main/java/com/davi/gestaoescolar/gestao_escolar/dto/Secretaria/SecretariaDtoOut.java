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
    public SecretariaDtoOut(Long id, String email, String nome, String cpf, String telefone,
                           LocalDate dataContratacao, String cargo, String matricula,
                           Perfil perfil, LocalDateTime ultimoAcesso, LocalDate dataNascimento,
                           String endereco, Boolean ativo) {
        this.id = id;
        this.email = email;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.dataContratacao = dataContratacao;
        this.cargo = cargo;
        this.matricula = matricula;
        this.perfil = perfil;
        this.ultimoAcesso = ultimoAcesso;
        this.dataNascimento = dataNascimento;
        this.endereco = endereco;
        this.ativo = ativo;
    }
}