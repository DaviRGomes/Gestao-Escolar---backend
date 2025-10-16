package com.davi.gestaoescolar.gestao_escolar.controller;

import com.davi.gestaoescolar.gestao_escolar.model.RegistroAula;
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
    public ResponseEntity<RegistroAula> salvar(@RequestBody RegistroAula registroAula) {
        RegistroAula novoRegistro = registroAulaService.salvar(registroAula);
        return ResponseEntity.ok(novoRegistro);
    }

    /**
     * Atualizar registro de aula existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<RegistroAula> atualizar(@PathVariable Long id, @RequestBody RegistroAula registroAula) {
        RegistroAula registroAtualizado = registroAulaService.atualizar(id, registroAula);
        return ResponseEntity.ok(registroAtualizado);
    }

    /**
     * Buscar registro de aula por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<RegistroAula>> buscarPorId(@PathVariable Long id) {
        Optional<RegistroAula> registro = registroAulaService.buscarPorId(id);
        return ResponseEntity.ok(registro);
    }

    /**
     * Listar todos os registros de aula
     */
    @GetMapping
    public ResponseEntity<List<RegistroAula>> listarTodos() {
        List<RegistroAula> registros = registroAulaService.listarTodos();
        return ResponseEntity.ok(registros);
    }

    /**
     * Buscar registros de aula por data
     */
    @GetMapping("/data/{data}")
    public ResponseEntity<List<RegistroAula>> buscarPorData(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<RegistroAula> registros = registroAulaService.buscarPorData(data);
        return ResponseEntity.ok(registros);
    }

    /**
     * Buscar registros de aula por período
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<RegistroAula>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        List<RegistroAula> registros = registroAulaService.buscarPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(registros);
    }

    /**
     * Buscar registros de aula por turma
     */
    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<RegistroAula>> buscarPorTurma(@PathVariable Long turmaId) {
        List<RegistroAula> registros = registroAulaService.buscarPorTurma(turmaId);
        return ResponseEntity.ok(registros);
    }

    /**
     * Buscar registros de aula por disciplina
     */
    @GetMapping("/disciplina/{disciplinaId}")
    public ResponseEntity<List<RegistroAula>> buscarPorDisciplina(@PathVariable Long disciplinaId) {
        List<RegistroAula> registros = registroAulaService.buscarPorDisciplina(disciplinaId);
        return ResponseEntity.ok(registros);
    }

    /**
     * Buscar registros de aula por turma e disciplina
     */
    @GetMapping("/turma/{turmaId}/disciplina/{disciplinaId}")
    public ResponseEntity<List<RegistroAula>> buscarPorTurmaEDisciplina(
            @PathVariable Long turmaId, @PathVariable Long disciplinaId) {
        List<RegistroAula> registros = registroAulaService.buscarPorTurmaEDisciplina(turmaId, disciplinaId);
        return ResponseEntity.ok(registros);
    }

    /**
     * Buscar registros de aula por conteúdo planejado
     */
    @GetMapping("/conteudo-planejado/{conteudoPlanejadoId}")
    public ResponseEntity<List<RegistroAula>> buscarPorConteudoPlanejado(@PathVariable Long conteudoPlanejadoId) {
        List<RegistroAula> registros = registroAulaService.buscarPorConteudoPlanejado(conteudoPlanejadoId);
        return ResponseEntity.ok(registros);
    }

    /**
     * Buscar registros de aula por turma e período
     */
    @GetMapping("/turma/{turmaId}/periodo")
    public ResponseEntity<List<RegistroAula>> buscarPorTurmaEPeriodo(
            @PathVariable Long turmaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        List<RegistroAula> registros = registroAulaService.buscarPorTurmaEPeriodo(turmaId, dataInicio, dataFim);
        return ResponseEntity.ok(registros);
    }

    /**
     * Buscar registros de aula por disciplina e período
     */
    @GetMapping("/disciplina/{disciplinaId}/periodo")
    public ResponseEntity<List<RegistroAula>> buscarPorDisciplinaEPeriodo(
            @PathVariable Long disciplinaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        List<RegistroAula> registros = registroAulaService.buscarPorDisciplinaEPeriodo(disciplinaId, dataInicio, dataFim);
        return ResponseEntity.ok(registros);
    }

    /**
     * Buscar aulas de hoje
     */
    @GetMapping("/hoje")
    public ResponseEntity<List<RegistroAula>> buscarAulasDeHoje() {
        List<RegistroAula> registros = registroAulaService.buscarAulasDeHoje();
        return ResponseEntity.ok(registros);
    }

    /**
     * Buscar aulas da semana atual
     */
    @GetMapping("/semana")
    public ResponseEntity<List<RegistroAula>> buscarAulasDaSemana() {
        List<RegistroAula> registros = registroAulaService.buscarAulasDaSemana();
        return ResponseEntity.ok(registros);
    }

    /**
     * Buscar aulas do mês atual
     */
    @GetMapping("/mes")
    public ResponseEntity<List<RegistroAula>> buscarAulasDoMes() {
        List<RegistroAula> registros = registroAulaService.buscarAulasDoMes();
        return ResponseEntity.ok(registros);
    }

    /**
     * Criar registro de aula básico
     */
    @PostMapping("/basico")
    public ResponseEntity<RegistroAula> criarRegistroBasico(
            @RequestParam Long turmaId,
            @RequestParam Long disciplinaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam String descricao) {
        RegistroAula registro = registroAulaService.criarRegistroBasico(turmaId, disciplinaId, data, descricao);
        return ResponseEntity.ok(registro);
    }
}