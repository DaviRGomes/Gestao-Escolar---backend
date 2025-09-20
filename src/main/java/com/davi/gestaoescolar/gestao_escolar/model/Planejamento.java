package com.davi.gestaoescolar.gestao_escolar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "planejamento",
        uniqueConstraints = @UniqueConstraint(columnNames = {"disciplina_id", "turma_id", "semestre", "ano"}))
@Getter
@Setter
public class Planejamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String descricao;

    @Column(nullable = false)
    private String semestre;

    @Column(nullable = false)
    private Integer ano;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    // Relacionamento ManyToOne com Disciplina (Muitos planejamentos para uma disciplina)
    @ManyToOne
    @JoinColumn(name = "disciplina_id", nullable = false)
    @JsonIgnoreProperties({"planejamentos", "registrosAula", "turmas", "professor"})
    private Disciplina disciplina;

    // Relacionamento ManyToOne com Turma (Muitos planejamentos para uma turma)
    @ManyToOne
    @JoinColumn(name = "turma_id", nullable = false)
    @JsonIgnoreProperties({"planejamentos", "disciplinas", "matriculas", "registrosAula"})
    private Turma turma;

    // Relacionamento com ConteudosPlanejados
    @OneToMany(mappedBy = "planejamento", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"planejamento", "registrosAula"})
    private List<ConteudoPlanejado> conteudos = new ArrayList<>();

    // Construtores
    public Planejamento() {
        this.dataCriacao = LocalDateTime.now();
    }

    public Planejamento(String semestre, Integer ano, Disciplina disciplina, Turma turma, Professor professor) {
        this();
        this.semestre = semestre;
        this.ano = ano;
        this.disciplina = disciplina;
        this.turma = turma;
    }

    public Planejamento(String descricao, String semestre, Integer ano, Disciplina disciplina, Turma turma, Professor professor) {
        this(semestre, ano, disciplina, turma, professor);
        this.descricao = descricao;
    }

}