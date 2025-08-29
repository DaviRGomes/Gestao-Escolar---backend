package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.model.Responsavel;
import com.davi.gestaoescolar.gestao_escolar.model.enums.Perfil;
import com.davi.gestaoescolar.gestao_escolar.repository.ResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional
public class ResponsavelService {

    @Autowired
    private ResponsavelRepository responsavelRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        
        // Verificar se email já existe
        if (usuarioService.emailJaExiste(responsavel.getEmail())) {
            throw new RuntimeException("Email já cadastrado: " + responsavel.getEmail());
        }
        
        // Configurar dados do usuário
        responsavel.setPerfil(Perfil.RESPONSAVEL);
        responsavel.setAtivo(true);
        
        // Criptografar senha
        if (responsavel.getSenha() != null) {
            responsavel.setSenha(passwordEncoder.encode(responsavel.getSenha()));
        }
        
        return responsavelRepository.save(responsavel);
    }

    /**
     * Atualiza um responsável existente
     */
    public Responsavel atualizar(Responsavel responsavel) {
        if (responsavel.getId() == null) {
            throw new IllegalArgumentException("ID do responsável não pode ser nulo para atualização");
        }
        
        Optional<Responsavel> responsavelExistente = responsavelRepository.findById(responsavel.getId());
        if (responsavelExistente.isEmpty()) {
            throw new RuntimeException("Responsável não encontrado com ID: " + responsavel.getId());
        }
        
        // Verificar se o novo CPF já existe (se foi alterado)
        Responsavel responsavelAtual = responsavelExistente.get();
        if (!responsavelAtual.getCpf().equals(responsavel.getCpf()) && 
            cpfJaExiste(responsavel.getCpf())) {
            throw new RuntimeException("CPF já cadastrado: " + responsavel.getCpf());
        }
        
        // Verificar se o novo email já existe (se foi alterado)
        if (!responsavelAtual.getEmail().equals(responsavel.getEmail()) && 
            usuarioService.emailJaExiste(responsavel.getEmail())) {
            throw new RuntimeException("Email já cadastrado: " + responsavel.getEmail());
        }
        
        validarDadosResponsavel(responsavel);
        
        // Se a senha foi alterada, criptografar
        if (responsavel.getSenha() != null && !responsavel.getSenha().isEmpty()) {
            responsavel.setSenha(passwordEncoder.encode(responsavel.getSenha()));
        } else {
            // Manter a senha existente se não foi fornecida nova senha
            responsavel.setSenha(responsavelExistente.get().getSenha());
        }
        
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
     * Busca responsável por email
     */
    public Optional<Responsavel> buscarPorEmail(String email) {
        return responsavelRepository.findByEmail(email);
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
     * Busca responsáveis por parentesco
     */
    public List<Responsavel> buscarPorParentesco(String parentesco) {
        return responsavelRepository.findByParentescoContainingIgnoreCase(parentesco);
    }

    /**
     * Lista todos os responsáveis
     */
    public List<Responsavel> listarTodos() {
        return responsavelRepository.findAll();
    }

    /**
     * Desativa um responsável (soft delete)
     */
    public void desativar(Long id) {
        Optional<Responsavel> responsavel = responsavelRepository.findById(id);
        if (responsavel.isPresent()) {
            responsavel.get().setAtivo(false);
            responsavelRepository.save(responsavel.get());
        } else {
            throw new RuntimeException("Responsável não encontrado com ID: " + id);
        }
    }

    /**
     * Reativa um responsável
     */
    public void reativar(Long id) {
        Optional<Responsavel> responsavel = responsavelRepository.findById(id);
        if (responsavel.isPresent()) {
            responsavel.get().setAtivo(true);
            responsavelRepository.save(responsavel.get());
        } else {
            throw new RuntimeException("Responsável não encontrado com ID: " + id);
        }
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
     * Altera senha do responsável
     */
    public void alterarSenha(Long id, String novaSenha) {
        Optional<Responsavel> responsavel = responsavelRepository.findById(id);
        if (responsavel.isPresent()) {
            responsavel.get().setSenha(passwordEncoder.encode(novaSenha));
            responsavelRepository.save(responsavel.get());
        } else {
            throw new RuntimeException("Responsável não encontrado com ID: " + id);
        }
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
        
        if (responsavel.getEmail() == null || responsavel.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        
        if (!isEmailValido(responsavel.getEmail())) {
            throw new IllegalArgumentException("Email inválido");
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
        
        if (responsavel.getSenha() != null && !responsavel.getSenha().isEmpty()) {
            if (responsavel.getSenha().length() < 6) {
                throw new IllegalArgumentException("Senha deve ter pelo menos 6 caracteres");
            }
        }
    }

    /**
     * Valida formato do email
     */
    private boolean isEmailValido(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.matches(emailRegex, email);
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