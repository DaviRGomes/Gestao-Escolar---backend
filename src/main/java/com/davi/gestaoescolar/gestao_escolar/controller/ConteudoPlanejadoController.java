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
    public ResponseEntity<List<ConteudoPlanejadoDtoOut>> listarTodos(
            @RequestParam(required = false) String conteudo,
            @RequestParam(required = false) Boolean concluido,
            @RequestParam(required = false)
            @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE)
            java.time.LocalDate dataPrevista,
            @RequestParam(required = false) Long planejamentoId
    ) {
        List<ConteudoPlanejadoDtoOut> conteudos = conteudoPlanejadoService.listarTodos();

        if (conteudo != null && !conteudo.trim().isEmpty()) {
            String termo = conteudo.trim().toLowerCase();
            conteudos = conteudos.stream()
                    .filter(c -> c.getConteudo() != null && c.getConteudo().toLowerCase().contains(termo))
                    .toList();
        }

        if (concluido != null) {
            conteudos = conteudos.stream()
                    .filter(c -> c.getConcluido() != null && c.getConcluido().equals(concluido))
                    .toList();
        }

        if (dataPrevista != null) {
            conteudos = conteudos.stream()
                    .filter(c -> dataPrevista.equals(c.getDataPrevista()))
                    .toList();
        }

        if (planejamentoId != null) {
            conteudos = conteudos.stream()
                    .filter(c -> c.getPlanejamento() != null
                            && c.getPlanejamento().getId() != null
                            && c.getPlanejamento().getId().equals(planejamentoId))
                    .toList();
        }

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

    @PatchMapping("/{id}/concluido")
    public ResponseEntity<ConteudoPlanejadoDtoOut> alterarConclusao(@PathVariable Long id,
                                                                    @RequestParam Integer concluido) {
        ConteudoPlanejadoDtoOut atualizado = conteudoPlanejadoService.alterarConclusao(id, concluido);
        return ResponseEntity.ok(atualizado);
    }
}