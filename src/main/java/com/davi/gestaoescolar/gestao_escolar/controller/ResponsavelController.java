package com.davi.gestaoescolar.gestao_escolar.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.davi.gestaoescolar.gestao_escolar.dto.Responsavel.ResponsavelDtoIn;
import com.davi.gestaoescolar.gestao_escolar.dto.Responsavel.ResponsavelDtoOut;
import com.davi.gestaoescolar.gestao_escolar.service.ResponsavelService;

@RestController
@RequestMapping("/api/responsaveis")
public class ResponsavelController {

    private ResponsavelService responsavelService;

    public ResponsavelController(ResponsavelService responsavelService) {
        this.responsavelService = responsavelService;
    }

    @PostMapping
    public ResponseEntity<ResponsavelDtoOut> criar(@RequestBody ResponsavelDtoIn dtoIn) {
        ResponsavelDtoOut salvo = responsavelService.salvar(dtoIn);
        return ResponseEntity.ok(salvo);
    }

    @GetMapping
    public ResponseEntity<List<ResponsavelDtoOut>> listar() {
        List<ResponsavelDtoOut> responsaveis = responsavelService.listarTodos();
        return ResponseEntity.ok(responsaveis);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Optional<ResponsavelDtoOut>> buscarPorId(@PathVariable Long id) {
        Optional<ResponsavelDtoOut> responsavel = responsavelService.buscarPorId(id);
        return ResponseEntity.ok(responsavel);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Optional<ResponsavelDtoOut>> buscarPorCpf(@PathVariable String cpf) {
        Optional<ResponsavelDtoOut> responsavel = responsavelService.buscarPorCpf(cpf);
        return ResponseEntity.ok(responsavel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponsavelDtoOut> atualizar(@PathVariable Long id, @RequestBody ResponsavelDtoIn dtoIn) {
        ResponsavelDtoOut atualizado = responsavelService.atualizar(id, dtoIn);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        responsavelService.deletar(id);
        return ResponseEntity.ok().build();
    }
}