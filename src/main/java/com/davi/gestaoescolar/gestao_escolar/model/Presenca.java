package com.davi.gestaoescolar.gestao_escolar.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "presenca")
@Getter
@Setter
public class Presenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean presente;

    @Column(length = 500)
    private String justificativa;

    // Relacionamento com RegistroAula
    @ManyToOne
    @JoinColumn(name = "registro_aula_id", nullable = false)
    private RegistroAula registroAula;

    // Relacionamento com Aluno
    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    // Construtores
    public Presenca() {}

    public Presenca(Boolean presente, RegistroAula registroAula, Aluno aluno) {
        this.presente = presente;
        this.registroAula = registroAula;
        this.aluno = aluno;
    }

    public Presenca(Boolean presente, String justificativa, RegistroAula registroAula, Aluno aluno) {
        this.presente = presente;
        this.justificativa = justificativa;
        this.registroAula = registroAula;
        this.aluno = aluno;
    }
}