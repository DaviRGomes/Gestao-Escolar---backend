package com.davi.gestaoescolar.gestao_escolar.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.davi.gestaoescolar.gestao_escolar.model.Responsavel;
import com.davi.gestaoescolar.gestao_escolar.service.ResponsavelService;

@RestController
@RequestMapping("/api/responsaveis")
public class ResponsavelController {

    private ResponsavelService responsavelService;

    public ResponsavelController(ResponsavelService responsavelService) {
        this.responsavelService = responsavelService;
    }

    @PostMapping
    public ResponseEntity<Responsavel> criar(@RequestBody Responsavel responsavel) {
        Responsavel salvo = responsavelService.salvar(responsavel);
        return ResponseEntity.ok(salvo);
    }

    @GetMapping
    public ResponseEntity<List<Responsavel>> listar() {
        List<Responsavel> responsaveis = responsavelService.listarTodos();
        return ResponseEntity.ok(responsaveis);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Optional<Responsavel>> buscarPorId(@PathVariable Long id) {
        Optional<Responsavel> responsavel = responsavelService.buscarPorId(id);
        return ResponseEntity.ok(responsavel);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Optional<Responsavel>> buscarPorCpf(@PathVariable String cpf) {
        Optional<Responsavel> responsavel = responsavelService.buscarPorCpf(cpf);
        return ResponseEntity.ok(responsavel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Responsavel> atualizar(@PathVariable Long id, @RequestBody Responsavel responsavel) {
        Responsavel atualizado = responsavelService.atualizar(id, responsavel);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        responsavelService.deletar(id);
        return ResponseEntity.ok().build();
    }
}