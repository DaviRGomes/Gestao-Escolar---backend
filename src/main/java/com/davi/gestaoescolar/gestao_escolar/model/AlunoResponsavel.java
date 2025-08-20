package com.davi.gestaoescolar.gestao_escolar.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "aluno_responsavel")
@Getter
@Setter
public class AlunoResponsavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "responsavel_id", nullable = false)
    private Responsavel responsavel;

    @Column(nullable = false)
    private Boolean principal = false;

    // Construtores
    public AlunoResponsavel() {}

    public AlunoResponsavel(Aluno aluno, Responsavel responsavel) {
        this.aluno = aluno;
        this.responsavel = responsavel;
    }

    public AlunoResponsavel(Aluno aluno, Responsavel responsavel, Boolean principal) {
        this.aluno = aluno;
        this.responsavel = responsavel;
        this.principal = principal;
    }

    // Equals e hashCode para comparar instâncias (importante para remoção de coleções)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AlunoResponsavel)) return false;
        AlunoResponsavel that = (AlunoResponsavel) o;
        return aluno != null && aluno.equals(that.aluno) &&
                responsavel != null && responsavel.equals(that.responsavel);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}