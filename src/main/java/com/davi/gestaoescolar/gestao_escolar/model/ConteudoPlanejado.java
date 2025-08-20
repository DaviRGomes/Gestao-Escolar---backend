package com.davi.gestaoescolar.gestao_escolar.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conteudo_planejado")
@Getter
@Setter
public class ConteudoPlanejado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String conteudo;

    @Column(name = "data_prevista", nullable = false)
    private LocalDate dataPrevista;

    @Column(name = "data_conclusao")
    private LocalDate dataConclusao;

    @Column(nullable = false)
    private Boolean concluido = false;

    @Column(length = 1000)
    private String observacoes;

    @Column(name = "ordem_apresentacao")
    private Integer ordemApresentacao;

    // Relacionamento com Planejamento (Many-to-One)
    @ManyToOne
    @JoinColumn(name = "planejamento_id", nullable = false)
    private Planejamento planejamento;

    // Relacionamento com RegistrosAula (One-to-Many)
    @OneToMany(mappedBy = "conteudoPlanejado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroAula> registrosAula = new ArrayList<>();

    // Construtores
    public ConteudoPlanejado() {}

    public ConteudoPlanejado(String conteudo, LocalDate dataPrevista, Planejamento planejamento) {
        this.conteudo = conteudo;
        this.dataPrevista = dataPrevista;
        this.planejamento = planejamento;
    }

    public ConteudoPlanejado(String conteudo, LocalDate dataPrevista, String observacoes, Planejamento planejamento) {
        this(conteudo, dataPrevista, planejamento);
        this.observacoes = observacoes;
    }


}