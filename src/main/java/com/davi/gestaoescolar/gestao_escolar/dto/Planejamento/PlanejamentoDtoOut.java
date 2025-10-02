package com.davi.gestaoescolar.gestao_escolar.dto.Planejamento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PlanejamentoDtoOut {
    private Long id;
    private String descricao;
    private String semestre;
    private Integer ano;
    private DisciplinaDTO disciplina;
    private TurmaDTO turma;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class DisciplinaDTO {
        private Long id;
        private String nome;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class TurmaDTO {
        private Long id;
        private String nome;
    }
}