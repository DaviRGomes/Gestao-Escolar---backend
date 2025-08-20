package com.davi.gestaoescolar.gestao_escolar.model;

import com.davi.gestaoescolar.gestao_escolar.model.enums.Gravidade;
import com.davi.gestaoescolar.gestao_escolar.model.enums.TipoComportamento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "comportamento")
@Getter
@Setter
public class Comportamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private TipoComportamento tipo;


    private Gravidade nivel;


    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;


    @OneToMany(mappedBy = "comportamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Aluno> alunos;


}
