package com.davi.gestaoescolar.gestao_escolar.dto.Nota;

import com.davi.gestaoescolar.gestao_escolar.dto.Aluno.AlunoDtoOut;
import com.davi.gestaoescolar.gestao_escolar.dto.RegistroAula.RegistroAulaDtoOut;
import com.davi.gestaoescolar.gestao_escolar.model.enums.TipoAvaliacao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class NotaDtoOut {
    
    private Long id;
    private BigDecimal valor;
    private TipoAvaliacao tipo;
    private BigDecimal peso;
    private String observacao;
    private RegistroAulaDtoOut registroAula;
    private AlunoDtoOut aluno;
    
    
    // Construtor padrão
    public NotaDtoOut() {}


    public NotaDtoOut(Long id, BigDecimal valor, TipoAvaliacao tipo, AlunoDtoOut aluno) {
        this.id = id;
        this.valor = valor;
        this.tipo = tipo;
        this.aluno = aluno;
    }
    
}