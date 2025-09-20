package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.model.Responsavel;

import com.davi.gestaoescolar.gestao_escolar.repository.ResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class ResponsavelService {

    @Autowired
    private ResponsavelRepository responsavelRepository;



    /**
     * Salva um novo responsável
     */
    public Responsavel salvar(Responsavel responsavel) {
        // Validações
        validarDadosResponsavel(responsavel);
        
        // Verificar se CPF já existe
        if (cpfJaExiste(responsavel.getCpf())) {
            throw new RuntimeException("CPF já cadastrado: " + responsavel.getCpf());
        }
        
        return responsavelRepository.save(responsavel);
    }

    /**
     * Atualiza um responsável existente
     */
    public Responsavel atualizar(Long id, Responsavel responsavel) {
        // Verificar se responsável existe
        Optional<Responsavel> responsavelExistente = responsavelRepository.findById(responsavel.getId());
        if (!responsavelExistente.isPresent()) {
            throw new RuntimeException("Responsável não encontrado com ID: " + responsavel.getId());
        }
        
        // Verificar se o novo CPF já existe (se foi alterado)
        Responsavel responsavelAtual = responsavelExistente.get();
        if (!responsavelAtual.getCpf().equals(responsavel.getCpf()) && 
            cpfJaExiste(responsavel.getCpf())) {
            throw new RuntimeException("CPF já cadastrado: " + responsavel.getCpf());
        }
    
        validarDadosResponsavel(responsavel);
        
        return responsavelRepository.save(responsavel);
    }

    /**
     * Busca responsável por ID
     */
    public Optional<Responsavel> buscarPorId(Long id) {
        return responsavelRepository.findById(id);
    }

    /**
     * Busca responsável por CPF
     */
    public Optional<Responsavel> buscarPorCpf(String cpf) {
        return responsavelRepository.findByCpf(cpf);
    }



    /**
     * Busca responsáveis por nome (busca parcial)
     */
    public List<Responsavel> buscarPorNome(String nome) {
        return responsavelRepository.findByNomeContainingIgnoreCase(nome);
    }

    /**
     * Busca responsáveis por telefone (busca parcial)
     */
    public List<Responsavel> buscarPorTelefone(String telefone) {
        return responsavelRepository.findByTelefoneContaining(telefone);
    }


    /**
     * Lista todos os responsáveis
     */
    public List<Responsavel> listarTodos() {
        return responsavelRepository.findAll();
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
    private void validarDadosResponsavel(Responsavel responsavel) {
        if (responsavel.getNome() == null || responsavel.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        
        if (responsavel.getNome().length() < 2) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres");
        }
        
        if (responsavel.getCpf() == null || responsavel.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório");
        }
        
        if (!isCpfValido(responsavel.getCpf())) {
            throw new IllegalArgumentException("CPF inválido");
        }
        
        if (responsavel.getTelefone() != null && !responsavel.getTelefone().trim().isEmpty()) {
            if (!isTelefoneValido(responsavel.getTelefone())) {
                throw new IllegalArgumentException("Telefone inválido");
            }
        }
    }

    /**
     * Valida CPF
     */
    private boolean isCpfValido(String cpf) {
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");
        
        // Verifica se tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }
        
        // Verifica se todos os dígitos são iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        
        // Validação do algoritmo do CPF
        try {
            int[] digits = new int[11];
            for (int i = 0; i < 11; i++) {
                digits[i] = Integer.parseInt(cpf.substring(i, i + 1));
            }
            
            // Primeiro dígito verificador
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += digits[i] * (10 - i);
            }
            int firstDigit = 11 - (sum % 11);
            if (firstDigit >= 10) firstDigit = 0;
            
            // Segundo dígito verificador
            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += digits[i] * (11 - i);
            }
            int secondDigit = 11 - (sum % 11);
            if (secondDigit >= 10) secondDigit = 0;
            
            return digits[9] == firstDigit && digits[10] == secondDigit;
        } catch (NumberFormatException e) {
            return false;
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
}