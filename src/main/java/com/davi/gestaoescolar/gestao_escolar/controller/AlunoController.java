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

    /**
     * Buscar aluno por CPF
     */
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Optional<AlunoDtoOut>> buscarPorCpf(@PathVariable String cpf) {
        Optional<AlunoDtoOut> aluno = alunoService.buscarPorCpf(cpf);
        return ResponseEntity.ok(aluno);
    }

    /**
     * Criar novo aluno
     */
    @PostMapping
    public ResponseEntity<AlunoDtoOut> criarAluno(@RequestBody AlunoDtoIn alunoCreate) {

        AlunoDtoOut aluno = alunoService.salvar(alunoCreate);
        return  ResponseEntity.ok(aluno);
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
     * Listar todos os alunos (filtros opcionais: nome, ativo, dataNascimento)
     */
    @GetMapping
    public ResponseEntity<List<AlunoDtoOut>> listarTodos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false)
            @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE)
            java.time.LocalDate dataNascimento
    ) {

        List<AlunoDtoOut> alunos = alunoService.listarTodos();

        if (nome != null && !nome.trim().isEmpty()) {
            String termo = nome.trim().toLowerCase();
            alunos = alunos.stream()
                    .filter(a -> a.getNome() != null && a.getNome().toLowerCase().contains(termo))
                    .toList();
        }

        if (ativo != null) {
            alunos = alunos.stream()
                    .filter(a -> a.getAtivo() != null && a.getAtivo().equals(ativo))
                    .toList();
        }

        if (dataNascimento != null) {
            alunos = alunos.stream()
                    .filter(a -> dataNascimento.equals(a.getDataNascimento()))
                    .toList();
        }

        return ResponseEntity.ok(alunos);
    }

    /**
     * Atualizar aluno
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlunoDtoOut> atualizarAluno(@PathVariable Long id, @RequestBody AlunoDtoIn alunoCreate) {

        AlunoDtoOut aluno = alunoService.atualizar(id, alunoCreate);
        return  ResponseEntity.ok(aluno);
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
     * Alterar status (ativo/inativo) do aluno com 0 ou 1
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<AlunoDtoOut> alterarStatus(@PathVariable Long id,
                                                     @RequestParam Integer ativo) {
        AlunoDtoOut aluno = alunoService.alterarStatus(id, ativo);
        return ResponseEntity.ok(aluno);
    }
}