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

        this.comportamentoService.salvar(comportamentoDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Atualiza um registro de comportamento existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<ComportamentoDtoOut> atualizarComportamento(
            @PathVariable Long id, 
            @RequestBody ComportamentoDtoIn comportamentoDto) {

        this.comportamentoService.atualizar(id, comportamentoDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Busca comportamento por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ComportamentoDtoOut> buscarPorId(@PathVariable Long id) {

        this.comportamentoService.buscarPorId(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Lista todos os comportamentos
     */
    @GetMapping
    public ResponseEntity<List<ComportamentoDtoOut>> listarTodos() {

        this.comportamentoService.listarTodos();
        return ResponseEntity.ok().build();
    }

    /**
     * Busca comportamentos por data específica
     */
    @GetMapping("/data/{data}")
    public ResponseEntity<List<ComportamentoDtoOut>> buscarPorData(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        this.comportamentoService.buscarPorData(data);
        return ResponseEntity.ok().build();
    }

    /**
     * Busca comportamentos por período
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<ComportamentoDtoOut>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        this.comportamentoService.buscarPorPeriodo(dataInicio,dataFim);
        return ResponseEntity.ok().build();
    }

    /**
     * Busca comportamentos por tipo
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ComportamentoDtoOut>> buscarPorTipo(@PathVariable TipoComportamento tipo) {

        this.comportamentoService.buscarPorTipo(tipo);
        return ResponseEntity.ok().build();
    }

    /**
     * Busca comportamentos por nível de gravidade
     */
    @GetMapping("/nivel/{nivel}")
    public ResponseEntity<List<ComportamentoDtoOut>> buscarPorNivel(@PathVariable Gravidade nivel) {

        this.comportamentoService.buscarPorNivel(nivel);
        return ResponseEntity.ok().build();
    }

    /**
     * Busca comportamentos por professor
     */
    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<ComportamentoDtoOut>> buscarPorProfessor(@PathVariable Long professorId) {

        this.comportamentoService.buscarPorProfessor(professorId);
        return ResponseEntity.ok().build();
    }

    /**
     * Busca comportamentos por aluno
     */
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<ComportamentoDtoOut>> buscarPorAluno(@PathVariable Long alunoId) {

        this.comportamentoService.buscarPorAluno(alunoId);
        return ResponseEntity.ok().build();
    }

    /**
     * Busca comportamentos por turma
     */
    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<ComportamentoDtoOut>> buscarPorTurma(@PathVariable Long turmaId) {

        this.comportamentoService.buscarPorTurma(turmaId);
        return ResponseEntity.ok().build();
    }

    /**
     * Busca comportamentos de hoje
     */
    @GetMapping("/hoje")
    public ResponseEntity<List<ComportamentoDtoOut>> buscarComportamentosDeHoje() {

        this.comportamentoService.buscarComportamentosDeHoje();
        return ResponseEntity.ok().build();
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