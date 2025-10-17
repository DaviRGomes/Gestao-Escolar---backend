package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.dto.Responsavel.ResponsavelDtoIn;
import com.davi.gestaoescolar.gestao_escolar.dto.Responsavel.ResponsavelDtoOut;
import com.davi.gestaoescolar.gestao_escolar.dto.Aluno.AlunoDtoOut;
import com.davi.gestaoescolar.gestao_escolar.model.Aluno;
import com.davi.gestaoescolar.gestao_escolar.model.AlunoResponsavel;
import com.davi.gestaoescolar.gestao_escolar.model.Responsavel;

import com.davi.gestaoescolar.gestao_escolar.repository.ResponsavelRepository;
import com.davi.gestaoescolar.gestao_escolar.repository.AlunoRepository;
import com.davi.gestaoescolar.gestao_escolar.repository.AlunoResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class ResponsavelService {

    @Autowired
    private ResponsavelRepository responsavelRepository;
    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private AlunoResponsavelRepository alunoResponsavelRepository;


    // Conversão de entidade -> DTO de saída
    private ResponsavelDtoOut toDTO(Responsavel responsavel) {
        List<AlunoDtoOut> alunosDTO = responsavel.getResponsaveis().stream()
            .map(ar -> new AlunoDtoOut(
                ar.getAluno() != null ? ar.getAluno().getId() : null,
                ar.getAluno() != null ? ar.getAluno().getNome() : null
            ))
            .collect(Collectors.toList());

        return new ResponsavelDtoOut(
            responsavel.getId(),
            responsavel.getNome(),
            responsavel.getTelefone(),
            responsavel.getCpf(),
            responsavel.getParentesco(),
            alunosDTO
        );
    }


    /**
     * Salva um novo responsável
     */
    public ResponsavelDtoOut salvar(ResponsavelDtoIn dtoIn) {
        if (cpfJaExiste(dtoIn.getCpf())) {
            throw new RuntimeException("CPF já cadastrado: " + dtoIn.getCpf());
        }
        Responsavel responsavel = new Responsavel(
            dtoIn.getNome(),
            dtoIn.getTelefone(),
            dtoIn.getCpf(),
            dtoIn.getParentesco()
        );
        validarDadosResponsavel(dtoIn);
        Responsavel salvo = responsavelRepository.save(responsavel);
        return toDTO(salvo);
    }

    /**
     * Atualiza um responsável existente
     */
    public ResponsavelDtoOut atualizar(Long id, ResponsavelDtoIn dtoIn) {
        Responsavel atual = responsavelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Responsável não encontrado com ID: " + id));

        if (dtoIn.getCpf() != null && !dtoIn.getCpf().equals(atual.getCpf()) && cpfJaExiste(dtoIn.getCpf())) {
            throw new RuntimeException("CPF já cadastrado: " + dtoIn.getCpf());
        }

        if (dtoIn.getNome() != null) atual.setNome(dtoIn.getNome());
        if (dtoIn.getTelefone() != null) atual.setTelefone(dtoIn.getTelefone());
        if (dtoIn.getCpf() != null) atual.setCpf(dtoIn.getCpf());
        if (dtoIn.getParentesco() != null) atual.setParentesco(dtoIn.getParentesco());

        validarDadosResponsavel(dtoIn);
        Responsavel salvo = responsavelRepository.save(atual);
        return toDTO(salvo);
    }

    /**
     * Busca responsável por ID
     */
    @Transactional(readOnly = true)
    public Optional<ResponsavelDtoOut> buscarPorId(Long id) {
        return responsavelRepository.findById(id).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<ResponsavelDtoOut> buscarPorCpf(String cpf) {
        return responsavelRepository.findByCpf(cpf).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public List<ResponsavelDtoOut> listarTodos() {
        return responsavelRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ResponsavelDtoOut> buscarPorNome(String nome) {
        return responsavelRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ResponsavelDtoOut> buscarPorTelefone(String telefone) {
        return responsavelRepository.findByTelefoneContaining(telefone).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    /**
     * Remove permanentemente um responsável
     */
    public void deletar(Long id) {
        if (!responsavelRepository.existsById(id)) {
            throw new RuntimeException("Responsável não encontrado com ID: " + id);
        }
        responsavelRepository.deleteById(id);
    }

    /**
     * Verifica se CPF já existe
     */
    public boolean cpfJaExiste(String cpf) {
        return responsavelRepository.findByCpf(cpf).isPresent();
    }

    /**
     * Validações dos dados do responsável
     */
    private void validarDadosResponsavel(ResponsavelDtoIn dtoIn) {
        if (dtoIn.getNome() == null) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        
        if (dtoIn.getNome().length() < 2) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres");
        }
        
        if (dtoIn.getCpf() == null) {
            throw new IllegalArgumentException("CPF é obrigatório");
        }
        
        if (dtoIn.getTelefone() != null && !dtoIn.getTelefone().trim().isEmpty()) {
            if (!isTelefoneValido(dtoIn.getTelefone())) {
                throw new IllegalArgumentException("Telefone inválido");
            }
        }
    }
    /**
     * Valida telefone brasileiro
     */
    private boolean isTelefoneValido(String telefone) {
        // Remove caracteres não numéricos
        String telefoneNumerico = telefone.replaceAll("[^0-9]", "");
        
        // Verifica se tem 10 ou 11 dígitos (com DDD)
        return telefoneNumerico.length() == 10 || telefoneNumerico.length() == 11;
    }

    public ResponsavelDtoOut adicionarAluno(Long responsavelId, Long alunoId, Boolean principal) {
        Responsavel responsavel = responsavelRepository.findById(responsavelId)
            .orElseThrow(() -> new RuntimeException("Responsável não encontrado com ID: " + responsavelId));

        Aluno aluno = alunoRepository.findById(alunoId)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + alunoId));

        Optional<AlunoResponsavel> relacaoExistente =
            alunoResponsavelRepository.findByAlunoIdAndResponsavelId(alunoId, responsavelId);
        if (relacaoExistente.isPresent()) {
            throw new RuntimeException("Relacionamento já existe entre este aluno e responsável");
        }

        Boolean isPrincipal = principal != null ? principal : false;

        if (isPrincipal) {
            List<AlunoResponsavel> responsaveisPrincipais =
                alunoResponsavelRepository.findByAlunoIdAndPrincipalTrue(alunoId);
            if (!responsaveisPrincipais.isEmpty()) {
                throw new RuntimeException("Aluno já possui um responsável principal");
            }
        }

        AlunoResponsavel alunoResponsavel = new AlunoResponsavel();
        alunoResponsavel.setAluno(aluno);
        alunoResponsavel.setResponsavel(responsavel);
        alunoResponsavel.setPrincipal(isPrincipal);

        alunoResponsavelRepository.save(alunoResponsavel);

        // Mantém o objeto em memória consistente
        responsavel.getResponsaveis().add(alunoResponsavel);

        return toDTO(responsavel);
    }
}