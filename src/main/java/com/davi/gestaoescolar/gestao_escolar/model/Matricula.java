package com.davi.gestaoescolar.gestao_escolar.model;


import com.davi.gestaoescolar.gestao_escolar.model.enums.SituacaoMatricula;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "matricula")
@Getter
@Setter
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_matricula", nullable = false)
    private LocalDate dataMatricula;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SituacaoMatricula situacao;

    // Relacionamento com Aluno
    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    @JsonIgnoreProperties({"matriculas", "responsaveis", "comportamentos", "dataNascimento", "cpf", "observacoes", "ativo"})
    private Aluno aluno;

    // Relacionamento com Turma
    @ManyToOne
    @JoinColumn(name = "turma_id", nullable = false)
    @JsonBackReference("turma-matriculas")
    private Turma turma;

    // Construtores
    public Matricula() {}

    public Matricula(LocalDate dataMatricula, SituacaoMatricula situacao, Aluno aluno, Turma turma) {
        this.dataMatricula = dataMatricula;
        this.situacao = situacao;
        this.aluno = aluno;
        this.turma = turma;
    }
}