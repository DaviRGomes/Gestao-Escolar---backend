package com.davi.gestaoescolar.gestao_escolar.dto.Presenca;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PresencaDtoIn {
    private Boolean presente;
    private String justificativa;
    private Long registroAulaId;
    private Long alunoId;
    
    // Construtor padrão
    public PresencaDtoIn() {}
    
    // Construtor com parâmetros
    public PresencaDtoIn(Boolean presente, String justificativa, Long registroAulaId, Long alunoId) {
        this.presente = presente;
        this.justificativa = justificativa;
        this.registroAulaId = registroAulaId;
        this.alunoId = alunoId;
    }
}