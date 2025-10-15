package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.dto.ConteudoPlanejado.ConteudoPlanejadoDtoIn;
import com.davi.gestaoescolar.gestao_escolar.dto.ConteudoPlanejado.ConteudoPlanejadoDtoOut;
import com.davi.gestaoescolar.gestao_escolar.dto.Planejamento.PlanejamentoDtoOut;
import com.davi.gestaoescolar.gestao_escolar.exception.ConteudoPlanejadoException;
import com.davi.gestaoescolar.gestao_escolar.model.ConteudoPlanejado;
import com.davi.gestaoescolar.gestao_escolar.model.Planejamento;
import com.davi.gestaoescolar.gestao_escolar.repository.ConteudoPlanejadoRepository;
import com.davi.gestaoescolar.gestao_escolar.repository.PlanejamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para gerenciar operações relacionadas aos Conteúdos Planejados
 * 
 * @author Sistema de Gestão Escolar
 * @version 1.0
 */
@Service
@Transactional
public class ConteudoPlanejadoService {

    @Autowired
    private ConteudoPlanejadoRepository conteudoPlanejadoRepository;

    @Autowired
    private PlanejamentoRepository planejamentoRepository;

    /**
     * Métodos auxiliares para conversão de DTOs
     */
    private List<ConteudoPlanejadoDtoOut> toDtos(List<ConteudoPlanejado> conteudos) {
        return conteudos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ConteudoPlanejadoDtoOut toDTO(ConteudoPlanejado conteudo) {
        if (conteudo == null) {
            return null;
        }

        PlanejamentoDtoOut planejamentoDto = null;
        if (conteudo.getPlanejamento() != null) {
            try {
                Planejamento p = conteudo.getPlanejamento();
                planejamentoDto = new PlanejamentoDtoOut(
                    p.getId(),
                    p.getDescricao(),
                    p.getSemestre(),
                    p.getAno(),
                    new PlanejamentoDtoOut.DisciplinaDTO(
                        p.getDisciplina() != null ? p.getDisciplina().getId() : null,
                        p.getDisciplina() != null ? p.getDisciplina().getNome() : null
                    ),
                    new PlanejamentoDtoOut.TurmaDTO(
                        p.getTurma() != null ? p.getTurma().getId() : null,
                        p.getTurma() != null ? p.getTurma().getNome() : null
                    ),
                    p.getDataCriacao(),
                    p.getDataAtualizacao()
                );
            } catch (jakarta.persistence.EntityNotFoundException e) {
                planejamentoDto = null;
            }
        }

        return new ConteudoPlanejadoDtoOut(
            conteudo.getId(),
            conteudo.getConteudo(),
            conteudo.getDataPrevista(),
            conteudo.getDataConclusao(),
            conteudo.getConcluido(),
            conteudo.getObservacoes(),
            conteudo.getOrdemApresentacao(),
            planejamentoDto
        );
    }

    /**
     * Salvar um novo conteúdo planejado
     * 
     * @param dto Dados do conteúdo planejado a ser salvo
     * @return ConteudoPlanejadoDtoOut com os dados salvos
     * @throws ConteudoPlanejadoException.DadosInvalidosException se os dados forem inválidos
     * @throws ConteudoPlanejadoException.PlanejamentoInvalidoException se o planejamento não existir
     */
    public ConteudoPlanejadoDtoOut salvar(ConteudoPlanejadoDtoIn dto) {
        validarDadosConteudoPlanejado(dto);
        
        Planejamento planejamento = buscarPlanejamento(dto.getPlanejamentoId());
        
        ConteudoPlanejado conteudo = new ConteudoPlanejado();
        conteudo.setConteudo(dto.getConteudo());
        conteudo.setDataPrevista(dto.getDataPrevista());
        conteudo.setDataConclusao(dto.getDataConclusao());
        conteudo.setConcluido(dto.getConcluido() != null ? dto.getConcluido() : false);
        conteudo.setObservacoes(dto.getObservacoes());
        conteudo.setOrdemApresentacao(dto.getOrdemApresentacao());
        conteudo.setPlanejamento(planejamento);
        
        ConteudoPlanejado conteudoSalvo = conteudoPlanejadoRepository.save(conteudo);
        return toDTO(conteudoSalvo);
    }

    /**
     * Atualizar um conteúdo planejado existente
     * 
     * @param id ID do conteúdo planejado
     * @param dto Novos dados do conteúdo planejado
     * @return ConteudoPlanejadoDtoOut com os dados atualizados
     * @throws ConteudoPlanejadoException.ConteudoPlanejadoNaoEncontradoException se o conteúdo não for encontrado
     * @throws ConteudoPlanejadoException.DadosInvalidosException se os dados forem inválidos
     * @throws ConteudoPlanejadoException.PlanejamentoInvalidoException se o planejamento não existir
     */
    public ConteudoPlanejadoDtoOut atualizar(Long id, ConteudoPlanejadoDtoIn dto) {
        ConteudoPlanejado conteudoExistente = conteudoPlanejadoRepository.findById(id)
                .orElseThrow(() -> new ConteudoPlanejadoException.ConteudoPlanejadoNaoEncontradoException(id));
        
        validarDadosConteudoPlanejado(dto);
        
        Planejamento planejamento = buscarPlanejamento(dto.getPlanejamentoId());
        
        conteudoExistente.setConteudo(dto.getConteudo());
        conteudoExistente.setDataPrevista(dto.getDataPrevista());
        conteudoExistente.setDataConclusao(dto.getDataConclusao());
        conteudoExistente.setConcluido(dto.getConcluido() != null ? dto.getConcluido() : false);
        conteudoExistente.setObservacoes(dto.getObservacoes());
        conteudoExistente.setOrdemApresentacao(dto.getOrdemApresentacao());
        conteudoExistente.setPlanejamento(planejamento);
        
        ConteudoPlanejado conteudoAtualizado = conteudoPlanejadoRepository.save(conteudoExistente);
        return toDTO(conteudoAtualizado);
    }

    /**
     * Buscar conteúdo planejado por ID
     * 
     * @param id ID do conteúdo planejado
     * @return ConteudoPlanejadoDtoOut com os dados encontrados
     * @throws ConteudoPlanejadoException.ConteudoPlanejadoNaoEncontradoException se não encontrado
     * @throws ConteudoPlanejadoException.DadosInvalidosException se ID for nulo
     */
    public ConteudoPlanejadoDtoOut buscarPorId(Long id) {
        if (id == null) {
            throw new ConteudoPlanejadoException.DadosInvalidosException("ID não pode ser nulo");
        }
        
        ConteudoPlanejado conteudo = conteudoPlanejadoRepository.findById(id)
                .orElseThrow(() -> new ConteudoPlanejadoException.ConteudoPlanejadoNaoEncontradoException(id));
        
        return toDTO(conteudo);
    }

    /**
     * Listar todos os conteúdos planejados
     * 
     * @return Lista de ConteudoPlanejadoDtoOut
     */
    public List<ConteudoPlanejadoDtoOut> listarTodos() {
        List<ConteudoPlanejado> conteudos = conteudoPlanejadoRepository.findAll();
        return toDtos(conteudos);
    }

    /**
     * Buscar conteúdos planejados por planejamento
     * 
     * @param planejamentoId ID do planejamento
     * @return Lista de ConteudoPlanejadoDtoOut
     * @throws ConteudoPlanejadoException.DadosInvalidosException se ID for nulo
     */
    public List<ConteudoPlanejadoDtoOut> buscarPorPlanejamento(Long planejamentoId) {
        if (planejamentoId == null) {
            throw new ConteudoPlanejadoException.DadosInvalidosException("ID do planejamento não pode ser nulo");
        }
        List<ConteudoPlanejado> conteudos = conteudoPlanejadoRepository.findByPlanejamentoId(planejamentoId);
        return toDtos(conteudos);
    }

    /**
     * Buscar conteúdos planejados por data prevista
     * 
     * @param dataPrevista Data prevista
     * @return Lista de ConteudoPlanejadoDtoOut
     * @throws ConteudoPlanejadoException.DadosInvalidosException se data for nula
     */
    public List<ConteudoPlanejadoDtoOut> buscarPorData(LocalDate dataPrevista) {
        if (dataPrevista == null) {
            throw new ConteudoPlanejadoException.DadosInvalidosException("Data prevista não pode ser nula");
        }
        List<ConteudoPlanejado> conteudos = conteudoPlanejadoRepository.findByDataPrevista(dataPrevista);
        return toDtos(conteudos);
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
     * Deletar conteúdo planejado
     * 
     * @param id ID do conteúdo planejado
     * @throws ConteudoPlanejadoException.DadosInvalidosException se ID for nulo
     * @throws ConteudoPlanejadoException.ConteudoPlanejadoNaoEncontradoException se não encontrado
     */
    public void deletar(Long id) {
        if (id == null) {
            throw new ConteudoPlanejadoException.DadosInvalidosException("ID não pode ser nulo");
        }
        
        if (!conteudoPlanejadoRepository.existsById(id)) {
            throw new ConteudoPlanejadoException.ConteudoPlanejadoNaoEncontradoException(id);
        }
        
        conteudoPlanejadoRepository.deleteById(id);
    }

    /**
     * Validar dados do conteúdo planejado
     * 
     * @param dto ConteudoPlanejadoDtoIn a ser validado
     * @throws ConteudoPlanejadoException.DadosInvalidosException se dados inválidos
     */
    private void validarDadosConteudoPlanejado(ConteudoPlanejadoDtoIn dto) {
        if (dto == null) {
            throw new ConteudoPlanejadoException.DadosInvalidosException("Dados do conteúdo planejado não podem ser nulos");
        }
        
        if (dto.getConteudo() == null || dto.getConteudo().trim().isEmpty()) {
            throw new ConteudoPlanejadoException.DadosInvalidosException("Conteúdo não pode ser vazio");
        }
        
        if (dto.getDataPrevista() == null) {
            throw new ConteudoPlanejadoException.DadosInvalidosException("Data prevista não pode ser nula");
        }
        
        if (dto.getPlanejamentoId() == null) {
            throw new ConteudoPlanejadoException.DadosInvalidosException("ID do planejamento não pode ser nulo");
        }
        
        // Validar se data de conclusão não é anterior à data prevista
        if (dto.getDataConclusao() != null && dto.getDataPrevista() != null) {
            if (dto.getDataConclusao().isBefore(dto.getDataPrevista())) {
                throw new ConteudoPlanejadoException.DadosInvalidosException("Data de conclusão não pode ser anterior à data prevista");
            }
        }
        
        // Validar ordem de apresentação
        if (dto.getOrdemApresentacao() != null && dto.getOrdemApresentacao() <= 0) {
            throw new ConteudoPlanejadoException.DadosInvalidosException("Ordem de apresentação deve ser maior que zero");
        }
    }
    
    /**
     * Buscar planejamento por ID
     * 
     * @param planejamentoId ID do planejamento
     * @return Planejamento encontrado
     * @throws ConteudoPlanejadoException.PlanejamentoInvalidoException se não encontrado
     */
    private Planejamento buscarPlanejamento(Long planejamentoId) {
        return planejamentoRepository.findById(planejamentoId)
                .orElseThrow(() -> new ConteudoPlanejadoException.PlanejamentoInvalidoException(planejamentoId));
    }

    /**
     * Altera o status de conclusão do conteúdo planejado (0 ou 1)
     */
    public ConteudoPlanejadoDtoOut alterarConclusao(Long id, Integer concluido) {
        if (id == null) {
            throw new ConteudoPlanejadoException.DadosInvalidosException("ID não pode ser nulo");
        }

        ConteudoPlanejado conteudo = conteudoPlanejadoRepository.findById(id)
                .orElseThrow(() -> new ConteudoPlanejadoException.ConteudoPlanejadoNaoEncontradoException(id));

        boolean valor = concluido != null && concluido.equals(1);
        conteudo.setConcluido(valor);

        ConteudoPlanejado atualizado = conteudoPlanejadoRepository.save(conteudo);
        return toDTO(atualizado);
    }
}