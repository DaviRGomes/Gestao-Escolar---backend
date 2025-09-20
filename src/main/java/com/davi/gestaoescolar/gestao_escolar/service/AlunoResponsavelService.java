package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.model.Aluno;
import com.davi.gestaoescolar.gestao_escolar.model.AlunoResponsavel;
import com.davi.gestaoescolar.gestao_escolar.model.Responsavel;
import com.davi.gestaoescolar.gestao_escolar.repository.AlunoRepository;
import com.davi.gestaoescolar.gestao_escolar.repository.AlunoResponsavelRepository;
import com.davi.gestaoescolar.gestao_escolar.repository.ResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AlunoResponsavelService {

    @Autowired
    private AlunoResponsavelRepository alunoResponsavelRepository;
    
    @Autowired
    private AlunoRepository alunoRepository;
    
    @Autowired
    private ResponsavelRepository responsavelRepository;

    /**
     * Cadastra um novo relacionamento aluno-responsável
     */
    public AlunoResponsavel cadastrar(AlunoResponsavel alunoResponsavel) {
        // Validações básicas
        validarDadosAlunoResponsavel(alunoResponsavel);
        
        // Verificar se aluno existe
        if (!alunoRepository.existsById(alunoResponsavel.getAluno().getId())) {
            throw new RuntimeException("Aluno não encontrado com ID: " + alunoResponsavel.getAluno().getId());
        }
        
        // Verificar se responsável existe
        if (!responsavelRepository.existsById(alunoResponsavel.getResponsavel().getId())) {
            throw new RuntimeException("Responsável não encontrado com ID: " + alunoResponsavel.getResponsavel().getId());
        }
        
        // Verificar se já existe relacionamento entre aluno e responsável
        Optional<AlunoResponsavel> relacionamentoExistente = alunoResponsavelRepository
            .findByAlunoIdAndResponsavelId(alunoResponsavel.getAluno().getId(), alunoResponsavel.getResponsavel().getId());
        
        if (relacionamentoExistente.isPresent()) {
            throw new RuntimeException("Relacionamento já existe entre este aluno e responsável");
        }
        
        // Se for marcado como principal, verificar se já existe um responsável principal para o aluno
        if (alunoResponsavel.getPrincipal() != null && alunoResponsavel.getPrincipal()) {
            List<AlunoResponsavel> responsaveisPrincipais = alunoResponsavelRepository
                .findByAlunoIdAndPrincipalTrue(alunoResponsavel.getAluno().getId());
            
            if (!responsaveisPrincipais.isEmpty()) {
                throw new RuntimeException("Aluno já possui um responsável principal");
            }
        }
        
        return alunoResponsavelRepository.save(alunoResponsavel);
    }

    /**
     * Busca relacionamento por ID
     */
    public Optional<AlunoResponsavel> buscarPorId(Long id) {
        return alunoResponsavelRepository.findById(id);
    }

    /**
     * Busca todos os responsáveis de um aluno
     */
    public List<AlunoResponsavel> buscarPorAlunoId(Long alunoId) {
        return alunoResponsavelRepository.findByAlunoId(alunoId);
    }

    /**
     * Busca todos os alunos de um responsável
     */
    public List<AlunoResponsavel> buscarPorResponsavelId(Long responsavelId) {
        return alunoResponsavelRepository.findByResponsavelId(responsavelId);
    }

    /**
     * Lista todos os relacionamentos
     */
    public List<AlunoResponsavel> listarTodos() {
        return alunoResponsavelRepository.findAll();
    }

    /**
     * Busca responsáveis principais
     */
    public List<AlunoResponsavel> buscarResponsaveisPrincipais() {
        return alunoResponsavelRepository.findByPrincipalTrue();
    }

    /**
     * Busca responsável principal de um aluno
     */
    public List<AlunoResponsavel> buscarResponsavelPrincipalPorAluno(Long alunoId) {
        return alunoResponsavelRepository.findByAlunoIdAndPrincipalTrue(alunoId);
    }

    /**
     * Atualiza um relacionamento
     */
    public AlunoResponsavel atualizar(Long id, AlunoResponsavel alunoResponsavelAtualizado) {
        Optional<AlunoResponsavel> relacionamentoExistente = alunoResponsavelRepository.findById(id);
        
        if (relacionamentoExistente.isEmpty()) {
            throw new RuntimeException("Relacionamento não encontrado com ID: " + id);
        }
        
        AlunoResponsavel relacionamento = relacionamentoExistente.get();
        
        // Validar se está tentando alterar para principal e já existe um principal
        if (alunoResponsavelAtualizado.getPrincipal() != null && 
            alunoResponsavelAtualizado.getPrincipal() && 
            !relacionamento.getPrincipal()) {
            
            List<AlunoResponsavel> responsaveisPrincipais = alunoResponsavelRepository
                .findByAlunoIdAndPrincipalTrue(relacionamento.getAluno().getId());
            
            if (!responsaveisPrincipais.isEmpty()) {
                throw new RuntimeException("Aluno já possui um responsável principal");
            }
        }
        
        // Atualizar apenas o campo principal (não permitir alterar aluno/responsável)
        relacionamento.setPrincipal(alunoResponsavelAtualizado.getPrincipal());
        
        return alunoResponsavelRepository.save(relacionamento);
    }

    /**
     * Deleta um relacionamento
     */
    public void deletar(Long id) {
        if (!alunoResponsavelRepository.existsById(id)) {
            throw new RuntimeException("Relacionamento não encontrado com ID: " + id);
        }
        alunoResponsavelRepository.deleteById(id);
    }

    /**
     * Valida os dados do relacionamento aluno-responsável
     */
    private void validarDadosAlunoResponsavel(AlunoResponsavel alunoResponsavel) {
        if (alunoResponsavel == null) {
            throw new RuntimeException("Dados do relacionamento são obrigatórios");
        }
        
        if (alunoResponsavel.getAluno() == null || alunoResponsavel.getAluno().getId() == null) {
            throw new RuntimeException("Aluno é obrigatório");
        }
        
        if (alunoResponsavel.getResponsavel() == null || alunoResponsavel.getResponsavel().getId() == null) {
            throw new RuntimeException("Responsável é obrigatório");
        }
        
        // Se principal não foi definido, definir como false
        if (alunoResponsavel.getPrincipal() == null) {
            alunoResponsavel.setPrincipal(false);
        }
    }
}