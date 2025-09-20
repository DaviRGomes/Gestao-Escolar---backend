package com.davi.gestaoescolar.gestao_escolar.model;

import com.davi.gestaoescolar.gestao_escolar.model.enums.Perfil;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "professor")
@PrimaryKeyJoinColumn(name = "usuario_id")
@Getter
@Setter
public class Professor extends Usuario {

   @Column(nullable = false, length = 100)
    private String nome;

    @Column(unique = true, length = 14)
    private String cpf;

    @Column(nullable = false, length = 100)
    private String formacao;

    @Column(length = 15)
    private String telefone;

    @Column(name = "data_contratacao")
    private LocalDate dataContratacao;

    @Column(length = 50)
    private String cargo;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Disciplina> disciplinas = new ArrayList<>();

    public Professor() {
        super();
        this.setPerfil(Perfil.PROFESSOR);
    }

    public Professor(String email, String senha, String nome) {
        super(email, senha, Perfil.PROFESSOR);
        this.nome = nome;
        this.dataContratacao = LocalDate.now();
    }

    public Professor(String email, String senha, String nome, String cpf, String telefone) {
        super(email, senha, Perfil.PROFESSOR);
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.dataContratacao = LocalDate.now();
    }

}