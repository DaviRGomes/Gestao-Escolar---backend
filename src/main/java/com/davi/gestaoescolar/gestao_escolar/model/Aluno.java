package com.davi.gestaoescolar.gestao_escolar.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "aluno")
@Getter
@Setter
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(unique = true, nullable = false)
    private String cpf;

    private String observacoes;

    @Column(nullable = false)
    private Boolean ativo = true;

    // Removido: campo email
    private String email;


    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("aluno-matriculas")
    private List<Matricula> matriculas = new ArrayList<>();

    // Relacionamento com Responsaveis (via tabela de junção)
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"responsaveis.id", "responsaveis.principal"})
    private List<AlunoResponsavel> responsaveis = new ArrayList<>();


    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comportamento> comportamentos = new ArrayList<>();

    // Construtores
    public Aluno() {}

    public Aluno(String nome, LocalDate dataNascimento, String cpf) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
    }



}
