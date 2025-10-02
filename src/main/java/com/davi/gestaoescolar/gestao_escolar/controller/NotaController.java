package com.davi.gestaoescolar.gestao_escolar.controller;

import com.davi.gestaoescolar.gestao_escolar.dto.Nota.NotaDtoIn;
import com.davi.gestaoescolar.gestao_escolar.dto.Nota.NotaDtoOut;
import com.davi.gestaoescolar.gestao_escolar.model.enums.TipoAvaliacao;
import com.davi.gestaoescolar.gestao_escolar.service.NotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notas")
public class NotaController {

    @Autowired
    private NotaService notaService;

    /**
     * Cria uma nova nota
     */
    @PostMapping
    public ResponseEntity<NotaDtoOut> criarNota(@RequestBody NotaDtoIn notaDto) {
        NotaDtoOut notaCriada = notaService.salvar(notaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(notaCriada);
    }

    /**
     * Atualiza uma nota existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<NotaDtoOut> atualizarNota(
            @PathVariable Long id, 
            @RequestBody NotaDtoIn notaDto) {
        NotaDtoOut notaAtualizada = notaService.atualizar(id, notaDto);
        return ResponseEntity.ok(notaAtualizada);
    }

    /**
     * Busca nota por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotaDtoOut> buscarPorId(@PathVariable Long id) {
        NotaDtoOut nota = notaService.buscarPorId(id);
        return ResponseEntity.ok(nota);
    }

    /**
     * Lista todas as notas
     */
    @GetMapping
    public ResponseEntity<List<NotaDtoOut>> listarTodas() {
        List<NotaDtoOut> notas = notaService.listarTodas();
        return ResponseEntity.ok(notas);
    }

    /**
     * Busca notas por aluno
     */
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<NotaDtoOut>> buscarPorAluno(@PathVariable Long alunoId) {
        List<NotaDtoOut> notas = notaService.buscarPorAluno(alunoId);
        return ResponseEntity.ok(notas);
    }

    /**
     * Busca notas por registro de aula
     */
    @GetMapping("/registro-aula/{registroAulaId}")
    public ResponseEntity<List<NotaDtoOut>> buscarPorRegistroAula(@PathVariable Long registroAulaId) {
        List<NotaDtoOut> notas = notaService.buscarPorRegistroAula(registroAulaId);
        return ResponseEntity.ok(notas);
    }

    /**
     * Busca notas por tipo de avaliação
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<NotaDtoOut>> buscarPorTipo(@PathVariable TipoAvaliacao tipo) {
        List<NotaDtoOut> notas = notaService.buscarPorTipo(tipo);
        return ResponseEntity.ok(notas);
    }

    /**
     * Deleta uma nota
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarNota(@PathVariable Long id) {
        notaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Calcula média do aluno
     */
    @GetMapping("/media/aluno/{alunoId}")
    public ResponseEntity<Double> calcularMediaAluno(@PathVariable Long alunoId) {
        Double media = notaService.calcularMediaAluno(alunoId);
        return ResponseEntity.ok(media);
    }

    /**
     * Calcula média do aluno por tipo de avaliação
     */
    @GetMapping("/media/aluno/{alunoId}/tipo/{tipo}")
    public ResponseEntity<Double> calcularMediaAlunoPorTipo(
            @PathVariable Long alunoId, 
            @PathVariable TipoAvaliacao tipo) {
        Double media = notaService.calcularMediaAlunoPorTipo(alunoId, tipo);
        return ResponseEntity.ok(media);
    }

    /**
     * Calcula média da turma
     */
    @GetMapping("/media/turma/{turmaId}")
    public ResponseEntity<Double> calcularMediaTurma(@PathVariable Long turmaId) {
        Double media = notaService.calcularMediaTurma(turmaId);
        return ResponseEntity.ok(media);
    }
}