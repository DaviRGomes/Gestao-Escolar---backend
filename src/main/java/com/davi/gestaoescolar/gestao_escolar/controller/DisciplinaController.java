package com.davi.gestaoescolar.gestao_escolar.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.davi.gestaoescolar.gestao_escolar.model.Disciplina;
import com.davi.gestaoescolar.gestao_escolar.service.DisciplinaService;

import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {


    private DisciplinaService service;

    public DisciplinaController(DisciplinaService service) {
        this.service = service;
    }


    /**
     * @param disciplina
     * @return
     */
    @PostMapping
    public ResponseEntity <Disciplina> postDisciplina(@RequestBody Disciplina disciplina) {
        try {
            
            Disciplina novoDisciplina = service.salvar(disciplina);
            return new ResponseEntity<>(novoDisciplina, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Disciplina>> listar() {
        try {
            List<Disciplina> disciplinas = service.listarTodas();
            return new ResponseEntity<>(disciplinas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{nome}")
    public ResponseEntity<List<Disciplina>> buscarPorNome(@PathVariable String nome) {
        try {
            List<Disciplina> disciplinas = service.buscarPorNome(nome);
            return new ResponseEntity<>(disciplinas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Disciplina> buscarPorId(@PathVariable Long id) {
        try {
            Optional<Disciplina> disciplina = service.buscarPorId(id);
            return disciplina.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<Disciplina>> buscarPorProfessor(@PathVariable Long professorId) {
        try {
            List<Disciplina> disciplinas = service.buscarPorProfessor(professorId);
            return new ResponseEntity<>(disciplinas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
     
    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<Disciplina>> buscarPorTurma(@PathVariable Long turmaId) {
        try {
            List<Disciplina> disciplinas = service.buscarPorTurma(turmaId);
            return new ResponseEntity<>(disciplinas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Disciplina> atualizar(@PathVariable Long id, @RequestBody Disciplina disciplina) {
        try {
            Disciplina disciplinaAtualizada = service.atualizar(id, disciplina);
            return new ResponseEntity<>(disciplinaAtualizada, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        try {
            service.desativar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
