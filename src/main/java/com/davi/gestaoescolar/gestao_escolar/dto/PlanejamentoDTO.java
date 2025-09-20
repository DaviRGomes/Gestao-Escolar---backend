package com.davi.gestaoescolar.gestao_escolar.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanejamentoDTO {
    private String descricao;
    private String semestre;
    private Integer ano;
    private DisciplinaDTO disciplina;
    private TurmaDTO turma;

    @Getter
    @Setter
    public static class DisciplinaDTO {
        private Long id;
        private String nome;
    }

    @Getter
    @Setter
    public static class TurmaDTO {
        private Long id;
        private String nome;
    }
}