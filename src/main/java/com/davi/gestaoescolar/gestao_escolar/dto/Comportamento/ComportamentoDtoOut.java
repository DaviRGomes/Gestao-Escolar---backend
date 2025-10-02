package com.davi.gestaoescolar.gestao_escolar.dto.Comportamento;

import com.davi.gestaoescolar.gestao_escolar.dto.Aluno.AlunoDtoSimples;
import com.davi.gestaoescolar.gestao_escolar.dto.Professor.ProfessorDtoSimples;
import com.davi.gestaoescolar.gestao_escolar.model.enums.Gravidade;
import com.davi.gestaoescolar.gestao_escolar.model.enums.TipoComportamento;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ComportamentoDtoOut {
    
    private Long id;
    private String descricao;
    private LocalDate date;
    private TipoComportamento tipo;
    private Gravidade nivel;
    private ProfessorDtoSimples professor;
    private AlunoDtoSimples aluno;
    
    // Construtor padrão
    public ComportamentoDtoOut() {}
    
}