package com.davi.gestaoescolar.gestao_escolar.controller;

import com.davi.gestaoescolar.gestao_escolar.model.RegistroAula;
import com.davi.gestaoescolar.gestao_escolar.service.RegistroAulaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/registros-aula")
@CrossOrigin(origins = "*")
public class RegistroAulaController {

    @Autowired
    private RegistroAulaService registroAulaService;

    /**
     * Salvar novo registro de aula
     */
    @PostMapping
    public ResponseEntity<RegistroAula> salvar(@RequestBody RegistroAula registroAula) {
        try {
            RegistroAula novoRegistro = registroAulaService.salvar(registroAula);
            return new ResponseEntity<>(novoRegistro, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Atualizar registro de aula existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<RegistroAula> atualizar(@PathVariable Long id, @RequestBody RegistroAula registroAula) {
        try {
            RegistroAula registroAtualizado = registroAulaService.atualizar(id, registroAula);
            return new ResponseEntity<>(registroAtualizado, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar registro de aula por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<RegistroAula> buscarPorId(@PathVariable Long id) {
        try {
            Optional<RegistroAula> registro = registroAulaService.buscarPorId(id);
            if (registro.isPresent()) {
                return new ResponseEntity<>(registro.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Listar todos os registros de aula
     */
    @GetMapping
    public ResponseEntity<List<RegistroAula>> listarTodos() {
        try {
            List<RegistroAula> registros = registroAulaService.listarTodos();
            return new ResponseEntity<>(registros, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar registros de aula por data
     */
    @GetMapping("/data/{data}")
    public ResponseEntity<List<RegistroAula>> buscarPorData(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        try {
            List<RegistroAula> registros = registroAulaService.buscarPorData(data);
            return new ResponseEntity<>(registros, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar registros de aula por período
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<RegistroAula>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        try {
            List<RegistroAula> registros = registroAulaService.buscarPorPeriodo(dataInicio, dataFim);
            return new ResponseEntity<>(registros, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar registros de aula por turma
     */
    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<RegistroAula>> buscarPorTurma(@PathVariable Long turmaId) {
        try {
            List<RegistroAula> registros = registroAulaService.buscarPorTurma(turmaId);
            return new ResponseEntity<>(registros, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar registros de aula por disciplina
     */
    @GetMapping("/disciplina/{disciplinaId}")
    public ResponseEntity<List<RegistroAula>> buscarPorDisciplina(@PathVariable Long disciplinaId) {
        try {
            List<RegistroAula> registros = registroAulaService.buscarPorDisciplina(disciplinaId);
            return new ResponseEntity<>(registros, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar registros de aula por turma e disciplina
     */
    @GetMapping("/turma/{turmaId}/disciplina/{disciplinaId}")
    public ResponseEntity<List<RegistroAula>> buscarPorTurmaEDisciplina(
            @PathVariable Long turmaId, @PathVariable Long disciplinaId) {
        try {
            List<RegistroAula> registros = registroAulaService.buscarPorTurmaEDisciplina(turmaId, disciplinaId);
            return new ResponseEntity<>(registros, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar registros de aula por conteúdo planejado
     */
    @GetMapping("/conteudo-planejado/{conteudoPlanejadoId}")
    public ResponseEntity<List<RegistroAula>> buscarPorConteudoPlanejado(@PathVariable Long conteudoPlanejadoId) {
        try {
            List<RegistroAula> registros = registroAulaService.buscarPorConteudoPlanejado(conteudoPlanejadoId);
            return new ResponseEntity<>(registros, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar registros de aula por turma e período
     */
    @GetMapping("/turma/{turmaId}/periodo")
    public ResponseEntity<List<RegistroAula>> buscarPorTurmaEPeriodo(
            @PathVariable Long turmaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        try {
            List<RegistroAula> registros = registroAulaService.buscarPorTurmaEPeriodo(turmaId, dataInicio, dataFim);
            return new ResponseEntity<>(registros, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar registros de aula por disciplina e período
     */
    @GetMapping("/disciplina/{disciplinaId}/periodo")
    public ResponseEntity<List<RegistroAula>> buscarPorDisciplinaEPeriodo(
            @PathVariable Long disciplinaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        try {
            List<RegistroAula> registros = registroAulaService.buscarPorDisciplinaEPeriodo(disciplinaId, dataInicio, dataFim);
            return new ResponseEntity<>(registros, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar aulas de hoje
     */
    @GetMapping("/hoje")
    public ResponseEntity<List<RegistroAula>> buscarAulasDeHoje() {
        try {
            List<RegistroAula> registros = registroAulaService.buscarAulasDeHoje();
            return new ResponseEntity<>(registros, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar aulas da semana atual
     */
    @GetMapping("/semana")
    public ResponseEntity<List<RegistroAula>> buscarAulasDaSemana() {
        try {
            List<RegistroAula> registros = registroAulaService.buscarAulasDaSemana();
            return new ResponseEntity<>(registros, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar aulas do mês atual
     */
    @GetMapping("/mes")
    public ResponseEntity<List<RegistroAula>> buscarAulasDoMes() {
        try {
            List<RegistroAula> registros = registroAulaService.buscarAulasDoMes();
            return new ResponseEntity<>(registros, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Criar registro de aula básico
     */
    @PostMapping("/basico")
    public ResponseEntity<RegistroAula> criarRegistroBasico(
            @RequestParam Long turmaId,
            @RequestParam Long disciplinaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam String descricao) {
        try {
            RegistroAula registro = registroAulaService.criarRegistroBasico(turmaId, disciplinaId, data, descricao);
            return new ResponseEntity<>(registro, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}