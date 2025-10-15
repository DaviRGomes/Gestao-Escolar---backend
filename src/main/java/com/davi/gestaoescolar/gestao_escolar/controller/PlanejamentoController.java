package com.davi.gestaoescolar.gestao_escolar.controller;

import com.davi.gestaoescolar.gestao_escolar.dto.Planejamento.PlanejamentoDtoIn;
import com.davi.gestaoescolar.gestao_escolar.dto.Planejamento.PlanejamentoDtoOut;
import com.davi.gestaoescolar.gestao_escolar.model.Planejamento;
import com.davi.gestaoescolar.gestao_escolar.service.DisciplinaService;
import com.davi.gestaoescolar.gestao_escolar.service.PlanejamentoService;
import com.davi.gestaoescolar.gestao_escolar.service.TurmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para gerenciar operações relacionadas aos Planejamentos
 * 
 * @author Sistema de Gestão Escolar
 * @version 1.0
 */
@RestController
@RequestMapping("/api/planejamentos")
public class PlanejamentoController {

    @Autowired
    private PlanejamentoService planejamentoService;
    

    /**
     * Criar um novo planejamento
     * 
     * @param dto Dados do planejamento a ser criado
     * @return ResponseEntity com o planejamento criado
     */
    @PostMapping
    public ResponseEntity<PlanejamentoDtoIn> criarPlanejamento(@RequestBody PlanejamentoDtoIn dto) {
        this.planejamentoService.salvar(dto);
        return ResponseEntity.ok().build();
    }

    /**
     * Atualizar um planejamento existente
     * 
     * @param id ID do planejamento a ser atualizado
     * @param dto Novos dados do planejamento
     * @return ResponseEntity com o planejamento atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlanejamentoDtoIn> atualizarPlanejamento(
            @PathVariable Long id,
            @RequestBody PlanejamentoDtoIn dto) {
        this.planejamentoService.atualizar( id, dto);
        return ResponseEntity.ok().build();
    }

    /**
     * Buscar planejamento por ID
     * 
     * @param id ID do planejamento
     * @return ResponseEntity com o planejamento encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<Planejamento> buscarPorId(@PathVariable Long id) {

        this.planejamentoService.buscarPorId(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Listar todos os planejamentos
     * 
     * @return ResponseEntity com a lista de planejamentos
     */
    @GetMapping
    public ResponseEntity<List<PlanejamentoDtoOut>> listarTodos() {

        this.planejamentoService.listarTodos();
        return ResponseEntity.ok().build();
    }

    /**
     * Buscar planejamentos por disciplina
     * 
     * @param disciplinaId ID da disciplina
     * @return ResponseEntity com a lista de planejamentos
     */
    @GetMapping("/disciplina/{disciplinaId}")
    public ResponseEntity<List<Planejamento>> buscarPorDisciplina(@PathVariable Long disciplinaId) {

        this.planejamentoService.buscarPorDisciplina(disciplinaId);
        return ResponseEntity.ok().build();
    }

    /**
     * Buscar planejamentos por turma
     * 
     * @param turmaId ID da turma
     * @return ResponseEntity com a lista de planejamentos
     */
    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<Planejamento>> buscarPorTurma(@PathVariable Long turmaId) {

        this.planejamentoService.buscarPorTurma(turmaId);
        return ResponseEntity.ok().build();
    }

    /**
     * Buscar planejamentos por ano
     * 
     * @param ano Ano letivo
     * @return ResponseEntity com a lista de planejamentos
     */
    @GetMapping("/ano/{ano}")
    public ResponseEntity<List<Planejamento>> buscarPorAno(@PathVariable Integer ano) {

        this.planejamentoService.buscarPorAno(ano);
        return ResponseEntity.ok().build();
    }

    /**
     * Buscar planejamentos por semestre
     * 
     * @param semestre Semestre ("1" ou "2")
     * @return ResponseEntity com a lista de planejamentos
     */
    @GetMapping("/semestre/{semestre}")
    public ResponseEntity<List<Planejamento>> buscarPorSemestre(@PathVariable String semestre) {

        this.planejamentoService.buscarPorSemestre(semestre);
        return ResponseEntity.ok().build();
    }

    /**
     * Buscar planejamentos por ano e semestre
     * 
     * @param ano Ano letivo
     * @param semestre Semestre ("1" ou "2")
     * @return ResponseEntity com a lista de planejamentos
     */
    @GetMapping("/ano/{ano}/semestre/{semestre}")
    public ResponseEntity<List<PlanejamentoDtoOut>> buscarPorAnoESemestre(@PathVariable Integer ano, @PathVariable String semestre) {

        this.planejamentoService.buscarPorAnoESemestre(ano, semestre);
        return ResponseEntity.ok().build();
    }

    /**
     * Buscar planejamento específico por disciplina, turma, semestre e ano
     * 
     * @param disciplinaId ID da disciplina
     * @param turmaId ID da turma
     * @param semestre Semestre ("1" ou "2")
     * @param ano Ano letivo
     * @return ResponseEntity com o planejamento encontrado
     */
    @GetMapping("/especifico")
    public ResponseEntity<PlanejamentoDtoOut> buscarEspecifico(
            @RequestParam Long disciplinaId,
            @RequestParam Long turmaId,
            @RequestParam String semestre,
            @RequestParam Integer ano) {

        this.planejamentoService.buscarEspecifico(disciplinaId,turmaId, semestre, ano);
        return ResponseEntity.ok().build();
    }

    /**
     * Deletar um planejamento
     * 
     * @param id ID do planejamento a ser deletado
     * @return ResponseEntity indicando o resultado da operação
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPlanejamento(@PathVariable Long id) {

        this.planejamentoService.deletar(id);
        return ResponseEntity.ok().build();
    }
}