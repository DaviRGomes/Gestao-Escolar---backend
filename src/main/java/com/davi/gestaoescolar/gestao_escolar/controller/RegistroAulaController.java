package com.davi.gestaoescolar.gestao_escolar.controller;

import com.davi.gestaoescolar.gestao_escolar.dto.RegistroAula.RegistroAulaDtoIn;
import com.davi.gestaoescolar.gestao_escolar.dto.RegistroAula.RegistroAulaDtoOut;
import com.davi.gestaoescolar.gestao_escolar.service.RegistroAulaService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/registros-aula")
public class RegistroAulaController {

    @Autowired
    private RegistroAulaService registroAulaService;

    /**
     * Salvar novo registro de aula
     */
    @PostMapping
    public ResponseEntity<RegistroAulaDtoOut> salvar(@RequestBody RegistroAulaDtoIn registroAula) {
        RegistroAulaDtoOut novoRegistro = registroAulaService.salvar(registroAula);
        return ResponseEntity.ok(novoRegistro);
    }

    /**
     * Atualizar registro de aula existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<RegistroAulaDtoOut> atualizar(@PathVariable Long id, @RequestBody RegistroAulaDtoIn registroAula) {
        RegistroAulaDtoOut registroAtualizado = registroAulaService.atualizar(id, registroAula);
        return ResponseEntity.ok(registroAtualizado);
    }

    /**
     * Buscar registro de aula por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<RegistroAulaDtoOut>> buscarPorId(@PathVariable Long id) {
        Optional<RegistroAulaDtoOut> registro = registroAulaService.buscarPorId(id);
        return ResponseEntity.ok(registro);
    }

    /**
     * Listar todos os registros de aula
     */
    @GetMapping
    public ResponseEntity<List<RegistroAulaDtoOut>> listarTodos() {
        List<RegistroAulaDtoOut> registros = registroAulaService.listarTodos();
        return ResponseEntity.ok(registros);
    }

    /**
     * Buscar registros de aula por data
     */
    @GetMapping("/data/{data}")
    public ResponseEntity<List<RegistroAulaDtoOut>> buscarPorData(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<RegistroAulaDtoOut> registros = registroAulaService.buscarPorData(data);
        return ResponseEntity.ok(registros);
    }

    /**
     * Buscar registros de aula por período
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<RegistroAulaDtoOut>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        List<RegistroAulaDtoOut> registros = registroAulaService.buscarPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(registros);
    }

    /**
     * Buscar registros de aula por turma
     */
    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<RegistroAulaDtoOut>> buscarPorTurma(@PathVariable Long turmaId) {
        List<RegistroAulaDtoOut> registros = registroAulaService.buscarPorTurma(turmaId);
        return ResponseEntity.ok(registros);
    }

    /**
     * Buscar registros de aula por disciplina
     */
    @GetMapping("/disciplina/{disciplinaId}")
    public ResponseEntity<List<RegistroAulaDtoOut>> buscarPorDisciplina(@PathVariable Long disciplinaId) {
        List<RegistroAulaDtoOut> registros = registroAulaService.buscarPorDisciplina(disciplinaId);
        return ResponseEntity.ok(registros);
    }

    /**
     * Buscar registros de aula por turma e disciplina
     */
    @GetMapping("/turma/{turmaId}/disciplina/{disciplinaId}")
    public ResponseEntity<List<RegistroAulaDtoOut>> buscarPorTurmaEDisciplina(
            @PathVariable Long turmaId, @PathVariable Long disciplinaId) {
        List<RegistroAulaDtoOut> registros = registroAulaService.buscarPorTurmaEDisciplina(turmaId, disciplinaId);
        return ResponseEntity.ok(registros);
    }

    /**
     * Buscar registros de aula por conteúdo planejado
     */
    @GetMapping("/conteudo-planejado/{conteudoPlanejadoId}")
    public ResponseEntity<List<RegistroAulaDtoOut>> buscarPorConteudoPlanejado(@PathVariable Long conteudoPlanejadoId) {
        List<RegistroAulaDtoOut> registros = registroAulaService.buscarPorConteudoPlanejado(conteudoPlanejadoId);
        return ResponseEntity.ok(registros);
    }

    /**
     * Buscar registros de aula por turma e período
     */
    @GetMapping("/turma/{turmaId}/periodo")
    public ResponseEntity<List<RegistroAulaDtoOut>> buscarPorTurmaEPeriodo(
            @PathVariable Long turmaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        List<RegistroAulaDtoOut> registros = registroAulaService.buscarPorTurmaEPeriodo(turmaId, dataInicio, dataFim);
        return ResponseEntity.ok(registros);
    }

    /**
     * Buscar registros de aula por disciplina e período
     */
    @GetMapping("/disciplina/{disciplinaId}/periodo")
    public ResponseEntity<List<RegistroAulaDtoOut>> buscarPorDisciplinaEPeriodo(
            @PathVariable Long disciplinaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        List<RegistroAulaDtoOut> registros = registroAulaService.buscarPorDisciplinaEPeriodo(disciplinaId, dataInicio, dataFim);
        return ResponseEntity.ok(registros);
    }

    /**
     * Buscar aulas de hoje
     */
    @GetMapping("/hoje")
    public ResponseEntity<List<RegistroAulaDtoOut>> buscarAulasDeHoje() {
        List<RegistroAulaDtoOut> registros = registroAulaService.buscarAulasDeHoje();
        return ResponseEntity.ok(registros);
    }

    /**
     * Buscar aulas da semana atual
     */
    @GetMapping("/semana")
    public ResponseEntity<List<RegistroAulaDtoOut>> buscarAulasDaSemana() {
        List<RegistroAulaDtoOut> registros = registroAulaService.buscarAulasDaSemana();
        return ResponseEntity.ok(registros);
    }

    /**
     * Buscar aulas do mês atual
     */
    @GetMapping("/mes")
    public ResponseEntity<List<RegistroAulaDtoOut>> buscarAulasDoMes() {
        List<RegistroAulaDtoOut> registros = registroAulaService.buscarAulasDoMes();
        return ResponseEntity.ok(registros);
    }

}