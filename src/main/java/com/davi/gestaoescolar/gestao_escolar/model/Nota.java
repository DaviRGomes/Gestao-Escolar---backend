package com.davi.gestaoescolar.gestao_escolar.model;

import com.davi.gestaoescolar.gestao_escolar.model.enums.TipoAvaliacao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "nota")
@Getter
@Setter
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoAvaliacao tipo;

    @Column(precision = 4, scale = 2)
    private BigDecimal peso;

    @Column(length = 500)
    private String observacao;

    // Relacionamento com RegistroAula
    @ManyToOne
    @JoinColumn(name = "registro_aula_id", nullable = false)
    private RegistroAula registroAula;

    // Relacionamento com Aluno
    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    // Construtores
    public Nota() {}

    public Nota(BigDecimal valor, TipoAvaliacao tipo, RegistroAula registroAula, Aluno aluno) {
        this.valor = valor;
        this.tipo = tipo;
        this.registroAula = registroAula;
        this.aluno = aluno;
    }
}