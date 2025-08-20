package com.davi.gestaoescolar.gestao_escolar.model;

import com.davi.gestaoescolar.gestao_escolar.model.enums.Perfil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "professor")
@PrimaryKeyJoinColumn(name = "usuario_id")
@Getter
@Setter
public class Professor extends Usuario {

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String cpf;

    private String formacao;

    private String telefone;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comportamento> comportamentos;

    // Construtores
    public Professor() {}

    public Professor(String email, String senha, String nome, String cpf) {
        super(email, senha, Perfil.PROFESSOR);
        this.nome = nome;
        this.cpf = cpf;
    }

}