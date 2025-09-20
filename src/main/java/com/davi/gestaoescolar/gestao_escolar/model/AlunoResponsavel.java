package com.davi.gestaoescolar.gestao_escolar.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "aluno_responsavel")
@Getter
@Setter
public class AlunoResponsavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    @JsonIgnoreProperties({"responsaveis", "matriculas", "comportamentos", "dataNascimento", "cpf", "observacoes"})
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "responsavel_id", nullable = false)
    @JsonBackReference
    private Responsavel responsavel;

    @Column(nullable = false)
    private Boolean principal = false;

    // Construtores
    public AlunoResponsavel() {}

    public AlunoResponsavel(Aluno aluno, Responsavel responsavel) {
        this.aluno = aluno;
        this.responsavel = responsavel;
    }

    public AlunoResponsavel(Aluno aluno, Responsavel responsavel, Boolean principal) {
        this.aluno = aluno;
        this.responsavel = responsavel;
        this.principal = principal;
    }

  
}