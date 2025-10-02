package com.davi.gestaoescolar.gestao_escolar.dto.Nota;

import com.davi.gestaoescolar.gestao_escolar.model.enums.TipoAvaliacao;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class NotaDtoIn {
    
    private BigDecimal valor;
    private TipoAvaliacao tipo;
    private BigDecimal peso;
    private String observacao;
    private Long registroAulaId;
    private Long alunoId;
    
    // Construtor padrão
    public NotaDtoIn() {}
    
    // Construtor com parâmetros
    public NotaDtoIn(BigDecimal valor, TipoAvaliacao tipo, BigDecimal peso, 
                     String observacao, Long registroAulaId, Long alunoId) {
        this.valor = valor;
        this.tipo = tipo;
        this.peso = peso;
        this.observacao = observacao;
        this.registroAulaId = registroAulaId;
        this.alunoId = alunoId;
    }

}