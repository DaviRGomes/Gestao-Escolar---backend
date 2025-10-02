package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.model.Secretaria;
import com.davi.gestaoescolar.gestao_escolar.model.enums.Perfil;
import com.davi.gestaoescolar.gestao_escolar.repository.SecretariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional
public class SecretariaService {

    @Autowired
    private SecretariaRepository secretariaRepository;

    // Padrões de validação
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final Pattern CPF_PATTERN = Pattern.compile("^\\d{11}$");
    private static final Pattern TELEFONE_PATTERN = Pattern.compile("^\\d{10,11}$");

    // ==================== OPERAÇÕES CRUD ====================

    /**
     * Salvar nova secretaria
     */
    public Secretaria salvar(Secretaria secretaria) {
        validarDadosObrigatorios(secretaria);
        validarDadosUnicos(secretaria);
        validarFormatos(secretaria);
        validarIdades(secretaria);
        
        // Configurações padrão
        if (secretaria.getDataContratacao() == null) {
            secretaria.setDataContratacao(LocalDate.now());
        }
        secretaria.setPerfil(Perfil.SECRETARIA);
        secretaria.setAtivo(true);
        
        return secretariaRepository.save(secretaria);
    }

    /**
     * Atualizar secretaria existente
     */
    public Secretaria atualizar(Long id, Secretaria secretariaAtualizada) {
        Secretaria secretariaExistente = buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Secretaria não encontrada com ID: " + id));

        validarDadosObrigatorios(secretariaAtualizada);
        validarDadosUnicosParaAtualizacao(secretariaAtualizada, id);
        validarFormatos(secretariaAtualizada);
        validarIdades(secretariaAtualizada);

        // Atualizar campos
        secretariaExistente.setNome(secretariaAtualizada.getNome());
        secretariaExistente.setEmail(secretariaAtualizada.getEmail());
        secretariaExistente.setCpf(secretariaAtualizada.getCpf());
        secretariaExistente.setTelefone(secretariaAtualizada.getTelefone());
        secretariaExistente.setDataNascimento(secretariaAtualizada.getDataNascimento());
        secretariaExistente.setEndereco(secretariaAtualizada.getEndereco());
        secretariaExistente.setCargo(secretariaAtualizada.getCargo());
        secretariaExistente.setMatricula(secretariaAtualizada.getMatricula());
        
        if (secretariaAtualizada.getSenha() != null && !secretariaAtualizada.getSenha().isEmpty()) {
            secretariaExistente.setSenha(secretariaAtualizada.getSenha());
        }

        return secretariaRepository.save(secretariaExistente);
    }

    // ==================== OPERAÇÕES DE BUSCA ====================

