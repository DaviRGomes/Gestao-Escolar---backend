package com.davi.gestaoescolar.gestao_escolar.controller;

import com.davi.gestaoescolar.gestao_escolar.model.Aluno;
import com.davi.gestaoescolar.gestao_escolar.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/alunos")
@CrossOrigin(origins = "*")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    /**
     * Criar novo aluno
     */
    @PostMapping
    public ResponseEntity<Aluno> criarAluno(@RequestBody Aluno aluno) {
        try {
            Aluno novoAluno = alunoService.salvar(aluno);
            return new ResponseEntity<>(novoAluno, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Buscar aluno por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Aluno> buscarPorId(@PathVariable Long id) {
        try {
            Optional<Aluno> aluno = alunoService.buscarPorId(id);
            return aluno.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Listar todos os alunos
     */
    @GetMapping
    public ResponseEntity<List<Aluno>> listarTodos() {
        try {
            List<Aluno> alunos = alunoService.listarTodos();
            return new ResponseEntity<>(alunos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Atualizar aluno
     */
    @PutMapping("/{id}")
    public ResponseEntity<Aluno> atualizarAluno(@PathVariable Long id, @RequestBody Aluno aluno) {
        try {
            Aluno alunoAtualizado = alunoService.atualizar(aluno);
            return new ResponseEntity<>(alunoAtualizado, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletar aluno
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAluno(@PathVariable Long id) {
        try {
            alunoService.deletar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Buscar alunos ativos
     */
    @GetMapping("/ativos")
    public ResponseEntity<List<Aluno>> listarAtivos() {
        try {
            List<Aluno> alunos = alunoService.listarAtivos();
            return new ResponseEntity<>(alunos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar aluno por CPF
     */
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Aluno> buscarPorCpf(@PathVariable String cpf) {
        try {
            Optional<Aluno> aluno = alunoService.buscarPorCpf(cpf);
            return aluno.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar alunos por nome
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Aluno>> buscarPorNome(@PathVariable String nome) {
        try {
            List<Aluno> alunos = alunoService.buscarPorNome(nome);
            return new ResponseEntity<>(alunos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Ativar aluno
     */
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> ativarAluno(@PathVariable Long id) {
        try {
            alunoService.ativar(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Desativar aluno
     */
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativarAluno(@PathVariable Long id) {
        try {
            alunoService.desativar(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}