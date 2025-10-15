package com.davi.gestaoescolar.gestao_escolar.dto.ConteudoPlanejado;

import com.davi.gestaoescolar.gestao_escolar.dto.Planejamento.PlanejamentoDtoOut;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO para saída de dados de ConteudoPlanejado
 * 
 * @author Sistema de Gestão Escolar
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
public class ConteudoPlanejadoDtoOut {
    
    private Long id;
    private String conteudo;
    private LocalDate dataPrevista;
    private LocalDate dataConclusao;
    private Boolean concluido;
    private String observacoes;
    private Integer ordemApresentacao;
    private PlanejamentoDtoOut planejamento;

    public ConteudoPlanejadoDtoOut(Long id, String conteudo, LocalDate dataPrevista) {
        this.id = id;
        this.conteudo = conteudo;
        this.dataPrevista = dataPrevista;
    }

    // Construtor padrão
    public ConteudoPlanejadoDtoOut() {}



}