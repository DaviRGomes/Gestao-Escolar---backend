package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.model.Professor;
import com.davi.gestaoescolar.gestao_escolar.model.enums.Perfil;
import com.davi.gestaoescolar.gestao_escolar.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Salva um novo professor
     */
    public Professor salvar(Professor professor) {
        // Validações
        validarDadosProfessor(professor);
        
        // Verificar se CPF já existe
        if (cpfJaExiste(professor.getCpf())) {
            throw new RuntimeException("CPF já cadastrado: " + professor.getCpf());
        }
        
        // Verificar se email já existe
        if (usuarioService.emailJaExiste(professor.getEmail())) {
            throw new RuntimeException("Email já cadastrado: " + professor.getEmail());
        }
        
        // Configurar dados do usuário
        professor.setPerfil(Perfil.PROFESSOR);
        professor.setAtivo(true);
        
        // Criptografar senha
        if (professor.getSenha() != null) {
            professor.setSenha(passwordEncoder.encode(professor.getSenha()));
        }
        
        return professorRepository.save(professor);
    }

    /**
     * Atualiza um professor existente
     */
    public Professor atualizar(Professor professor) {
        if (professor.getId() == null) {
            throw new IllegalArgumentException("ID do professor não pode ser nulo para atualização");
        }
        
        Optional<Professor> professorExistente = professorRepository.findById(professor.getId());
        if (professorExistente.isEmpty()) {
            throw new RuntimeException("Professor não encontrado com ID: " + professor.getId());
        }
        
        // Validações
        validarDadosProfessor(professor);
        
        // Verificar se CPF já existe para outro professor
        if (cpfJaExisteParaOutroProfessor(professor.getCpf(), professor.getId())) {
            throw new RuntimeException("CPF já cadastrado para outro professor: " + professor.getCpf());
        }
        
        // Verificar se email já existe para outro usuário
        if (usuarioService.emailJaExisteParaOutroUsuario(professor.getEmail(), professor.getId())) {
            throw new RuntimeException("Email já cadastrado para outro usuário: " + professor.getEmail());
        }
        
        // Se a senha foi alterada, criptografar
        if (professor.getSenha() != null && !professor.getSenha().isEmpty()) {
            professor.setSenha(passwordEncoder.encode(professor.getSenha()));
        } else {
            // Manter a senha existente se não foi fornecida nova senha
            professor.setSenha(professorExistente.get().getSenha());
        }
        
        // Manter perfil como PROFESSOR
        professor.setPerfil(Perfil.PROFESSOR);
        
        return professorRepository.save(professor);
    }

    /**
     * Busca professor por ID
     */
    @Transactional(readOnly = true)
    public Optional<Professor> buscarPorId(Long id) {
        return professorRepository.findById(id);
    }

    /**
     * Busca professor por CPF
     */
    @Transactional(readOnly = true)
    public Optional<Professor> buscarPorCpf(String cpf) {
        return professorRepository.findByCpf(cpf);
    }

    /**
     * Busca professor por email
     */
    @Transactional(readOnly = true)
    public Optional<Professor> buscarPorEmail(String email) {
        return professorRepository.findByEmail(email);
    }

    /**
     * Lista todos os professores
     */
    @Transactional(readOnly = true)
    public List<Professor> listarTodos() {
        return professorRepository.findAll();
    }

    /**
     * Busca professores por nome (busca parcial, case insensitive)
     */
    @Transactional(readOnly = true)
    public List<Professor> buscarPorNome(String nome) {
        return professorRepository.findByNomeContainingIgnoreCase(nome);
    }

    /**
     * Busca professores por formação (busca parcial, case insensitive)
     */
    @Transactional(readOnly = true)
    public List<Professor> buscarPorFormacao(String formacao) {
        return professorRepository.findByFormacaoContainingIgnoreCase(formacao);
    }

    /**
     * Busca professores por telefone (busca parcial)
     */
    @Transactional(readOnly = true)
    public List<Professor> buscarPorTelefone(String telefone) {
        return professorRepository.findByTelefoneContaining(telefone);
    }

    /**
     * Desativa um professor (soft delete)
     */
    public void desativar(Long id) {
        Optional<Professor> professor = professorRepository.findById(id);
        if (professor.isPresent()) {
            professor.get().setAtivo(false);
            professorRepository.save(professor.get());
        } else {
            throw new RuntimeException("Professor não encontrado com ID: " + id);
        }
    }

    /**
     * Reativa um professor
     */
    public void reativar(Long id) {
        Optional<Professor> professor = professorRepository.findById(id);
        if (professor.isPresent()) {
            professor.get().setAtivo(true);
            professorRepository.save(professor.get());
        } else {
            throw new RuntimeException("Professor não encontrado com ID: " + id);
        }
    }

    /**
     * Remove professor permanentemente (hard delete)
     */
    public void deletar(Long id) {
        if (!professorRepository.existsById(id)) {
            throw new RuntimeException("Professor não encontrado com ID: " + id);
        }
        professorRepository.deleteById(id);
    }

    /**
     * Atualiza o último acesso do professor
     */
    public void atualizarUltimoAcesso(String email) {
        Optional<Professor> professor = professorRepository.findByEmail(email);
        if (professor.isPresent()) {
            professor.get().setUltimoAcesso(LocalDateTime.now());
            professorRepository.save(professor.get());
        }
    }

    /**
     * Altera senha do professor
     */
    public void alterarSenha(Long id, String novaSenha) {
        Optional<Professor> professor = professorRepository.findById(id);
        if (professor.isPresent()) {
            professor.get().setSenha(passwordEncoder.encode(novaSenha));
            professorRepository.save(professor.get());
        } else {
            throw new RuntimeException("Professor não encontrado com ID: " + id);
        }
    }

    /**
     * Verifica se CPF já existe
     */
    @Transactional(readOnly = true)
    public boolean cpfJaExiste(String cpf) {
        return professorRepository.findByCpf(cpf).isPresent();
    }

    /**
     * Verifica se CPF já existe para outro professor (útil para atualizações)
     */
    @Transactional(readOnly = true)
    public boolean cpfJaExisteParaOutroProfessor(String cpf, Long idProfessor) {
        Optional<Professor> professor = professorRepository.findByCpf(cpf);
        return professor.isPresent() && !professor.get().getId().equals(idProfessor);
    }

    /**
     * Valida os dados obrigatórios do professor
     */
    private void validarDadosProfessor(Professor professor) {
        if (professor.getNome() == null || professor.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do professor é obrigatório");
        }
        
        if (professor.getCpf() == null || professor.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF do professor é obrigatório");
        }
        
        if (professor.getEmail() == null || professor.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email do professor é obrigatório");
        }
        
        // Validação básica de formato de email
        if (!professor.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Formato de email inválido");
        }
        
        // Validação básica de CPF (apenas formato)
        String cpfLimpo = professor.getCpf().replaceAll("[^0-9]", "");
        if (cpfLimpo.length() != 11) {
            throw new IllegalArgumentException("CPF deve conter 11 dígitos");
        }
        
        // Atualizar CPF com apenas números
        professor.setCpf(cpfLimpo);
    }

    /**
     * Formata CPF para exibição (XXX.XXX.XXX-XX)
     */
    public String formatarCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return cpf;
        }
        return cpf.substring(0, 3) + "." + 
               cpf.substring(3, 6) + "." + 
               cpf.substring(6, 9) + "-" + 
               cpf.substring(9, 11);
    }

    /**
     * Lista professores ativos
     */
    @Transactional(readOnly = true)
    public List<Professor> listarAtivos() {
        return professorRepository.findAll().stream()
                .filter(professor -> professor.getAtivo() != null && professor.getAtivo())
                .toList();
    }

    /**
     * Lista professores inativos
     */
    @Transactional(readOnly = true)
    public List<Professor> listarInativos() {
        return professorRepository.findAll().stream()
                .filter(professor -> professor.getAtivo() == null || !professor.getAtivo())
                .toList();
    }
}