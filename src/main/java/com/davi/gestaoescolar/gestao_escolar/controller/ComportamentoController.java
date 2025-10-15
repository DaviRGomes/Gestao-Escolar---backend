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