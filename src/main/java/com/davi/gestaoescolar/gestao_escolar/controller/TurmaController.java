package com.davi.gestaoescolar.gestao_escolar.controller;

import com.davi.gestaoescolar.gestao_escolar.model.Turma;
import com.davi.gestaoescolar.gestao_escolar.model.enums.Periodo;
import com.davi.gestaoescolar.gestao_escolar.service.TurmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/turmas")
@CrossOrigin(origins = "*")
public class TurmaController {

    @Autowired
    private TurmaService turmaService;

    /**
     * Criar nova turma
     */
    @PostMapping
    public ResponseEntity<Turma> criarTurma(@RequestBody Turma turma) {
        try {
            Turma novaTurma = turmaService.salvar(turma);
            return new ResponseEntity<>(novaTurma, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Atualizar turma existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Turma> atualizarTurma(@PathVariable Long id, @RequestBody Turma turma) {
        try {
            Turma turmaAtualizada = turmaService.atualizar(id, turma);
            return new ResponseEntity<>(turmaAtualizada, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar turma por ID
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<Turma> buscarPorId(@PathVariable Long id) {
        try {
            Optional<Turma> turma = turmaService.buscarPorId(id);
            return turma.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar turmas por nome
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Turma>> buscarPorNome(@PathVariable String nome) {
        try {
            List<Turma> turmas = turmaService.buscarPorNome(nome);
            return new ResponseEntity<>(turmas, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar turmas por ano letivo
     */
    @GetMapping("/ano/{anoLetivo}")
    public ResponseEntity<List<Turma>> buscarPorAnoLetivo(@PathVariable String anoLetivo) {
        try {
            List<Turma> turmas = turmaService.buscarPorAnoLetivo(anoLetivo);
            return new ResponseEntity<>(turmas, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar turmas por semestre
     */
    @GetMapping("/semestre/{semestre}")
    public ResponseEntity<List<Turma>> buscarPorSemestre(@PathVariable String semestre) {
        try {
            List<Turma> turmas = turmaService.buscarPorSemestre(semestre);
            return new ResponseEntity<>(turmas, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar turmas por período
     */
    @GetMapping("/periodo/{periodo}")
    public ResponseEntity<List<Turma>> buscarPorPeriodo(@PathVariable Periodo periodo) {
        try {
            List<Turma> turmas = turmaService.buscarPorPeriodo(periodo);
            return new ResponseEntity<>(turmas, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar turmas por ano letivo e semestre
     */
    @GetMapping("/ano/{anoLetivo}/semestre/{semestre}")
    public ResponseEntity<List<Turma>> buscarPorAnoLetivoESemestre(
            @PathVariable String anoLetivo, 
            @PathVariable String semestre) {
        try {
            List<Turma> turmas = turmaService.buscarPorAnoLetivoESemestre(anoLetivo, semestre);
            return new ResponseEntity<>(turmas, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Listar todas as turmas
     */
    @GetMapping
    public ResponseEntity<List<Turma>> listarTodas() {
        try {
            List<Turma> turmas = turmaService.listarTodas();
            return new ResponseEntity<>(turmas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Listar apenas turmas ativas
     */
    @GetMapping("/ativas")
    public ResponseEntity<List<Turma>> listarAtivas() {
        try {
            List<Turma> turmas = turmaService.listarAtivas();
            return new ResponseEntity<>(turmas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Listar apenas turmas inativas
     */
    @GetMapping("/inativas")
    public ResponseEntity<List<Turma>> listarInativas() {
        try {
            List<Turma> turmas = turmaService.listarInativas();
            return new ResponseEntity<>(turmas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Desativar turma (soft delete)
     */
    @PatchMapping("/desativar/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        try {
            turmaService.desativar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Reativar turma
     */
    @PatchMapping("/reativar/{id}")
    public ResponseEntity<Void> reativar(@PathVariable Long id) {
        try {
            turmaService.reativar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletar turma permanentemente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            turmaService.deletar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}