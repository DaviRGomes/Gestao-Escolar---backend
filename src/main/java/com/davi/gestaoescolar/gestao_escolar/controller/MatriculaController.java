package com.davi.gestaoescolar.gestao_escolar.controller;

import com.davi.gestaoescolar.gestao_escolar.dto.Matricula.MatriculaDtoIn;
import com.davi.gestaoescolar.gestao_escolar.dto.Matricula.MatriculaDtoOut;
import com.davi.gestaoescolar.gestao_escolar.model.enums.SituacaoMatricula;
import com.davi.gestaoescolar.gestao_escolar.service.MatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/matriculas")
@CrossOrigin(origins = "*")
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;

    // Listar todas as matrículas
    @GetMapping
    public ResponseEntity<List<MatriculaDtoOut>> listarTodas() {
        List<MatriculaDtoOut> matriculas = matriculaService.listarTodas();
        return ResponseEntity.ok(matriculas);
    }

    // Buscar matrícula por ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<MatriculaDtoOut>> buscarPorId(@PathVariable Long id) {
        Optional<MatriculaDtoOut> matricula = matriculaService.buscarPorId(id);
        return ResponseEntity.ok(matricula);
    }

    // Criar nova matrícula
    @PostMapping
    public ResponseEntity<MatriculaDtoOut> criarMatricula(@Valid @RequestBody MatriculaDtoIn matriculaDtoIn) {
        MatriculaDtoOut novaMatricula = matriculaService.salvarMatricula(matriculaDtoIn);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaMatricula);
    }

    // Atualizar matrícula
    @PutMapping("/{id}")
    public ResponseEntity<MatriculaDtoOut> atualizarMatricula(@PathVariable Long id, @Valid @RequestBody MatriculaDtoIn matriculaDtoIn) {
        MatriculaDtoOut matriculaAtualizada = matriculaService.atualizarMatricula(id, matriculaDtoIn);
        return ResponseEntity.ok(matriculaAtualizada);
    }

    // Deletar matrícula
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMatricula(@PathVariable Long id) {
        matriculaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar matrículas por aluno
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<MatriculaDtoOut>> buscarPorAluno(@PathVariable Long alunoId) {
        List<MatriculaDtoOut> matriculas = matriculaService.buscarPorAluno(alunoId);
        return ResponseEntity.ok(matriculas);
    }

    // Buscar matrículas por turma
    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<MatriculaDtoOut>> buscarPorTurma(@PathVariable Long turmaId) {
        List<MatriculaDtoOut> matriculas = matriculaService.buscarPorTurma(turmaId);
        return ResponseEntity.ok(matriculas);
    }

    // Buscar matrículas por situação
    @GetMapping("/situacao/{situacao}")
    public ResponseEntity<List<MatriculaDtoOut>> buscarPorSituacao(@PathVariable SituacaoMatricula situacao) {
        List<MatriculaDtoOut> matriculas = matriculaService.buscarPorSituacao(situacao);
        return ResponseEntity.ok(matriculas);
    }

    // Buscar matrículas por data específica
    @GetMapping("/data/{data}")
    public ResponseEntity<List<MatriculaDtoOut>> buscarPorData(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<MatriculaDtoOut> matriculas = matriculaService.buscarPorData(data);
        return ResponseEntity.ok(matriculas);
    }

    // Buscar matrículas por período
    @GetMapping("/periodo")
    public ResponseEntity<List<MatriculaDtoOut>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        List<MatriculaDtoOut> matriculas = matriculaService.buscarPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(matriculas);
    }

    // Buscar matrículas ativas por turma
    @GetMapping("/turma/{turmaId}/ativas")
    public ResponseEntity<List<MatriculaDtoOut>> buscarAtivasPorTurma(@PathVariable Long turmaId) {
        List<MatriculaDtoOut> matriculas = matriculaService.buscarAtivasPorTurma(turmaId);
        return ResponseEntity.ok(matriculas);
    }

    // Ativar matrícula
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<MatriculaDtoOut> ativarMatricula(@PathVariable Long id) {
        matriculaService.ativar(id);
        return ResponseEntity.ok().build();
    }

    // Desativar matrícula
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativarMatricula(@PathVariable Long id) {
        matriculaService.desativar(id);
        return ResponseEntity.ok().build();
    }

    // Colocar matrícula em processo
    @PatchMapping("/{id}/processo")
    public ResponseEntity<MatriculaDtoOut> colocarEmProcesso(@PathVariable Long id) {
        matriculaService.colocarEmProcesso(id);
        return ResponseEntity.ok().build();
    }

    // Finalizar matrícula
    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<Void> finalizarMatricula(@PathVariable Long id) {
        matriculaService.finalizar(id);
        return ResponseEntity.ok().build();
    }

    // Transferir aluno para outra turma
    @PatchMapping("/{id}/transferir/{novaTurmaId}")
    public ResponseEntity<MatriculaDtoOut> transferirTurma(@PathVariable Long id, @PathVariable Long novaTurmaId) {
        MatriculaDtoOut matriculaTransferida = matriculaService.transferirTurma(id, novaTurmaId);
        return ResponseEntity.ok(matriculaTransferida);
    }
}