    /**
     * Buscar por ID
     */
    @Transactional(readOnly = true)
    public Optional<Secretaria> buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        return secretariaRepository.findById(id);
    }

    /**
     * Buscar por email
     */
    @Transactional(readOnly = true)
    public Optional<Secretaria> buscarPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
        }
        return secretariaRepository.findByEmail(email.trim().toLowerCase());
    }

    /**
     * Buscar por CPF
     */
    @Transactional(readOnly = true)
    public Optional<Secretaria> buscarPorCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF não pode ser nulo ou vazio");
        }
        return secretariaRepository.findByCpf(cpf.replaceAll("[^0-9]", ""));
    }

    /**
     * Buscar por matrícula
     */
    @Transactional(readOnly = true)
    public Optional<Secretaria> buscarPorMatricula(String matricula) {
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new IllegalArgumentException("Matrícula não pode ser nula ou vazia");
        }
        return secretariaRepository.findByMatricula(matricula.trim());
    }

    /**
     * Buscar por nome (contendo)
     */
    @Transactional(readOnly = true)
    public List<Secretaria> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
        return secretariaRepository.findByNomeContainingIgnoreCase(nome.trim());
    }

    /**
     * Buscar por cargo
     */
    @Transactional(readOnly = true)
    public List<Secretaria> buscarPorCargo(String cargo) {
        if (cargo == null || cargo.trim().isEmpty()) {
            throw new IllegalArgumentException("Cargo não pode ser nulo ou vazio");
        }
        return secretariaRepository.findByCargo(cargo.trim());
    }

    /**
     * Buscar por período de contratação
     */
    @Transactional(readOnly = true)
    public List<Secretaria> buscarPorPeriodoContratacao(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Datas de início e fim não podem ser nulas");
        }
        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data de início não pode ser posterior à data de fim");
        }
        return secretariaRepository.findByDataContratacaoBetween(dataInicio, dataFim);
    }

    /**
     * Listar todos
     */
    @Transactional(readOnly = true)
    public List<Secretaria> listarTodos() {
        return secretariaRepository.findAll();
    }

    /**
     * Listar ativos
     */
    @Transactional(readOnly = true)
    public List<Secretaria> listarAtivos() {
        return secretariaRepository.findByAtivo(true);
    }

    /**
     * Listar inativos
     */
    @Transactional(readOnly = true)
    public List<Secretaria> listarInativos() {
        return secretariaRepository.findByAtivo(false);
    }



    // ==================== OPERAÇÕES ESPECIAIS ====================

    /**
     * Ativar secretaria
     */
    public Secretaria ativar(Long id) {
        Secretaria secretaria = buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Secretaria não encontrada com ID: " + id));
        
        secretaria.setAtivo(true);
        return secretariaRepository.save(secretaria);
    }

    /**
     * Desativar secretaria
     */
    public Secretaria desativar(Long id) {
        Secretaria secretaria = buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Secretaria não encontrada com ID: " + id));
        
        secretaria.setAtivo(false);
        return secretariaRepository.save(secretaria);
    }

    /**
     * Atualizar último acesso
     */
    public void atualizarUltimoAcesso(Long id) {
        Secretaria secretaria = buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Secretaria não encontrada com ID: " + id));
        
        secretaria.setUltimoAcesso(LocalDateTime.now());
        secretariaRepository.save(secretaria);
    }

    /**
     * Alterar senha
     */
    public void alterarSenha(Long id, String novaSenha) {
        if (novaSenha == null || novaSenha.trim().length() < 6) {
            throw new IllegalArgumentException("Nova senha deve ter pelo menos 6 caracteres");
        }
        
        Secretaria secretaria = buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Secretaria não encontrada com ID: " + id));
        
        secretaria.setSenha(novaSenha.trim());
        secretariaRepository.save(secretaria);
    }

    /**
     * Deletar secretaria
     */
    public void deletar(Long id) {
        if (!secretariaRepository.existsById(id)) {
            throw new RuntimeException("Secretaria não encontrada com ID: " + id);
        }
        secretariaRepository.deleteById(id);
    }

    // ==================== ESTATÍSTICAS ====================

    /**
     * Contar total
     */
    @Transactional(readOnly = true)
    public long contarTotal() {
        return secretariaRepository.count();
    }

    // ==================== MÉTODOS DE VALIDAÇÃO ====================

    private void validarDadosObrigatorios(Secretaria secretaria) {
        if (secretaria == null) {
            throw new IllegalArgumentException("Secretaria não pode ser nula");
        }
        if (secretaria.getNome() == null || secretaria.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (secretaria.getEmail() == null || secretaria.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        if (secretaria.getSenha() == null || secretaria.getSenha().trim().isEmpty()) {
            throw new IllegalArgumentException("Senha é obrigatória");
        }
    }

    private void validarDadosUnicos(Secretaria secretaria) {
        // Verificar email único
        if (secretariaRepository.findByEmail(secretaria.getEmail().trim().toLowerCase()).isPresent()) {
            throw new IllegalArgumentException("Email já está em uso: " + secretaria.getEmail());
        }
        
        // Verificar CPF único (se fornecido)
        if (secretaria.getCpf() != null && !secretaria.getCpf().trim().isEmpty()) {
            String cpfLimpo = secretaria.getCpf().replaceAll("[^0-9]", "");
            if (secretariaRepository.findByCpf(cpfLimpo).isPresent()) {
                throw new IllegalArgumentException("CPF já está em uso: " + secretaria.getCpf());
            }
        }
        
        // Verificar matrícula única (se fornecida)
        if (secretaria.getMatricula() != null && !secretaria.getMatricula().trim().isEmpty()) {
            if (secretariaRepository.findByMatricula(secretaria.getMatricula().trim()).isPresent()) {
                throw new IllegalArgumentException("Matrícula já está em uso: " + secretaria.getMatricula());
            }
        }
    }

    private void validarDadosUnicosParaAtualizacao(Secretaria secretaria, Long id) {
        // Validar email único
        Optional<Secretaria> secretariaComEmail = secretariaRepository.findByEmail(secretaria.getEmail().trim().toLowerCase());
        if (secretariaComEmail.isPresent() && !secretariaComEmail.get().getId().equals(id)) {
            throw new IllegalArgumentException("Email já está em uso: " + secretaria.getEmail());
        }
        
        // Validar CPF único (se fornecido)
        if (secretaria.getCpf() != null && !secretaria.getCpf().trim().isEmpty()) {
            String cpfLimpo = secretaria.getCpf().replaceAll("[^0-9]", "");
            Optional<Secretaria> secretariaComCpf = secretariaRepository.findByCpf(cpfLimpo);
            if (secretariaComCpf.isPresent() && !secretariaComCpf.get().getId().equals(id)) {
                throw new IllegalArgumentException("CPF já está em uso: " + secretaria.getCpf());
            }
        }
        
        // Validar matrícula única (se fornecida)
        if (secretaria.getMatricula() != null && !secretaria.getMatricula().trim().isEmpty()) {
            Optional<Secretaria> secretariaComMatricula = secretariaRepository.findByMatricula(secretaria.getMatricula().trim());
            if (secretariaComMatricula.isPresent() && !secretariaComMatricula.get().getId().equals(id)) {
                throw new IllegalArgumentException("Matrícula já está em uso: " + secretaria.getMatricula());
            }
        }
    }

    private void validarFormatos(Secretaria secretaria) {
        // Validar formato do email
        if (!EMAIL_PATTERN.matcher(secretaria.getEmail().trim()).matches()) {
            throw new IllegalArgumentException("Formato de email inválido: " + secretaria.getEmail());
        }
        
        // Validar formato do CPF (se fornecido)
        if (secretaria.getCpf() != null && !secretaria.getCpf().trim().isEmpty()) {
            String cpfLimpo = secretaria.getCpf().replaceAll("[^0-9]", "");
            if (!CPF_PATTERN.matcher(cpfLimpo).matches()) {
                throw new IllegalArgumentException("CPF deve conter exatamente 11 dígitos");
            }
        }
        
        // Validar formato do telefone (se fornecido)
        if (secretaria.getTelefone() != null && !secretaria.getTelefone().trim().isEmpty()) {
            String telefoneLimpo = secretaria.getTelefone().replaceAll("[^0-9]", "");
            if (!TELEFONE_PATTERN.matcher(telefoneLimpo).matches()) {
                throw new IllegalArgumentException("Telefone deve conter 10 ou 11 dígitos");
            }
        }
        
        // Validar tamanhos
        if (secretaria.getNome().trim().length() > 100) {
            throw new IllegalArgumentException("Nome não pode ter mais de 100 caracteres");
        }
        if (secretaria.getSenha().trim().length() < 6) {
            throw new IllegalArgumentException("Senha deve ter pelo menos 6 caracteres");
        }
        if (secretaria.getEndereco() != null && secretaria.getEndereco().trim().length() > 200) {
            throw new IllegalArgumentException("Endereço não pode ter mais de 200 caracteres");
        }
        if (secretaria.getCargo() != null && secretaria.getCargo().trim().length() > 50) {
            throw new IllegalArgumentException("Cargo não pode ter mais de 50 caracteres");
        }
        if (secretaria.getMatricula() != null && secretaria.getMatricula().trim().length() > 20) {
            throw new IllegalArgumentException("Matrícula não pode ter mais de 20 caracteres");
        }
    }

    private void validarIdades(Secretaria secretaria) {
        LocalDate hoje = LocalDate.now();
        
        // Validar data de nascimento
        if (secretaria.getDataNascimento() != null) {
            if (secretaria.getDataNascimento().isAfter(hoje)) {
                throw new IllegalArgumentException("Data de nascimento não pode ser futura");
            }
            
            int idade = hoje.getYear() - secretaria.getDataNascimento().getYear();
            if (secretaria.getDataNascimento().isAfter(hoje.minusYears(idade))) {
                idade--;
            }
            
            if (idade < 18) {
                throw new IllegalArgumentException("Funcionário deve ter pelo menos 18 anos");
            }
            if (idade > 80) {
                throw new IllegalArgumentException("Idade não pode ser superior a 80 anos");
            }
        }
        
        // Validar data de contratação
        if (secretaria.getDataContratacao() != null) {
            if (secretaria.getDataContratacao().isAfter(hoje)) {
                throw new IllegalArgumentException("Data de contratação não pode ser futura");
            }
        }
    }

}