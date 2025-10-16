package com.davi.gestaoescolar.gestao_escolar.dto.RegistroAula;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class RegistroAulaDtoIn {
    private LocalDate data;
    private LocalTime horaInicio;
    private String descricao;
    private LocalTime horaFim;
    private String conteudoMinistrado;
    private String observacoes;
    private Long disciplinaId;
    private Long turmaId;
    private Long professorId;
    private Long conteudoPlanejadoId;
    
    // Construtor padrão
    public RegistroAulaDtoIn() {}
    
    // Construtor com parâmetros essenciais
    public RegistroAulaDtoIn(LocalDate data, LocalTime horaInicio, LocalTime horaFim,
                            String conteudoMinistrado, Long disciplinaId, Long turmaId, Long professorId) {
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.conteudoMinistrado = conteudoMinistrado;
        this.disciplinaId = disciplinaId;
        this.turmaId = turmaId;
        this.professorId = professorId;
    }
}