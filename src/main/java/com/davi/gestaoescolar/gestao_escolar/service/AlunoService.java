package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.model.Aluno;
import com.davi.gestaoescolar.gestao_escolar.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    /**
     * Salva um novo aluno
     */
    public Aluno salvar(Aluno aluno) {
        // Validações básicas
        validarDadosAluno(aluno);
        
        // Verificar se CPF já existe
        if (cpfJaExiste(aluno.getCpf())) {
            throw new RuntimeException("CPF já cadastrado: " + aluno.getCpf());
        }
        
        aluno.setAtivo(true);
        return alunoRepository.save(aluno);
    }

    /**
     * Atualiza um aluno existente
     */
    public Aluno atualizar(Aluno aluno) {
        if (aluno.getId() == null) {
            throw new IllegalArgumentException("ID do aluno não pode ser nulo para atualização");
        }
        
        Optional<Aluno> alunoExistente = alunoRepository.findById(aluno.getId());
        if (alunoExistente.isEmpty()) {
            throw new RuntimeException("Aluno não encontrado com ID: " + aluno.getId());
        }
        
        // Verificar se o novo CPF já existe (se foi alterado)
        Aluno alunoAtual = alunoExistente.get();
        if (!alunoAtual.getCpf().equals(aluno.getCpf()) && 
            cpfJaExiste(aluno.getCpf())) {
            throw new RuntimeException("CPF já cadastrado: " + aluno.getCpf());
        }
        
        validarDadosAluno(aluno);
        return alunoRepository.save(aluno);
    }

    /**
     * Busca aluno por ID
     */
    public Optional<Aluno> buscarPorId(Long id) {
        return alunoRepository.findById(id);
    }

    /**
     * Busca aluno por CPF
     */
    public Optional<Aluno> buscarPorCpf(String cpf) {
        return alunoRepository.findByCpf(cpf);
    }

    /**
     * Busca alunos por nome (busca parcial)
     */
    public List<Aluno> buscarPorNome(String nome) {
        return alunoRepository.findByNomeContainingIgnoreCase(nome);
    }

    /**
     * Lista todos os alunos
     */
    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    /**
     * Lista apenas alunos ativos
     */
    public List<Aluno> listarAtivos() {
        return alunoRepository.findByAtivoTrue();
    }

    /**
     * Desativa um aluno (soft delete)
     */
    public void desativar(Long id) {
        Optional<Aluno> aluno = alunoRepository.findById(id);
        if (aluno.isPresent()) {
            aluno.get().setAtivo(false);
            alunoRepository.save(aluno.get());
        } else {
            throw new RuntimeException("Aluno não encontrado com ID: " + id);
        }
    }

    /**
     * Ativa um aluno
     */
    public void ativar(Long id) {
        Optional<Aluno> aluno = alunoRepository.findById(id);
        if (aluno.isPresent()) {
            aluno.get().setAtivo(true);
            alunoRepository.save(aluno.get());
        } else {
            throw new RuntimeException("Aluno não encontrado com ID: " + id);
        }
    }

    /**
     * Remove permanentemente um aluno
     */
    public void deletar(Long id) {
        if (!alunoRepository.existsById(id)) {
            throw new RuntimeException("Aluno não encontrado com ID: " + id);
        }
        alunoRepository.deleteById(id);
    }

    /**
     * Verifica se CPF já existe
     */
    public boolean cpfJaExiste(String cpf) {
        return alunoRepository.findByCpf(cpf).isPresent();
    }

    /**
     * Validações dos dados do aluno
     */
    private void validarDadosAluno(Aluno aluno) {
        if (aluno.getNome() == null || aluno.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        
        if (aluno.getNome().length() < 2) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres");
        }
        
        if (aluno.getCpf() == null || aluno.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório");
        }
        
        if (!validarFormatoCpf(aluno.getCpf())) {
            throw new IllegalArgumentException("CPF deve ter formato válido (XXX.XXX.XXX-XX)");
        }
        
        if (aluno.getDataNascimento() == null) {
            throw new IllegalArgumentException("Data de nascimento é obrigatória");
        }
        
        if (aluno.getDataNascimento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de nascimento não pode ser futura");
        }
        
        // Verificar idade mínima (3 anos) e máxima (25 anos)
        LocalDate hoje = LocalDate.now();
        int idade = hoje.getYear() - aluno.getDataNascimento().getYear();
        if (aluno.getDataNascimento().plusYears(idade).isAfter(hoje)) {
            idade--;
        }
        
        if (idade < 3) {
            throw new IllegalArgumentException("Aluno deve ter pelo menos 3 anos");
        }
        
        if (idade > 25) {
            throw new IllegalArgumentException("Aluno não pode ter mais de 25 anos");
        }
    }

    /**
     * Valida formato do CPF
     */
    private boolean validarFormatoCpf(String cpf) {
        if (cpf == null) return false;
        // Remove pontos e traços
        String cpfLimpo = cpf.replaceAll("[.\\-]", "");
        // Verifica se tem 11 dígitos
        return cpfLimpo.matches("\\d{11}");
    }
}