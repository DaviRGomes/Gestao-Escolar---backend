package com.davi.gestaoescolar.gestao_escolar.controller;

import com.davi.gestaoescolar.gestao_escolar.dto.PlanejamentoDTO;
import com.davi.gestaoescolar.gestao_escolar.model.Disciplina;
import com.davi.gestaoescolar.gestao_escolar.model.Planejamento;
import com.davi.gestaoescolar.gestao_escolar.model.Turma;
import com.davi.gestaoescolar.gestao_escolar.service.DisciplinaService;
import com.davi.gestaoescolar.gestao_escolar.service.PlanejamentoService;
import com.davi.gestaoescolar.gestao_escolar.service.TurmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller para gerenciar operações relacionadas aos Planejamentos
 * 
 * @author Sistema de Gestão Escolar
 * @version 1.0
 */
@RestController
@RequestMapping("/api/planejamentos")
@CrossOrigin(origins = "*")
public class PlanejamentoController {

    @Autowired
    private PlanejamentoService planejamentoService;
    
    @Autowired
    private DisciplinaService disciplinaService;
    
    @Autowired
    private TurmaService turmaService;

    /**
     * Criar um novo planejamento
     * 
     * @param dto Dados do planejamento a ser criado
     * @return ResponseEntity com o planejamento criado
     */
    @PostMapping
    public ResponseEntity<Planejamento> criarPlanejamento(@RequestBody PlanejamentoDTO dto) {
        try {
            // Buscar disciplina e turma pelos IDs
            Disciplina disciplina = disciplinaService.buscarPorId(dto.getDisciplina().getId())
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));
            Turma turma = turmaService.buscarPorId(dto.getTurma().getId())
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
            
            // Criar o planejamento
            Planejamento planejamento = new Planejamento();
            planejamento.setDescricao(dto.getDescricao());
            planejamento.setSemestre(dto.getSemestre());
            planejamento.setAno(dto.getAno());
            planejamento.setDisciplina(disciplina);
            planejamento.setTurma(turma);
            
            Planejamento novoPlanejamento = planejamentoService.salvar(planejamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoPlanejamento);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Atualizar um planejamento existente
     * 
     * @param id ID do planejamento a ser atualizado
     * @param planejamento Novos dados do planejamento
     * @return ResponseEntity com o planejamento atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<Planejamento> atualizarPlanejamento(@PathVariable Long id, @RequestBody Planejamento planejamento) {
        try {
            Planejamento planejamentoAtualizado = planejamentoService.atualizar(id, planejamento);
            return new ResponseEntity<>(planejamentoAtualizado, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar planejamento por ID
     * 
     * @param id ID do planejamento
     * @return ResponseEntity com o planejamento encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<Planejamento> buscarPorId(@PathVariable Long id) {
        try {
            Optional<Planejamento> planejamento = planejamentoService.buscarPorId(id);
            if (planejamento.isPresent()) {
                return new ResponseEntity<>(planejamento.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Listar todos os planejamentos
     * 
     * @return ResponseEntity com a lista de planejamentos
     */
    @GetMapping
    public ResponseEntity<List<Planejamento>> listarTodos() {
        try {
            List<Planejamento> planejamentos = planejamentoService.listarTodos();
            return new ResponseEntity<>(planejamentos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar planejamentos por disciplina
     * 
     * @param disciplinaId ID da disciplina
     * @return ResponseEntity com a lista de planejamentos
     */
    @GetMapping("/disciplina/{disciplinaId}")
    public ResponseEntity<List<Planejamento>> buscarPorDisciplina(@PathVariable Long disciplinaId) {
        try {
            List<Planejamento> planejamentos = planejamentoService.buscarPorDisciplina(disciplinaId);
            return new ResponseEntity<>(planejamentos, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar planejamentos por turma
     * 
     * @param turmaId ID da turma
     * @return ResponseEntity com a lista de planejamentos
     */
    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<Planejamento>> buscarPorTurma(@PathVariable Long turmaId) {
        try {
            List<Planejamento> planejamentos = planejamentoService.buscarPorTurma(turmaId);
            return new ResponseEntity<>(planejamentos, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar planejamentos por ano
     * 
     * @param ano Ano letivo
     * @return ResponseEntity com a lista de planejamentos
     */
    @GetMapping("/ano/{ano}")
    public ResponseEntity<List<Planejamento>> buscarPorAno(@PathVariable Integer ano) {
        try {
            List<Planejamento> planejamentos = planejamentoService.buscarPorAno(ano);
            return new ResponseEntity<>(planejamentos, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar planejamentos por semestre
     * 
     * @param semestre Semestre ("1" ou "2")
     * @return ResponseEntity com a lista de planejamentos
     */
    @GetMapping("/semestre/{semestre}")
    public ResponseEntity<List<Planejamento>> buscarPorSemestre(@PathVariable String semestre) {
        try {
            List<Planejamento> planejamentos = planejamentoService.buscarPorSemestre(semestre);
            return new ResponseEntity<>(planejamentos, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar planejamentos por ano e semestre
     * 
     * @param ano Ano letivo
     * @param semestre Semestre ("1" ou "2")
     * @return ResponseEntity com a lista de planejamentos
     */
    @GetMapping("/ano/{ano}/semestre/{semestre}")
    public ResponseEntity<List<Planejamento>> buscarPorAnoESemestre(@PathVariable Integer ano, @PathVariable String semestre) {
        try {
            List<Planejamento> planejamentos = planejamentoService.buscarPorAnoESemestre(ano, semestre);
            return new ResponseEntity<>(planejamentos, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar planejamento específico por disciplina, turma, semestre e ano
     * 
     * @param disciplinaId ID da disciplina
     * @param turmaId ID da turma
     * @param semestre Semestre ("1" ou "2")
     * @param ano Ano letivo
     * @return ResponseEntity com o planejamento encontrado
     */
    @GetMapping("/especifico")
    public ResponseEntity<Planejamento> buscarEspecifico(
            @RequestParam Long disciplinaId,
            @RequestParam Long turmaId,
            @RequestParam String semestre,
            @RequestParam Integer ano) {
        try {
            Optional<Planejamento> planejamento = planejamentoService.buscarEspecifico(disciplinaId, turmaId, semestre, ano);
            if (planejamento.isPresent()) {
                return new ResponseEntity<>(planejamento.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Criar planejamento básico
     * 
     * @param disciplinaId ID da disciplina
     * @param turmaId ID da turma
     * @param semestre Semestre ("1" ou "2")
     * @param ano Ano letivo
     * @return ResponseEntity com o planejamento criado
     */
    @PostMapping("/basico")
    public ResponseEntity<Planejamento> criarPlanejamentoBasico(
            @RequestParam Long disciplinaId,
            @RequestParam Long turmaId,
            @RequestParam String semestre,
            @RequestParam Integer ano) {
        try {
            Planejamento planejamento = planejamentoService.criarPlanejamentoBasico(disciplinaId, turmaId, semestre, ano);
            return new ResponseEntity<>(planejamento, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletar um planejamento
     * 
     * @param id ID do planejamento a ser deletado
     * @return ResponseEntity indicando o resultado da operação
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPlanejamento(@PathVariable Long id) {
        try {
            planejamentoService.deletar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}