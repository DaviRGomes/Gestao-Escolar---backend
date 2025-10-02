package com.davi.gestaoescolar.gestao_escolar.controller;

import com.davi.gestaoescolar.gestao_escolar.dto.ConteudoPlanejado.ConteudoPlanejadoDtoIn;
import com.davi.gestaoescolar.gestao_escolar.dto.ConteudoPlanejado.ConteudoPlanejadoDtoOut;
import com.davi.gestaoescolar.gestao_escolar.service.ConteudoPlanejadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller para gerenciar operações relacionadas aos Conteúdos Planejados
 * 
 * @author Sistema de Gestão Escolar
 * @version 1.0
 */
@RestController
@RequestMapping("/api/conteudos-planejados")
public class ConteudoPlanejadoController {

    @Autowired
    private ConteudoPlanejadoService conteudoPlanejadoService;

    /**
     * Criar um novo conteúdo planejado
     * 
     * @param dto Dados do conteúdo planejado
     * @return ResponseEntity com o conteúdo planejado criado
     */
    @PostMapping
    public ResponseEntity<ConteudoPlanejadoDtoOut> salvar(@RequestBody ConteudoPlanejadoDtoIn dto) {
        ConteudoPlanejadoDtoOut novoConteudo = conteudoPlanejadoService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoConteudo);
    }

    /**
     * Atualizar um conteúdo planejado existente
     * 
     * @param id ID do conteúdo planejado
     * @param dto Dados atualizados
     * @return ResponseEntity com o conteúdo planejado atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ConteudoPlanejadoDtoOut> atualizar(@PathVariable Long id, @RequestBody ConteudoPlanejadoDtoIn dto) {
        ConteudoPlanejadoDtoOut conteudoAtualizado = conteudoPlanejadoService.atualizar(id, dto);
        return ResponseEntity.ok(conteudoAtualizado);
    }

    /**
     * Buscar conteúdo planejado por ID
     * 
     * @param id ID do conteúdo planejado
     * @return ResponseEntity com o conteúdo planejado encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConteudoPlanejadoDtoOut> buscarPorId(@PathVariable Long id) {
        ConteudoPlanejadoDtoOut conteudo = conteudoPlanejadoService.buscarPorId(id);
        return ResponseEntity.ok(conteudo);
    }

    /**
     * Listar todos os conteúdos planejados
     * 
     * @return ResponseEntity com a lista de conteúdos planejados
     */
    @GetMapping
    public ResponseEntity<List<ConteudoPlanejadoDtoOut>> listarTodos() {
        List<ConteudoPlanejadoDtoOut> conteudos = conteudoPlanejadoService.listarTodos();
        return ResponseEntity.ok(conteudos);
    }

    /**
     * Buscar conteúdos planejados por planejamento
     * 
     * @param planejamentoId ID do planejamento
     * @return ResponseEntity com a lista de conteúdos planejados
     */
    @GetMapping("/planejamento/{planejamentoId}")
    public ResponseEntity<List<ConteudoPlanejadoDtoOut>> buscarPorPlanejamento(@PathVariable Long planejamentoId) {
        List<ConteudoPlanejadoDtoOut> conteudos = conteudoPlanejadoService.buscarPorPlanejamento(planejamentoId);
        return ResponseEntity.ok(conteudos);
    }

    /**
     * Buscar conteúdos planejados por data prevista
     * 
     * @param data Data prevista (formato: yyyy-MM-dd)
     * @return ResponseEntity com a lista de conteúdos planejados
     */
    @GetMapping("/data/{data}")
    public ResponseEntity<List<ConteudoPlanejadoDtoOut>> buscarPorData(@PathVariable String data) {
        LocalDate dataPrevista = LocalDate.parse(data);
        List<ConteudoPlanejadoDtoOut> conteudos = conteudoPlanejadoService.buscarPorData(dataPrevista);
        return ResponseEntity.ok(conteudos);
    }

    /**
     * Buscar conteúdos planejados por turma
     * 
     * @param turmaId ID da turma
     * @return ResponseEntity com a lista de conteúdos planejados
     */
    // @GetMapping("/turma/{turmaId}")
    // public ResponseEntity<List<ConteudoPlanejado>> buscarPorTurma(@PathVariable Long turmaId) {
    //     try {
    //         List<ConteudoPlanejado> conteudos = conteudoPlanejadoService.buscarPorTurma(turmaId);
    //         return new ResponseEntity<>(conteudos, HttpStatus.OK);
    //     } catch (IllegalArgumentException e) {
    //         return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    //     } catch (Exception e) {
    //         return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

    /**
     * Deletar um conteúdo planejado
     * 
     * @param id ID do conteúdo planejado a ser deletado
     * @return ResponseEntity indicando o resultado da operação
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        conteudoPlanejadoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}