package com.davi.gestaoescolar.gestao_escolar.controller;

import com.davi.gestaoescolar.gestao_escolar.model.Professor;
import com.davi.gestaoescolar.gestao_escolar.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/professores")
@CrossOrigin(origins = "*")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    /**
     * Criar novo professor
     */
    @PostMapping
    public ResponseEntity<?> criarProfessor(@RequestBody Professor professor) {
        Professor professorSalvo = professorService.salvar(professor);
        return ResponseEntity.status(HttpStatus.CREATED).body(professorSalvo);
    }

    /**
     * Atualizar professor existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProfessor(@PathVariable Long id, @RequestBody Professor professor) {
        professor.setId(id);
        Professor professorAtualizado = professorService.atualizar(professor);
        return ResponseEntity.ok(professorAtualizado);
    }

    /**
     * Buscar professor por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarProfessorPorId(@PathVariable Long id) {
        Optional<Professor> professor = professorService.buscarPorId(id);
        if (professor.isPresent()) {
            return ResponseEntity.ok(professor.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Buscar professor por CPF
     */
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<?> buscarProfessorPorCpf(@PathVariable String cpf) {
        Optional<Professor> professor = professorService.buscarPorCpf(cpf);
        if (professor.isPresent()) {
            return ResponseEntity.ok(professor.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Buscar professor por email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> buscarProfessorPorEmail(@PathVariable String email) {
        Optional<Professor> professor = professorService.buscarPorEmail(email);
        if (professor.isPresent()) {
            return ResponseEntity.ok(professor.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Listar todos os professores
     */
    @GetMapping
    public ResponseEntity<?> listarTodosProfessores() {
        List<Professor> professores = professorService.listarTodos();
        return ResponseEntity.ok(professores);
    }

    /**
     * Deletar professor permanentemente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarProfessor(@PathVariable Long id) {
        professorService.deletar(id);
        return ResponseEntity.ok("Professor deletado com sucesso");
    }
}