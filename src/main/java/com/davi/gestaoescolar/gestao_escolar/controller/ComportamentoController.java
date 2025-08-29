package com.davi.gestaoescolar.gestao_escolar.controller;

import com.davi.gestaoescolar.gestao_escolar.model.Comportamento;
import com.davi.gestaoescolar.gestao_escolar.service.ComportamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comportamentos")
@CrossOrigin(origins = "*")
public class ComportamentoController {

    @Autowired
    private ComportamentoService comportamentoService;

    /**
     * Criar um novo comportamento
     */
    @PostMapping
    public ResponseEntity<?> criarComportamento(@RequestBody Comportamento comportamento) {
        try {
            Comportamento novoComportamento = comportamentoService.salvar(comportamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoComportamento);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    /**
     * Buscar comportamento por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Optional<Comportamento> comportamento = comportamentoService.buscarPorId(id);
            if (comportamento.isPresent()) {
                return ResponseEntity.ok(comportamento.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar comportamento: " + e.getMessage());
        }
    }

    /**
     * Listar todos os comportamentos
     */
    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            List<Comportamento> comportamentos = comportamentoService.listarTodos();
            return ResponseEntity.ok(comportamentos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar comportamentos: " + e.getMessage());
        }
    }

    /**
     * Atualizar comportamento
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarComportamento(@PathVariable Long id, @RequestBody Comportamento comportamento) {
        try {
            Comportamento comportamentoAtualizado = comportamentoService.atualizar(id, comportamento);
            return ResponseEntity.ok(comportamentoAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Comportamento não encontrado: " + e.getMessage());
        }
    }

    /**
     * Deletar comportamento
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarComportamento(@PathVariable Long id) {
        try {
            comportamentoService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Comportamento não encontrado: " + e.getMessage());
        }
    }
    /**
     * Buscar comportamentos por data
     */
    @GetMapping("/data/{data}")
    public ResponseEntity<?> buscarPorData(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        try {
            List<Comportamento> comportamentos = comportamentoService.buscarPorData(data);
            return ResponseEntity.ok(comportamentos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar comportamentos: " + e.getMessage());
        }
    }

    /**
     * Buscar comportamentos por período
     */
    @GetMapping("/periodo")
    public ResponseEntity<?> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        try {
            List<Comportamento> comportamentos = comportamentoService.buscarPorPeriodo(dataInicio, dataFim);
            return ResponseEntity.ok(comportamentos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar comportamentos: " + e.getMessage());
        }
    }

    /**
     * Buscar comportamentos por professor
     */
    @GetMapping("/professor/{professorId}")
    public ResponseEntity<?> buscarPorProfessor(@PathVariable Long professorId) {
        try {
            List<Comportamento> comportamentos = comportamentoService.buscarPorProfessor(professorId);
            return ResponseEntity.ok(comportamentos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar comportamentos: " + e.getMessage());
        }
    }


    /**
     * Gerar relatório de comportamentos por período
     */
    @GetMapping("/relatorio")
    public ResponseEntity<?> gerarRelatorioComportamentos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        try {
            String relatorio = comportamentoService.gerarRelatorioComportamentos(dataInicio, dataFim);
            return ResponseEntity.ok(relatorio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar relatório: " + e.getMessage());
        }
    }

    /**
     * Gerar relatório de comportamentos por professor
     */
    @GetMapping("/relatorio/professor/{professorId}")
    public ResponseEntity<?> gerarRelatorioPorProfessor(
            @PathVariable Long professorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        try {
            String relatorio = comportamentoService.gerarRelatorioPorProfessor(professorId, dataInicio, dataFim);
            return ResponseEntity.ok(relatorio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar relatório: " + e.getMessage());
        }
    }

    /**
     * Gera a busca por lista de aluno
     */
    @GetMapping("aluno/{alunoId}")
    public ResponseEntity<?> buscarComportamentosPorAluno(@PathVariable Long alunoId) {
        try {
            List<Comportamento> comportamentos = comportamentoService.buscarTodosOsComportamentosDoAluno(alunoId);
            return ResponseEntity.ok(comportamentos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar comportamentos: " + e.getMessage());
        }
    }

}