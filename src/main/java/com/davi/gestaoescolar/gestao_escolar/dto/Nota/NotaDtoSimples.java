package com.davi.gestaoescolar.gestao_escolar.dto.Nota;

import com.davi.gestaoescolar.gestao_escolar.dto.Aluno.AlunoDtoSimples;
import com.davi.gestaoescolar.gestao_escolar.model.enums.TipoAvaliacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class NotaDtoSimples {
    private Long id;
    private BigDecimal valor;
    private TipoAvaliacao tipo;
    private AlunoDtoSimples aluno;
    
    // Construtor padrão
    public NotaDtoSimples() {}

}