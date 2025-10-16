package com.davi.gestaoescolar.gestao_escolar.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "responsavel")
@Getter
@Setter
public class Responsavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String telefone;

    @Column(unique = true, nullable = false)
    private String cpf;

    private String parentesco;

    // Relacionamento com Responsaveis (via tabela de junção)
    @OneToMany(mappedBy = "responsavel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"responsavel", "aluno.responsaveis", "aluno.disciplinas", 
    "aluno.dataNascimento", "aluno.cpf", "aluno.observacoes"})
    private List<AlunoResponsavel> responsaveis = new ArrayList<>();

    // Construtores
    public Responsavel() {}

    public Responsavel(String nome, String telefone, String cpf) {
        this.nome = nome;
        this.telefone = telefone;
        this.cpf = cpf;

    }
    
  
}