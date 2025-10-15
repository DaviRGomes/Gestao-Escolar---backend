package com.davi.gestaoescolar.gestao_escolar.controller;

import com.davi.gestaoescolar.gestao_escolar.dto.Disciplina.DisciplinaDtoIn;
import com.davi.gestaoescolar.gestao_escolar.dto.Disciplina.DisciplinaDtoOut;
import com.davi.gestaoescolar.gestao_escolar.service.DisciplinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disciplinas")
@CrossOrigin(origins = "*")
public class DisciplinaController {

    @Autowired
    private DisciplinaService disciplinaService;

    /**
     * Cria uma nova disciplina
     */
    @PostMapping
    public ResponseEntity<DisciplinaDtoOut> criarDisciplina(@RequestBody DisciplinaDtoIn disciplinaDto) {
        DisciplinaDtoOut disciplinaCriada = disciplinaService.salvar(disciplinaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(disciplinaCriada);
    }

    /**
     * Atualiza uma disciplina existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<DisciplinaDtoOut> atualizarDisciplina(
            @PathVariable Long id,
            @RequestBody DisciplinaDtoIn disciplinaDto) {
        DisciplinaDtoOut disciplinaAtualizada = disciplinaService.atualizar(id, disciplinaDto);
        return ResponseEntity.ok(disciplinaAtualizada);
    }

    /**
     * Busca disciplina por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<DisciplinaDtoOut> buscarPorId(@PathVariable Long id) {
        DisciplinaDtoOut disciplina = disciplinaService.buscarPorId(id);
        return ResponseEntity.ok(disciplina);
    }

    /**
     * Lista todas as disciplinas com filtros opcionais (nome, ativo, cargaHorariaMinima)
     */
    @GetMapping
    public ResponseEntity<List<DisciplinaDtoOut>> listarTodas(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) Integer cargaHorariaMinima
    ) {
        List<DisciplinaDtoOut> disciplinas = disciplinaService.listarTodas();

        if (nome != null && !nome.trim().isEmpty()) {
            String termo = nome.trim().toLowerCase();
            disciplinas = disciplinas.stream()
                    .filter(d -> d.getNome() != null && d.getNome().toLowerCase().contains(termo))
                    .toList();
        }

        if (ativo != null) {
            disciplinas = disciplinas.stream()
                    .filter(d -> d.getAtivo() != null && d.getAtivo().equals(ativo))
                    .toList();
        }

        if (cargaHorariaMinima != null) {
            disciplinas = disciplinas.stream()
                    .filter(d -> d.getCargaHoraria() != null && d.getCargaHoraria() >= cargaHorariaMinima)
                    .toList();
        }

        return ResponseEntity.ok(disciplinas);
    }


    /**
     * Busca disciplinas por nome
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<DisciplinaDtoOut>> buscarPorNome(@RequestParam String nome) {
        List<DisciplinaDtoOut> disciplinas = disciplinaService.buscarPorNome(nome);
        return ResponseEntity.ok(disciplinas);
    }

    /**
     * Busca disciplinas por professor
     */
    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<DisciplinaDtoOut>> buscarPorProfessor(@PathVariable Long professorId) {
        List<DisciplinaDtoOut> disciplinas = disciplinaService.buscarPorProfessor(professorId);
        return ResponseEntity.ok(disciplinas);
    }

    /**
     * Busca disciplinas por turma
     */
    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<DisciplinaDtoOut>> buscarPorTurma(@PathVariable Long turmaId) {
        List<DisciplinaDtoOut> disciplinas = disciplinaService.buscarPorTurma(turmaId);
        return ResponseEntity.ok(disciplinas);
    }

    /**
     * Deleta uma disciplina permanentemente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        disciplinaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Atribui um professor a uma disciplina
     */
    @PatchMapping("/{disciplinaId}/professor/{professorId}")
    public ResponseEntity<DisciplinaDtoOut> atribuirProfessor(
            @PathVariable Long disciplinaId,
            @PathVariable Long professorId) {
        DisciplinaDtoOut disciplinaAtualizada = disciplinaService.atribuirProfessor(disciplinaId, professorId);
        return ResponseEntity.ok(disciplinaAtualizada);
    }

    /**
     * Alterar status (ativo/inativo) da disciplina com 0 ou 1
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<DisciplinaDtoOut> alterarStatus(@PathVariable Long id,
                                                          @RequestParam Integer ativo) {
        DisciplinaDtoOut disciplina = disciplinaService.alterarStatus(id, ativo);
        return ResponseEntity.ok(disciplina);
    }
}
