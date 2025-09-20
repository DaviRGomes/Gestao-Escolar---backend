package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.model.ConteudoPlanejado;
import com.davi.gestaoescolar.gestao_escolar.model.Planejamento;
import com.davi.gestaoescolar.gestao_escolar.repository.ConteudoPlanejadoRepository;
import com.davi.gestaoescolar.gestao_escolar.repository.PlanejamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service para gerenciar operações relacionadas aos Conteúdos Planejados
 * 
 * @author Sistema de Gestão Escolar
 * @version 1.0
 */
@Service
public class ConteudoPlanejadoService {

    @Autowired
    private ConteudoPlanejadoRepository conteudoPlanejadoRepository;

    @Autowired
    private PlanejamentoRepository planejamentoRepository;

    /**
     * Salvar um novo conteúdo planejado
     * 
     * @param conteudoPlanejado Conteúdo planejado a ser salvo
     * @return Conteúdo planejado salvo
     * @throws IllegalArgumentException se os dados forem inválidos
     * @throws RuntimeException se o planejamento não existir
     */
    public ConteudoPlanejado salvar(ConteudoPlanejado conteudoPlanejado) {
        validarConteudoPlanejado(conteudoPlanejado);
        
        // Verificar se o planejamento existe
        if (conteudoPlanejado.getPlanejamento() != null && conteudoPlanejado.getPlanejamento().getId() != null) {
            Optional<Planejamento> planejamento = planejamentoRepository.findById(conteudoPlanejado.getPlanejamento().getId());
            if (!planejamento.isPresent()) {
                throw new RuntimeException("Planejamento não encontrado com ID: " + conteudoPlanejado.getPlanejamento().getId());
            }
            conteudoPlanejado.setPlanejamento(planejamento.get());
        }
        
        return conteudoPlanejadoRepository.save(conteudoPlanejado);
    }

    /**
     * Atualizar um conteúdo planejado existente
     * 
     * @param id ID do conteúdo planejado
     * @param conteudoPlanejado Novos dados do conteúdo planejado
     * @return Conteúdo planejado atualizado
     * @throws RuntimeException se o conteúdo planejado não for encontrado
     */
    public ConteudoPlanejado atualizar(Long id, ConteudoPlanejado conteudoPlanejado) {
        Optional<ConteudoPlanejado> conteudoExistente = conteudoPlanejadoRepository.findById(id);
        if (!conteudoExistente.isPresent()) {
            throw new RuntimeException("Conteúdo planejado não encontrado com ID: " + id);
        }
        
        validarConteudoPlanejado(conteudoPlanejado);
        
        ConteudoPlanejado conteudoAtualizado = conteudoExistente.get();
        conteudoAtualizado.setConteudo(conteudoPlanejado.getConteudo());
        conteudoAtualizado.setDataPrevista(conteudoPlanejado.getDataPrevista());
        conteudoAtualizado.setDataConclusao(conteudoPlanejado.getDataConclusao());
        conteudoAtualizado.setConcluido(conteudoPlanejado.getConcluido());
        conteudoAtualizado.setObservacoes(conteudoPlanejado.getObservacoes());
        conteudoAtualizado.setOrdemApresentacao(conteudoPlanejado.getOrdemApresentacao());
        
        // Atualizar planejamento se fornecido
        if (conteudoPlanejado.getPlanejamento() != null && conteudoPlanejado.getPlanejamento().getId() != null) {
            Optional<Planejamento> planejamento = planejamentoRepository.findById(conteudoPlanejado.getPlanejamento().getId());
            if (!planejamento.isPresent()) {
                throw new RuntimeException("Planejamento não encontrado com ID: " + conteudoPlanejado.getPlanejamento().getId());
            }
            conteudoAtualizado.setPlanejamento(planejamento.get());
        }
        
        return conteudoPlanejadoRepository.save(conteudoAtualizado);
    }

    /**
     * Buscar conteúdo planejado por ID
     * 
     * @param id ID do conteúdo planejado
     * @return Optional com o conteúdo planejado encontrado
     */
    public Optional<ConteudoPlanejado> buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        return conteudoPlanejadoRepository.findById(id);
    }

    /**
     * Listar todos os conteúdos planejados
     * 
     * @return Lista de conteúdos planejados
     */
    public List<ConteudoPlanejado> listarTodos() {
        return conteudoPlanejadoRepository.findAll();
    }

    /**
     * Buscar conteúdos planejados por planejamento
     * 
     * @param planejamentoId ID do planejamento
     * @return Lista de conteúdos planejados
     */
    public List<ConteudoPlanejado> buscarPorPlanejamento(Long planejamentoId) {
        if (planejamentoId == null) {
            throw new IllegalArgumentException("ID do planejamento não pode ser nulo");
        }
        return conteudoPlanejadoRepository.findByPlanejamentoId(planejamentoId);
    }

    /**
     * Buscar conteúdos planejados por data prevista
     * 
     * @param dataPrevista Data prevista
     * @return Lista de conteúdos planejados
     */
    public List<ConteudoPlanejado> buscarPorData(java.time.LocalDate dataPrevista) {
        if (dataPrevista == null) {
            throw new IllegalArgumentException("Data prevista não pode ser nula");
        }
        return conteudoPlanejadoRepository.findByDataPrevista(dataPrevista);
    }

    // /**
    //  * Buscar conteúdos planejados por turma
    //  * 
    //  * @param turmaId ID da turma
    //  * @return Lista de conteúdos planejados
    //  */
    // public List<ConteudoPlanejado> buscarPorTurma(Long turmaId) {
    //     if (turmaId == null) {
    //         throw new IllegalArgumentException("ID da turma não pode ser nulo");
    //     }
    //     return conteudoPlanejadoRepository.findByPlanejamento_Turma_Id(turmaId);
    // }

    /**
     * Deletar um conteúdo planejado
     * 
     * @param id ID do conteúdo planejado a ser deletado
     * @throws RuntimeException se o conteúdo planejado não for encontrado
     */
    public void deletar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        
        Optional<ConteudoPlanejado> conteudo = conteudoPlanejadoRepository.findById(id);
        if (!conteudo.isPresent()) {
            throw new RuntimeException("Conteúdo planejado não encontrado com ID: " + id);
        }
        
        conteudoPlanejadoRepository.deleteById(id);
    }

    /**
     * Validar dados do conteúdo planejado
     * 
     * @param conteudoPlanejado Conteúdo planejado a ser validado
     * @throws IllegalArgumentException se os dados forem inválidos
     */
    private void validarConteudoPlanejado(ConteudoPlanejado conteudoPlanejado) {
        if (conteudoPlanejado == null) {
            throw new IllegalArgumentException("Conteúdo planejado não pode ser nulo");
        }
        
        if (conteudoPlanejado.getConteudo() == null || conteudoPlanejado.getConteudo().trim().isEmpty()) {
            throw new IllegalArgumentException("Conteúdo é obrigatório");
        }
        
        if (conteudoPlanejado.getPlanejamento() == null) {
            throw new IllegalArgumentException("Planejamento é obrigatório");
        }
    }
}