package com.davi.gestaoescolar.gestao_escolar.dto.Aluno;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AlunoDtoIn {
    
    public AlunoDtoIn() {}
    private String nome;
    private LocalDate dataNascimento;
    private String cpf;
    private String observacoes;
    private Boolean ativo;
    private List<ResponsavelDTO> responsaveis;
    private LocalDateTime dataCriacao;


    @Getter
    @Setter
    @AllArgsConstructor
    public static class ResponsavelDTO {
        
        public ResponsavelDTO() {}
        private Long id;
        private String nome;
        private String telefone;
        private String cpf;
        private String parentesco;
        private Boolean principal;
    }
}