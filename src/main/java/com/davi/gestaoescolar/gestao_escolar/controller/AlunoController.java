package com.davi.gestaoescolar.gestao_escolar.controller;

import com.davi.gestaoescolar.gestao_escolar.dto.Aluno.AlunoDtoIn;
import com.davi.gestaoescolar.gestao_escolar.dto.Aluno.AlunoDtoOut;
import com.davi.gestaoescolar.gestao_escolar.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    /**dasdasda
     * Criar novo aluno
     */
    @PostMapping
    public ResponseEntity<AlunoDtoOut> criarAluno(@RequestBody AlunoDtoIn alunoCreate) {

        this.alunoService.salvar(alunoCreate);
        return  ResponseEntity.ok().build();
    }

    /**
     * Buscar aluno por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<AlunoDtoOut>> buscarPorId(@PathVariable Long id) {

        Optional<AlunoDtoOut> aluno = alunoService.buscarPorId(id);
        return  ResponseEntity.ok(aluno);
    }

    /**
     * Listar todos os alunos
     */
    @GetMapping
    public ResponseEntity<List<AlunoDtoOut>> listarTodos() {

        this.alunoService.listarTodos();
        return  ResponseEntity.ok().build();

    }

    /**
     * Atualizar aluno
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlunoDtoOut> atualizarAluno(@PathVariable Long id, @RequestBody AlunoDtoIn alunoCreate) {

        this.alunoService.atualizar(id, alunoCreate);
        return  ResponseEntity.ok().build();
    }

    /**
     * Deletar aluno
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAluno(@PathVariable Long id) {

        this.alunoService.deletar(id);
        return  ResponseEntity.ok().build();
    }

    /**
     * Buscar alunos ativos
     */
    @GetMapping("/ativos")
    public ResponseEntity<List<AlunoDtoOut>> listarAtivos() {

        this.alunoService.listarAtivos();
        return  ResponseEntity.ok().build();
    }

    /**
     * Buscar aluno por CPF
     */
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<AlunoDtoOut> buscarPorCpf(@PathVariable String cpf) {

        this.alunoService.buscarPorCpf(cpf);
        return  ResponseEntity.ok().build();
    }

    /**
     * Buscar alunos por nome
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<AlunoDtoOut>> buscarPorNome(@PathVariable String nome) {

        this.alunoService.buscarPorNome(nome);
        return  ResponseEntity.ok().build();
    }

    /**
     * Ativar aluno
     */
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<AlunoDtoOut> ativarAluno(@PathVariable Long id) {

        this.alunoService.ativar(id);
        return  ResponseEntity.ok().build();
    }

    /**
     * Desativar aluno
     */
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<AlunoDtoOut> desativarAluno(@PathVariable Long id) {

        this.alunoService.desativar(id);
        return  ResponseEntity.ok().build();
    }
}