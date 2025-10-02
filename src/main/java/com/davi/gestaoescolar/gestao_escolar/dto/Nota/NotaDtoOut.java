package com.davi.gestaoescolar.gestao_escolar.dto.Nota;

import com.davi.gestaoescolar.gestao_escolar.dto.Aluno.AlunoDtoSimples;
import com.davi.gestaoescolar.gestao_escolar.dto.RegistroAula.RegistroAulaDtoSimples;
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
    private RegistroAulaDtoSimples registroAula;
    private AlunoDtoSimples aluno;
    
    // Construtor padrão
    public NotaDtoOut() {}
    
}