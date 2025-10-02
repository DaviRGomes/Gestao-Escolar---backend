package com.davi.gestaoescolar.gestao_escolar.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
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
        try {
            Responsavel salvo = responsavelService.salvar(responsavel);
            return  new ResponseEntity<>(salvo, HttpStatus.CREATED);
            
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }        
    }

    @GetMapping
    public ResponseEntity<List<Responsavel>> listar() {
        try {
            List<Responsavel> responsaveis = responsavelService.listarTodos();
            return new ResponseEntity<>(responsaveis, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Responsavel> buscarPorId(@PathVariable Long id) {
        try {
            Optional<Responsavel> responsavel = responsavelService.buscarPorId(id);
            return new ResponseEntity<>(responsavel.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }      
    
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Responsavel> buscarPorCpf(@PathVariable String cpf) {
        try {
            Optional<Responsavel> responsavel = responsavelService.buscarPorCpf(cpf);
            return new ResponseEntity<>(responsavel.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }   

    @PutMapping("/{id}")
    public ResponseEntity<Responsavel> atualizar(@PathVariable Long id, @RequestBody Responsavel responsavel) {
        try {
            Optional<Responsavel> responsavelExistente = responsavelService.buscarPorId(id);
            if (responsavelExistente.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            Responsavel atualizado = responsavelService.atualizar(id, responsavel);
            return new ResponseEntity<>(atualizado, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            Optional<Responsavel> responsavelExistente = responsavelService.buscarPorId(id);
            if (responsavelExistente.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            responsavelService.deletar(id);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}