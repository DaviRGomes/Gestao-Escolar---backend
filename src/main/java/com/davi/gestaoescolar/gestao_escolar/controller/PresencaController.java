package com.davi.gestaoescolar.gestao_escolar.controller;

import com.davi.gestaoescolar.gestao_escolar.service.PresencaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.davi.gestaoescolar.gestao_escolar.dto.Presenca.PresencaDtoIn;
import com.davi.gestaoescolar.gestao_escolar.dto.Presenca.PresencaDtoOut;

@RestController
@RequestMapping("/api/presencas")
public class PresencaController {

    @Autowired
    private PresencaService presencaService;

    /**
     * Salvar nova presença
     */
    @PostMapping
    public ResponseEntity<PresencaDtoOut> salvar(@RequestBody PresencaDtoIn presenca) {
       PresencaDtoOut novaPresenca = presencaService.salvar(presenca);
       return ResponseEntity.ok(novaPresenca);
    }

    /**
     * Atualizar presença existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<PresencaDtoOut> atualizar(@PathVariable Long id, @RequestBody PresencaDtoIn presenca) {
        PresencaDtoOut presencaAtualizada = presencaService.atualizar(id, presenca);
        return ResponseEntity.ok(presencaAtualizada);
    }

    /**
     * Buscar presença por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<PresencaDtoOut>> buscarPorId(@PathVariable Long id) {
        Optional<PresencaDtoOut> presenca = presencaService.buscarPorId(id);
        return ResponseEntity.ok(presenca);
    }

    /**
     * Listar todas as presenças
     */
    @GetMapping
    public ResponseEntity<List<PresencaDtoOut>> listarTodas() {
        List<PresencaDtoOut> presencas = presencaService.listarTodas();
        return ResponseEntity.ok(presencas);
    }

    /**
     * Buscar presenças por aluno
     */
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<PresencaDtoOut>> buscarPorAluno(@PathVariable Long alunoId) {
        List<PresencaDtoOut> presencas = presencaService.buscarPorAluno(alunoId);
        return ResponseEntity.ok(presencas);
    }

    /**
     * Buscar presenças por registro de aula
     */
    @GetMapping("/aula/{registroAulaId}")
    public ResponseEntity<List<PresencaDtoOut>> buscarPorRegistroAula(@PathVariable Long registroAulaId) {
        List<PresencaDtoOut> presencas = presencaService.buscarPorRegistroAula(registroAulaId);
        return ResponseEntity.ok(presencas);
    }

    /**
     * Buscar presenças por status (presente/ausente)
     */
    @GetMapping("/status/{presente}")
    public ResponseEntity<List<PresencaDtoOut>> buscarPorStatus(@PathVariable Boolean presente) {
        List<PresencaDtoOut> presencas = presencaService.buscarPorStatus(presente);
        return ResponseEntity.ok(presencas);
    }

    /**
     * Buscar presenças de um aluno por status
     */
    @GetMapping("/aluno/{alunoId}/status/{presente}")
    public ResponseEntity<List<PresencaDtoOut>> buscarPorAlunoEStatus(@PathVariable Long alunoId, @PathVariable Boolean presente) {
        List<PresencaDtoOut> presencas = presencaService.buscarPorAlunoEStatus(alunoId, presente);
        return ResponseEntity.ok(presencas);
    }

    /**
     * Buscar presenças de uma aula por status
     */
    @GetMapping("/aula/{registroAulaId}/status/{presente}")
    public ResponseEntity<List<PresencaDtoOut>> buscarPorRegistroAulaEStatus(@PathVariable Long registroAulaId, @PathVariable Boolean presente) {
        List<PresencaDtoOut> presencas = presencaService.buscarPorRegistroAulaEStatus(registroAulaId, presente);
        return ResponseEntity.ok(presencas);
    }

