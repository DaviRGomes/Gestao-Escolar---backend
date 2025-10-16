package com.davi.gestaoescolar.gestao_escolar.dto.RegistroAula;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

import com.davi.gestaoescolar.gestao_escolar.dto.Disciplina.DisciplinaDtoOut;
import com.davi.gestaoescolar.gestao_escolar.dto.Turma.TurmaDtoOut;
import com.davi.gestaoescolar.gestao_escolar.dto.ConteudoPlanejado.ConteudoPlanejadoDtoOut;
import com.fasterxml.jackson.annotation.JsonInclude;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistroAulaDtoOut {
    
    private Long id;
    private LocalDate data;
    private String descricao;
    private String observacoes;
    
    // Referências de classe solicitadas
    private DisciplinaDtoOut disciplina;
    private TurmaDtoOut turma;
    private ConteudoPlanejadoDtoOut conteudoPlanejado;
    
    // Construtor com parâmetros
    // Construtor com parâmetros mínimos (compatível com usos atuais)
    public RegistroAulaDtoOut(Long id, LocalDate data, String descricao) {
        this.id = id;
        this.data = data;
        this.descricao = descricao;
    }

    public RegistroAulaDtoOut() {
    }

    
}