package com.davi.gestaoescolar.gestao_escolar.controller;

import com.davi.gestaoescolar.gestao_escolar.dto.Comportamento.ComportamentoDtoIn;
import com.davi.gestaoescolar.gestao_escolar.dto.Comportamento.ComportamentoDtoOut;
import com.davi.gestaoescolar.gestao_escolar.model.enums.Gravidade;
import com.davi.gestaoescolar.gestao_escolar.model.enums.TipoComportamento;
import com.davi.gestaoescolar.gestao_escolar.service.ComportamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comportamentos")
public class ComportamentoController {

    @Autowired
    private ComportamentoService comportamentoService;

    /**
     * Cria um novo registro de comportamento
     */
    @PostMapping
    public ResponseEntity<ComportamentoDtoOut> criarComportamento(@RequestBody ComportamentoDtoIn comportamentoDto) {

        ComportamentoDtoOut comportamento = comportamentoService.salvar(comportamentoDto);
        return ResponseEntity.ok(comportamento);
    }

    /**
     * Atualiza um registro de comportamento existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<ComportamentoDtoOut> atualizarComportamento(
            @PathVariable Long id, 
            @RequestBody ComportamentoDtoIn comportamentoDto) {

        ComportamentoDtoOut comportamento = comportamentoService.atualizar(id, comportamentoDto);
        return ResponseEntity.ok(comportamento);
    }

    /**
     * Busca comportamento por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<ComportamentoDtoOut>> buscarPorId(@PathVariable Long id) {

        Optional<ComportamentoDtoOut> comportamento = comportamentoService.buscarPorId(id);
        return ResponseEntity.ok(comportamento);
    }

    /**
     * Lista todos os comportamentos
     */
    @GetMapping
    public ResponseEntity<List<ComportamentoDtoOut>> listarTodos() {

        List<ComportamentoDtoOut> comportamento = comportamentoService.listarTodos();
        return ResponseEntity.ok(comportamento);
    }

    /**
     * Busca comportamentos por data específica
     */
    @GetMapping("/data/{data}")
    public ResponseEntity<List<ComportamentoDtoOut>> buscarPorData(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        List<ComportamentoDtoOut> comportamento = comportamentoService.buscarPorData(data);
        return ResponseEntity.ok(comportamento);
    }

    /**
     * Busca comportamentos por período
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<ComportamentoDtoOut>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        List<ComportamentoDtoOut> comportamento = comportamentoService.buscarPorPeriodo(dataInicio,dataFim);
        return ResponseEntity.ok(comportamento);
    }

    /**
     * Busca comportamentos por tipo
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ComportamentoDtoOut>> buscarPorTipo(@PathVariable TipoComportamento tipo) {

        List<ComportamentoDtoOut> comportamento = comportamentoService.buscarPorTipo(tipo);
        return ResponseEntity.ok(comportamento);
    }

    /**
     * Busca comportamentos por nível de gravidade
     */
    @GetMapping("/nivel/{nivel}")
    public ResponseEntity<List<ComportamentoDtoOut>> buscarPorNivel(@PathVariable Gravidade nivel) {

        List<ComportamentoDtoOut> comportamento = comportamentoService.buscarPorNivel(nivel);
        return ResponseEntity.ok(comportamento);
    }

    /**
     * Busca comportamentos por professor
     */
    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<ComportamentoDtoOut>> buscarPorProfessor(@PathVariable Long professorId) {

        List<ComportamentoDtoOut> comportamento = comportamentoService.buscarPorProfessor(professorId);
        return ResponseEntity.ok(comportamento);
    }

    /**
     * Busca comportamentos por aluno
     */
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<ComportamentoDtoOut>> buscarPorAluno(@PathVariable Long alunoId) {

        List<ComportamentoDtoOut> comportamento = comportamentoService.buscarPorAluno(alunoId);
        return ResponseEntity.ok(comportamento);
    }

    /**
     * Busca comportamentos por turma
     */
    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<ComportamentoDtoOut>> buscarPorTurma(@PathVariable Long turmaId) {

        List<ComportamentoDtoOut> comportamento = comportamentoService.buscarPorTurma(turmaId);
        return ResponseEntity.ok(comportamento);
    }

    /**
     * Busca comportamentos de hoje
     */
    @GetMapping("/hoje")
    public ResponseEntity<List<ComportamentoDtoOut>> buscarComportamentosDeHoje() {

        List<ComportamentoDtoOut> comportamento = comportamentoService.buscarComportamentosDeHoje();
        return ResponseEntity.ok(comportamento);
    }

    /**
     * Deleta um comportamento
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarComportamento(@PathVariable Long id) {

        this.comportamentoService.deletar(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Gera relatório de comportamentos por professor
     */
    @GetMapping("/relatorio/professor/{professorId}")
    public ResponseEntity<String> gerarRelatorioPorProfessor(
            @PathVariable Long professorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        this.comportamentoService.gerarRelatorioPorProfessor(professorId, dataInicio, dataFim);
        return ResponseEntity.ok().build();
    }
}