package com.davi.gestaoescolar.gestao_escolar.model;

import com.davi.gestaoescolar.gestao_escolar.model.enums.Periodo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "turma")
@Getter
@Setter
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "ano_letivo", nullable = false)
    private String anoLetivo;

    @Column(nullable = false)
    private String semestre;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Periodo periodo;

    @Column(nullable = false)
    private Boolean ativo = true;



    // Relacionamento com Matriculas
    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Matricula> matriculas = new ArrayList<>();

    // Relacionamento com Disciplinas (Muitos-para-Muitos)
    @ManyToMany
    @JoinTable(
            name = "turma_disciplina",
            joinColumns = @JoinColumn(name = "turma_id"),
            inverseJoinColumns = @JoinColumn(name = "disciplina_id")
    )
    private List<Disciplina> disciplinas = new ArrayList<>();

    // Relacionamento com RegistroAula
    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroAula> registrosAula = new ArrayList<>();

    // Relacionamento 1:N - Uma turma tem muitos planejamentos
    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Planejamento> planejamentos = new ArrayList<>();

    // Construtores
    public Turma() {}

    public Turma(String nome, String anoLetivo, String semestre, Periodo periodo) {
        this.nome = nome;
        this.anoLetivo = anoLetivo;
        this.semestre = semestre;
        this.periodo = periodo;
    }


}