package com.davi.gestaoescolar.gestao_escolar.model;

import com.davi.gestaoescolar.gestao_escolar.model.enums.Perfil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Secretaria extends Usuario {

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(unique = true, length = 14)
    private String cpf;

    @Column(length = 15)
    private String telefone;

    @Column(name = "data_contratacao")
    private LocalDate dataContratacao;

    @Column(length = 50)
    private String cargo;

    @Column(length = 20)
    private String matricula;

    // Construtores
    public Secretaria() {
        super();
        this.setPerfil(Perfil.SECRETARIA);
    }

    public Secretaria(String email, String senha, String nome) {
        super(email, senha, Perfil.SECRETARIA);
        this.nome = nome;
        this.dataContratacao = LocalDate.now();
    }

    public Secretaria(String email, String senha, String nome, String cpf, String telefone) {
        super(email, senha, Perfil.SECRETARIA);
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.dataContratacao = LocalDate.now();
    }
}