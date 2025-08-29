package com.davi.gestaoescolar.gestao_escolar.model;

import com.davi.gestaoescolar.gestao_escolar.model.enums.Perfil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Perfil perfil;

    private LocalDateTime ultimoAcesso;
    

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(length = 200)
    private String endereco;


    private Boolean ativo = true;

    // Construtores
    public Usuario() {}

    public Usuario(String email, String senha, Perfil perfil) {
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
        this.ativo = true;
    }

}