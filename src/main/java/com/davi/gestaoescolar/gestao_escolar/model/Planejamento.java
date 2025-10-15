package com.davi.gestaoescolar.gestao_escolar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "planejamento",
        uniqueConstraints = @UniqueConstraint(columnNames = {"disciplina_id", "turma_id", "semestre", "ano"}))
@Getter
@Setter
@NoArgsConstructor
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

    public Planejamento(String descricao, String semestre, Integer ano, LocalDateTime dataCriacao,
                        LocalDateTime dataAtualizacao, Disciplina disciplina,
                        Turma turma, List<ConteudoPlanejado> conteudos) {
        this.descricao = descricao;
        this.semestre = semestre;
        this.ano = ano;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.disciplina = disciplina;
        this.turma = turma;
        this.conteudos = conteudos;
    }

    // Getters explícitos
    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getSemestre() {
        return semestre;
    }

    public Integer getAno() {
        return ano;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public Turma getTurma() {
        return turma;
    }

    public List<ConteudoPlanejado> getConteudos() {
        return conteudos;
    }

    // Setters explícitos
    public void setId(Long id) {
        this.id = id;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public void setConteudos(List<ConteudoPlanejado> conteudos) {
        this.conteudos = conteudos;
    }
}