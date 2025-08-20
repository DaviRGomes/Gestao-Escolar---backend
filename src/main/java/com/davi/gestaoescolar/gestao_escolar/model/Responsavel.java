package com.davi.gestaoescolar.gestao_escolar.model;

import com.davi.gestaoescolar.gestao_escolar.model.enums.Perfil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "responsavel")
@PrimaryKeyJoinColumn(name = "usuario_id")
@Getter
@Setter
public class Responsavel extends Usuario {

    @Column(nullable = false)
    private String nome;

    private String telefone;

    @Column(unique = true, nullable = false)
    private String cpf;

    private String parentesco;

    // Relacionamento com Responsaveis (via tabela de junção)
    @OneToMany(mappedBy = "responsavel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AlunoResponsavel> responsaveis = new ArrayList<>();

    // Construtores
    public Responsavel() {}

    public Responsavel(String email, String senha, String nome, String cpf) {
        super(email, senha, Perfil.RESPONSAVEL);
        this.nome = nome;
        this.cpf = cpf;
    }
}