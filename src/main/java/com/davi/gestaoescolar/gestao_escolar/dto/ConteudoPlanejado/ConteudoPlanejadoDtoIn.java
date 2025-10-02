package com.davi.gestaoescolar.gestao_escolar.dto.ConteudoPlanejado;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO para entrada de dados de ConteudoPlanejado
 * 
 * @author Sistema de Gestão Escolar
 * @version 1.0
 */
@Getter
@Setter
public class ConteudoPlanejadoDtoIn {
    
    private String conteudo;
    private LocalDate dataPrevista;
    private LocalDate dataConclusao;
    private Boolean concluido;
    private String observacoes;
    private Integer ordemApresentacao;
    private Long planejamentoId;

    // Construtor padrão
    public ConteudoPlanejadoDtoIn() {}

    // Construtor com parâmetros
    public ConteudoPlanejadoDtoIn(String conteudo, LocalDate dataPrevista, LocalDate dataConclusao, 
                                 Boolean concluido, String observacoes, Integer ordemApresentacao, 
                                 Long planejamentoId) {
        this.conteudo = conteudo;
        this.dataPrevista = dataPrevista;
        this.dataConclusao = dataConclusao;
        this.concluido = concluido;
        this.observacoes = observacoes;
        this.ordemApresentacao = ordemApresentacao;
        this.planejamentoId = planejamentoId;
    }

}