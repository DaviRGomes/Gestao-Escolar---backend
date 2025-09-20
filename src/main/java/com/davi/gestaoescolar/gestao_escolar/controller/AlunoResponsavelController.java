package com.davi.gestaoescolar.gestao_escolar.controller;

import com.davi.gestaoescolar.gestao_escolar.model.AlunoResponsavel;
import com.davi.gestaoescolar.gestao_escolar.service.AlunoResponsavelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/aluno-responsavel")
public class AlunoResponsavelController {
  
    
    @Autowired
    private AlunoResponsavelService alunoResponsavelService;
    
    
    @PostMapping
    public ResponseEntity<AlunoResponsavel> cadastrar(@RequestBody AlunoResponsavel alunoResponsavel) {
        try {
            AlunoResponsavel alunoResponsavelCadastrado = alunoResponsavelService.cadastrar(alunoResponsavel);
            return new ResponseEntity<>(alunoResponsavelCadastrado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            alunoResponsavelService.deletar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
