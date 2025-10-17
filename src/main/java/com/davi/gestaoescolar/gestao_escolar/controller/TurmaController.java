package com.davi.gestaoescolar.gestao_escolar.controller;

import com.davi.gestaoescolar.gestao_escolar.model.enums.Periodo;
import com.davi.gestaoescolar.gestao_escolar.service.TurmaService;
import com.davi.gestaoescolar.gestao_escolar.dto.Turma.TurmaDtoIn;
import com.davi.gestaoescolar.gestao_escolar.dto.Turma.TurmaDtoOut;
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
    public ResponseEntity<TurmaDtoOut> criarTurma(@RequestBody TurmaDtoIn turmaCreate) {
        TurmaDtoOut novaTurma = turmaService.salvar(turmaCreate);
        return ResponseEntity.ok(novaTurma);
    }

    /**
     * Atualizar turma existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<TurmaDtoOut> atualizarTurma(@PathVariable Long id, @RequestBody TurmaDtoIn turmaCreate) {
        TurmaDtoOut turmaAtualizada = turmaService.atualizar(id, turmaCreate);
        return ResponseEntity.ok(turmaAtualizada);
    }

    /**
     * Buscar turma por ID
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<Optional<TurmaDtoOut>> buscarPorId(@PathVariable Long id) {
        Optional<TurmaDtoOut> turma = turmaService.buscarPorId(id);
        return ResponseEntity.ok(turma);
    }

    /**
     * Buscar turmas por nome
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<TurmaDtoOut>> buscarPorNome(@PathVariable String nome) {
        List<TurmaDtoOut> turmas = turmaService.buscarPorNome(nome);
        return ResponseEntity.ok(turmas);
    }

    /**
     * Buscar turmas por ano letivo
     */
    @GetMapping("/ano/{anoLetivo}")
    public ResponseEntity<List<TurmaDtoOut>> buscarPorAnoLetivo(@PathVariable String anoLetivo) {
        List<TurmaDtoOut> turmas = turmaService.buscarPorAnoLetivo(anoLetivo);
        return ResponseEntity.ok(turmas);
    }

    /**
     * Buscar turmas por semestre
     */
    @GetMapping("/semestre/{semestre}")
    public ResponseEntity<List<TurmaDtoOut>> buscarPorSemestre(@PathVariable String semestre) {
        List<TurmaDtoOut> turmas = turmaService.buscarPorSemestre(semestre);
        return ResponseEntity.ok(turmas);
    }

    /**
     * Buscar turmas por período
     */
    @GetMapping("/periodo/{periodo}")
    public ResponseEntity<List<TurmaDtoOut>> buscarPorPeriodo(@PathVariable Periodo periodo) {
        List<TurmaDtoOut> turmas = turmaService.buscarPorPeriodo(periodo);
        return ResponseEntity.ok(turmas);
    }

    /**
     * Buscar turmas por ano letivo e semestre
     */
    @GetMapping("/ano/{anoLetivo}/semestre/{semestre}")
    public ResponseEntity<List<TurmaDtoOut>> buscarPorAnoLetivoESemestre(
            @PathVariable String anoLetivo,
            @PathVariable String semestre) {
        List<TurmaDtoOut> turmas = turmaService.buscarPorAnoLetivoESemestre(anoLetivo, semestre);
        return ResponseEntity.ok(turmas);
    }

    /**
     * Listar todas as turmas
     */
    @GetMapping
    public ResponseEntity<List<TurmaDtoOut>> listarTodas() {
        List<TurmaDtoOut> turmas = turmaService.listarTodas();
        return ResponseEntity.ok(turmas);
    }

    /**
     * Listar apenas turmas ativas
     */
    @GetMapping("/ativas")
    public ResponseEntity<List<TurmaDtoOut>> listarAtivas() {
        List<TurmaDtoOut> turmas = turmaService.listarAtivas();
        return ResponseEntity.ok(turmas);
    }

    /**
     * Listar apenas turmas inativas
     */
    @GetMapping("/inativas")
    public ResponseEntity<List<TurmaDtoOut>> listarInativas() {
        List<TurmaDtoOut> turmas = turmaService.listarInativas();
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