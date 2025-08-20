package com.davi.gestaoescolar.gestao_escolar.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "disciplina")
@Getter
@Setter
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "carga_horaria", nullable = false)
    private Integer cargaHoraria;

    private String descricao;

    @Column(nullable = false)
    private Boolean ativo = true;

    // Relacionamento com Turmas (Muitos-para-Muitos)
    @ManyToMany(mappedBy = "disciplinas")
    private List<Turma> turmas = new ArrayList<>();

    // Relacionamento com Professores (Muitos-para-Muitos)
    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    // Relacionamento com Planejamentos
    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Planejamento> planejamentos = new ArrayList<>();

    // Relacionamento com RegistrosAula
    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroAula> registrosAula = new ArrayList<>();

    // Construtores
    public Disciplina() {}

    public Disciplina(String nome, Integer cargaHoraria) {
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
    }

    public Disciplina(String nome, Integer cargaHoraria, String descricao) {
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.descricao = descricao;
    }

    public Disciplina(String nome, Integer cargaHoraria, String descricao, Boolean ativo) {
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.descricao = descricao;
        this.ativo = ativo;
    }


    // Método toString para facilitar a visualização
    @Override
    public String toString() {
        return "Disciplina{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cargaHoraria=" + cargaHoraria +
                ", ativo=" + ativo +
                '}';
    }

    // Equals e hashCode baseados no ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Disciplina)) return false;
        Disciplina that = (Disciplina) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}