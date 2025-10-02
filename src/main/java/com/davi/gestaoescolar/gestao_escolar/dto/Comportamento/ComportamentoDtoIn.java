package com.davi.gestaoescolar.gestao_escolar.dto.Comportamento;

import com.davi.gestaoescolar.gestao_escolar.model.enums.Gravidade;
import com.davi.gestaoescolar.gestao_escolar.model.enums.TipoComportamento;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ComportamentoDtoIn {
    
    private String descricao;
    private LocalDate date;
    private TipoComportamento tipo;
    private Gravidade nivel;
    private Long professorId;
    private Long alunoId;
    
    // Construtor padrão
    public ComportamentoDtoIn() {}
    
    // Construtor com parâmetros
    public ComportamentoDtoIn(String descricao, LocalDate date, TipoComportamento tipo, 
                             Gravidade nivel, Long professorId, Long alunoId) {
        this.descricao = descricao;
        this.date = date;
        this.tipo = tipo;
        this.nivel = nivel;
        this.professorId = professorId;
        this.alunoId = alunoId;
    }

}