package com.davi.gestaoescolar.gestao_escolar.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "registro_aula")
@Getter
@Setter
public class RegistroAula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false, length = 1000)
    private String descricao;

    @Column(length = 500)
    private String observacoes;

    // Relacionamento com Turma
    @ManyToOne
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    // Relacionamento com Disciplina
    @ManyToOne
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    // Relacionamento com Presencas
    @OneToMany(mappedBy = "registroAula", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Presenca> presencas = new ArrayList<>();

    // Relacionamento com Notas
    @OneToMany(mappedBy = "registroAula", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Nota> notas = new ArrayList<>();

    // Relacionamento com ConteudoPlanejado
    @ManyToOne
    @JoinColumn(name = "conteudo_planejado_id")
    private ConteudoPlanejado conteudoPlanejado;

    // Construtores
    public RegistroAula() {}

    public RegistroAula(LocalDate data, String descricao, Turma turma, Disciplina disciplina) {
        this.data = data;
        this.descricao = descricao;
        this.turma = turma;
        this.disciplina = disciplina;
    }

    public RegistroAula(LocalDate data, String descricao, String observacoes, Turma turma, Disciplina disciplina) {
        this.data = data;
        this.descricao = descricao;
        this.observacoes = observacoes;
        this.turma = turma;
        this.disciplina = disciplina;
    }

}