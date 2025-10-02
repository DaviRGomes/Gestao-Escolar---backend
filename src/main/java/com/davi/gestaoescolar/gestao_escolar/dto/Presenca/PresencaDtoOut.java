package com.davi.gestaoescolar.gestao_escolar.dto.Presenca;

import com.davi.gestaoescolar.gestao_escolar.dto.Aluno.AlunoDtoOut;
import com.davi.gestaoescolar.gestao_escolar.dto.RegistroAula.RegistroAulaDtoOut;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PresencaDtoOut {
    private Long id;
    private Boolean presente;
    private String justificativa;
    private RegistroAulaDtoOut registroAula;
    private AlunoDtoOut aluno;
    
    // Construtor padrão
    public PresencaDtoOut() {}
    
    // Construtor com parâmetros
    public PresencaDtoOut(Long id, Boolean presente, String justificativa, 
                         RegistroAulaDtoOut registroAula, AlunoDtoOut aluno) {
        this.id = id;
        this.presente = presente;
        this.justificativa = justificativa;
        this.registroAula = registroAula;
        this.aluno = aluno;
    }
}