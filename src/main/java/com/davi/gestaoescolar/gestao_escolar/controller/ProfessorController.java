package com.davi.gestaoescolar.gestao_escolar.controller;

import com.davi.gestaoescolar.gestao_escolar.dto.Professor.ProfessorDtoIn;
import com.davi.gestaoescolar.gestao_escolar.dto.Professor.ProfessorDtoOut;
import com.davi.gestaoescolar.gestao_escolar.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
    
import java.util.List;
import java.util.Optional;
    
        @RestController
        @RequestMapping("/api/professores")
        public class ProfessorController {
    
            @Autowired
            private ProfessorService professorService;
    
        /**
         * Criar novo professor
         */
        @PostMapping
        public ResponseEntity<ProfessorDtoOut> criarProfessor(@RequestBody ProfessorDtoIn professorDto) {
            ProfessorDtoOut professorSalvo = professorService.salvar(professorDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(professorSalvo);
        }
    
        /**
         * Atualizar professor existente
         */
        @PutMapping("/{id}")
        public ResponseEntity<ProfessorDtoOut> atualizarProfessor(@PathVariable Long id, @RequestBody ProfessorDtoIn professorDto) {
            ProfessorDtoOut professorAtualizado = professorService.atualizar(id, professorDto);
            return ResponseEntity.ok(professorAtualizado);
        }
    
        /**
         * Buscar professor por ID
         */
        @GetMapping("/{id}")
        public ResponseEntity<ProfessorDtoOut> buscarProfessorPorId(@PathVariable Long id) {
            Optional<ProfessorDtoOut> professor = professorService.buscarPorIdDto(id);
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
        public ResponseEntity<ProfessorDtoOut> buscarProfessorPorCpf(@PathVariable String cpf) {
            Optional<ProfessorDtoOut> professor = professorService.buscarPorCpfDto(cpf);
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
        public ResponseEntity<ProfessorDtoOut> buscarProfessorPorEmail(@PathVariable String email) {
            Optional<ProfessorDtoOut> professor = professorService.buscarPorEmailDto(email);
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
        public ResponseEntity<List<ProfessorDtoOut>> listarTodosProfessores() {
            List<ProfessorDtoOut> professores = professorService.listarTodosDto();
            return ResponseEntity.ok(professores);
        }
    
        /**
         * Deletar professor permanentemente
         */
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletarProfessor(@PathVariable Long id) {
            professorService.deletar(id);
            return ResponseEntity.noContent().build();
        }
}