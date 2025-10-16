package com.davi.gestaoescolar.gestao_escolar.controller;

import com.davi.gestaoescolar.gestao_escolar.model.Turma;
import com.davi.gestaoescolar.gestao_escolar.model.enums.Periodo;
import com.davi.gestaoescolar.gestao_escolar.service.TurmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/turmas")
public class TurmaController {

    @Autowired
    private TurmaService turmaService;

    /**
     * Criar nova turma
     */
    @PostMapping
    public ResponseEntity<Turma> criarTurma(@RequestBody Turma turma) {
        Turma novaTurma = turmaService.salvar(turma);
        return ResponseEntity.ok(novaTurma);
    }

    /**
     * Atualizar turma existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Turma> atualizarTurma(@PathVariable Long id, @RequestBody Turma turma) {
        Turma turmaAtualizada = turmaService.atualizar(id, turma);
        return ResponseEntity.ok(turmaAtualizada);
    }

    /**
     * Buscar turma por ID
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<Optional<Turma>> buscarPorId(@PathVariable Long id) {
        Optional<Turma> turma = turmaService.buscarPorId(id);
        return ResponseEntity.ok(turma);
    }

    /**
     * Buscar turmas por nome
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Turma>> buscarPorNome(@PathVariable String nome) {
        List<Turma> turmas = turmaService.buscarPorNome(nome);
        return ResponseEntity.ok(turmas);
    }

    /**
     * Buscar turmas por ano letivo
     */
    @GetMapping("/ano/{anoLetivo}")
    public ResponseEntity<List<Turma>> buscarPorAnoLetivo(@PathVariable String anoLetivo) {
        List<Turma> turmas = turmaService.buscarPorAnoLetivo(anoLetivo);
        return ResponseEntity.ok(turmas);
    }

    /**
     * Buscar turmas por semestre
     */
    @GetMapping("/semestre/{semestre}")
    public ResponseEntity<List<Turma>> buscarPorSemestre(@PathVariable String semestre) {
        List<Turma> turmas = turmaService.buscarPorSemestre(semestre);
        return ResponseEntity.ok(turmas);
    }

    /**
     * Buscar turmas por período
     */
    @GetMapping("/periodo/{periodo}")
    public ResponseEntity<List<Turma>> buscarPorPeriodo(@PathVariable Periodo periodo) {
        List<Turma> turmas = turmaService.buscarPorPeriodo(periodo);
        return ResponseEntity.ok(turmas);
    }

    /**
     * Buscar turmas por ano letivo e semestre
     */
    @GetMapping("/ano/{anoLetivo}/semestre/{semestre}")
    public ResponseEntity<List<Turma>> buscarPorAnoLetivoESemestre(
            @PathVariable String anoLetivo,
            @PathVariable String semestre) {
        List<Turma> turmas = turmaService.buscarPorAnoLetivoESemestre(anoLetivo, semestre);
        return ResponseEntity.ok(turmas);
    }

    /**
     * Listar todas as turmas
     */
    @GetMapping
    public ResponseEntity<List<Turma>> listarTodas() {
        List<Turma> turmas = turmaService.listarTodas();
        return ResponseEntity.ok(turmas);
    }

    /**
     * Listar apenas turmas ativas
     */
    @GetMapping("/ativas")
    public ResponseEntity<List<Turma>> listarAtivas() {
        List<Turma> turmas = turmaService.listarAtivas();
        return ResponseEntity.ok(turmas);
    }

    /**
     * Listar apenas turmas inativas
     */
    @GetMapping("/inativas")
    public ResponseEntity<List<Turma>> listarInativas() {
        List<Turma> turmas = turmaService.listarInativas();
        return ResponseEntity.ok(turmas);
    }

    /**
     * Desativar turma (soft delete)
     */
    @PatchMapping("/desativar/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        turmaService.desativar(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Reativar turma
     */
    @PatchMapping("/reativar/{id}")
    public ResponseEntity<Void> reativar(@PathVariable Long id) {
        turmaService.reativar(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Deletar turma permanentemente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        turmaService.deletar(id);
        return ResponseEntity.ok().build();
    }
}