    /**
     * Buscar presença específica de um aluno em uma aula
     */
    @GetMapping("/aluno/{alunoId}/aula/{registroAulaId}")
    public ResponseEntity<List<PresencaDtoOut>> buscarPorAlunoERegistroAula(@PathVariable Long alunoId, @PathVariable Long registroAulaId) {
        List<PresencaDtoOut> presencas = presencaService.buscarPorAlunoERegistroAula(alunoId, registroAulaId);
        return ResponseEntity.ok(presencas);
    }

    /**
     * Calcular frequência de um aluno
     */
    @GetMapping("/frequencia/aluno/{alunoId}")
    public ResponseEntity<BigDecimal> calcularFrequenciaAluno(@PathVariable Long alunoId) {
        BigDecimal frequencia = presencaService.calcularFrequenciaAluno(alunoId);
        return ResponseEntity.ok(frequencia);
    }

    /**
     * Contar presenças de um aluno
     */
    @GetMapping("/contador/presencas/aluno/{alunoId}")
    public ResponseEntity<Long> contarPresencasAluno(@PathVariable Long alunoId) {
        long presencas = presencaService.contarPresencasAluno(alunoId);
        return ResponseEntity.ok(presencas);
    }

    /**
     * Contar faltas de um aluno
     */
    @GetMapping("/contador/faltas/aluno/{alunoId}")
    public ResponseEntity<Long> contarFaltasAluno(@PathVariable Long alunoId) {
        long faltas = presencaService.contarFaltasAluno(alunoId);
        return ResponseEntity.ok(faltas);
    }

    /**
     * Contar total de aulas de um aluno
     */
    @GetMapping("/contador/aulas/aluno/{alunoId}")
    public ResponseEntity<Long> contarTotalAulasAluno(@PathVariable Long alunoId) {
        long totalAulas = presencaService.contarTotalAulasAluno(alunoId);
        return ResponseEntity.ok(totalAulas);
    }

    /**
     * Verificar se aluno tem frequência mínima
     */
    @GetMapping("/frequencia-minima/aluno/{alunoId}")
    public ResponseEntity<Boolean> verificarFrequenciaMinima(@PathVariable Long alunoId) {
        boolean temFrequenciaMinima = presencaService.verificarFrequenciaMinima(alunoId);
        return ResponseEntity.ok(temFrequenciaMinima);
    }

    /**
     * Registrar presença de um aluno (presente ou ausente)
     */
    @PostMapping("/aluno/{alunoId}/aula/{registroAulaId}")
    public ResponseEntity<PresencaDtoOut> registrarPresencaAluno(@PathVariable Long alunoId, @PathVariable Long registroAulaId, @RequestParam Boolean presente, @RequestParam(required = false) String justificativa) {
        PresencaDtoIn dto = new PresencaDtoIn(presente, justificativa, registroAulaId, alunoId);
        PresencaDtoOut presenca = presencaService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(presenca);
    }

    /**
     * Alterar status de presença
     */
    @PatchMapping("/alterar-status/{presencaId}")
    public ResponseEntity<PresencaDtoOut> alterarStatusPresenca(@PathVariable Long presencaId, @RequestParam Boolean novoStatus, @RequestParam(required = false) String justificativa) {
        PresencaDtoIn dto = new PresencaDtoIn(novoStatus, justificativa, null, null);
        PresencaDtoOut presenca = presencaService.atualizar(presencaId, dto);
        return ResponseEntity.ok(presenca);
    }

    /**
     * Buscar alunos com baixa frequência
     */
    @GetMapping("/baixa-frequencia")
    public ResponseEntity<List<Long>> buscarAlunosComBaixaFrequencia() {
        List<Long> alunosIds = presencaService.buscarAlunosComBaixaFrequencia();
        return ResponseEntity.ok(alunosIds);
    }

    /**
     * Gerar relatório de frequência de um aluno
     */
    @GetMapping("/relatorio/aluno/{alunoId}")
    public ResponseEntity<String> gerarRelatorioFrequencia(@PathVariable Long alunoId) {
        String relatorio = presencaService.gerarRelatorioFrequencia(alunoId);
        return ResponseEntity.ok(relatorio);
    }

    /**
     * Deletar presença
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        presencaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}