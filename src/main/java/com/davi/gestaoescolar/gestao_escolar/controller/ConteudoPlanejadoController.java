package com.davi.gestaoescolar.gestao_escolar.controller;

import com.davi.gestaoescolar.gestao_escolar.model.ConteudoPlanejado;
import com.davi.gestaoescolar.gestao_escolar.service.ConteudoPlanejadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller para gerenciar operações relacionadas aos Conteúdos Planejados
 * 
 * @author Sistema de Gestão Escolar
 * @version 1.0
 */
@RestController
@RequestMapping("/api/conteudos-planejados")
@CrossOrigin(origins = "*")
public class ConteudoPlanejadoController {

    @Autowired
    private ConteudoPlanejadoService conteudoPlanejadoService;

    /**
     * Criar um novo conteúdo planejado
     * 
     * @param conteudoPlanejado Dados do conteúdo planejado a ser criado
     * @return ResponseEntity com o conteúdo planejado criado
     */
    @PostMapping
    public ResponseEntity<ConteudoPlanejado> criarConteudoPlanejado(@RequestBody ConteudoPlanejado conteudoPlanejado) {
        try {
            ConteudoPlanejado novoConteudo = conteudoPlanejadoService.salvar(conteudoPlanejado);
            return new ResponseEntity<>(novoConteudo, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Atualizar um conteúdo planejado existente
     * 
     * @param id ID do conteúdo planejado a ser atualizado
     * @param conteudoPlanejado Novos dados do conteúdo planejado
     * @return ResponseEntity com o conteúdo planejado atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ConteudoPlanejado> atualizarConteudoPlanejado(@PathVariable Long id, @RequestBody ConteudoPlanejado conteudoPlanejado) {
        try {
            ConteudoPlanejado conteudoAtualizado = conteudoPlanejadoService.atualizar(id, conteudoPlanejado);
            return new ResponseEntity<>(conteudoAtualizado, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar conteúdo planejado por ID
     * 
     * @param id ID do conteúdo planejado
     * @return ResponseEntity com o conteúdo planejado encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConteudoPlanejado> buscarPorId(@PathVariable Long id) {
        try {
            Optional<ConteudoPlanejado> conteudo = conteudoPlanejadoService.buscarPorId(id);
            if (conteudo.isPresent()) {
                return new ResponseEntity<>(conteudo.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Listar todos os conteúdos planejados
     * 
     * @return ResponseEntity com a lista de conteúdos planejados
     */
    @GetMapping
    public ResponseEntity<List<ConteudoPlanejado>> listarTodos() {
        try {
            List<ConteudoPlanejado> conteudos = conteudoPlanejadoService.listarTodos();
            return new ResponseEntity<>(conteudos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar conteúdos planejados por planejamento
     * 
     * @param planejamentoId ID do planejamento
     * @return ResponseEntity com a lista de conteúdos planejados
     */
    @GetMapping("/planejamento/{planejamentoId}")
    public ResponseEntity<List<ConteudoPlanejado>> buscarPorPlanejamento(@PathVariable Long planejamentoId) {
        try {
            List<ConteudoPlanejado> conteudos = conteudoPlanejadoService.buscarPorPlanejamento(planejamentoId);
            return new ResponseEntity<>(conteudos, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar conteúdos planejados por data prevista
     * 
     * @param data Data prevista (formato: yyyy-MM-dd)
     * @return ResponseEntity com a lista de conteúdos planejados
     */
    @GetMapping("/data/{data}")
    public ResponseEntity<List<ConteudoPlanejado>> buscarPorData(@PathVariable String data) {
        try {
            java.time.LocalDate dataPrevista = java.time.LocalDate.parse(data);
            List<ConteudoPlanejado> conteudos = conteudoPlanejadoService.buscarPorData(dataPrevista);
            return new ResponseEntity<>(conteudos, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    public ResponseEntity<Void> deletarConteudoPlanejado(@PathVariable Long id) {
        try {
            conteudoPlanejadoService.deletar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}