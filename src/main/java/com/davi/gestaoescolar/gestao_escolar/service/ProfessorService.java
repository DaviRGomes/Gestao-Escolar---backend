package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.dto.Professor.ProfessorDtoIn;
import com.davi.gestaoescolar.gestao_escolar.dto.Professor.ProfessorDtoOut;
import com.davi.gestaoescolar.gestao_escolar.model.Professor;
import com.davi.gestaoescolar.gestao_escolar.model.enums.Perfil;
import com.davi.gestaoescolar.gestao_escolar.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    // Comentando temporariamente para testar compilação
    // @Autowired
    // private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Conversão de entidade para DTO de saída
    private ProfessorDtoOut toDTO(Professor professor) {
        return new ProfessorDtoOut(
                professor.getId(),
                professor.getNome(),
                professor.getCpf(),
                professor.getFormacao(),
                professor.getTelefone(),
                professor.getDataContratacao(),
                professor.getCargo(),
                professor.getAtivo()
        );
    }

    // DTOIn: salvar novo professor
    public ProfessorDtoOut salvar(ProfessorDtoIn professorDto) {
        if (professorDto == null) {
            throw new IllegalArgumentException("Dados do professor não podem ser nulos");
        }

        Professor professor = new Professor();
        professor.setEmail(professorDto.getEmail());
        professor.setSenha(professorDto.getSenha());
        professor.setNome(professorDto.getNome());
        professor.setCpf(professorDto.getCpf());
        professor.setFormacao(professorDto.getFormacao());
        professor.setTelefone(professorDto.getTelefone());
        professor.setDataContratacao(professorDto.getDataContratacao() != null ? professorDto.getDataContratacao() : LocalDate.now());
        professor.setCargo(professorDto.getCargo());
        professor.setAtivo(professorDto.getAtivo() != null ? professorDto.getAtivo() : true);
        professor.setPerfil(Perfil.PROFESSOR);

        validarDadosProfessor(professor);

        if (cpfJaExiste(professor.getCpf())) {
            throw new RuntimeException("CPF já cadastrado: " + professor.getCpf());
        }

        if (professor.getSenha() != null && !professor.getSenha().isEmpty()) {
            professor.setSenha(passwordEncoder.encode(professor.getSenha()));
        }

        Professor salvo = professorRepository.save(professor);
        return toDTO(salvo);
    }

    // DTOIn: atualizar professor existente
    public ProfessorDtoOut atualizar(Long id, ProfessorDtoIn professorDto) {
        if (id == null) {
            throw new IllegalArgumentException("ID do professor não pode ser nulo para atualização");
        }
        if (professorDto == null) {
            throw new IllegalArgumentException("Dados do professor não podem ser nulos");
        }

        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado com ID: " + id));

        professor.setEmail(professorDto.getEmail());
        professor.setNome(professorDto.getNome());
        professor.setCpf(professorDto.getCpf());
        professor.setFormacao(professorDto.getFormacao());
        professor.setTelefone(professorDto.getTelefone());
        professor.setDataContratacao(professorDto.getDataContratacao() != null ? professorDto.getDataContratacao() : professor.getDataContratacao());
        professor.setCargo(professorDto.getCargo());
        professor.setAtivo(professorDto.getAtivo() != null ? professorDto.getAtivo() : professor.getAtivo());
        professor.setPerfil(Perfil.PROFESSOR);

        validarDadosProfessor(professor);

        if (cpfJaExiste(professor.getCpf())) {
            throw new RuntimeException("CPF já cadastrado para outro professor: " + professor.getCpf());
        }

        if (professorDto.getSenha() != null && !professorDto.getSenha().isEmpty()) {
            professor.setSenha(passwordEncoder.encode(professorDto.getSenha()));
        }

        Professor atualizado = professorRepository.save(professor);
        return toDTO(atualizado);
    }

    // Consultas e listagens com saída DTO
    @Transactional(readOnly = true)
    public Optional<ProfessorDtoOut> buscarPorIdDto(Long id) {
        return professorRepository.findById(id).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<ProfessorDtoOut> buscarPorCpfDto(String cpf) {
        return professorRepository.findByCpf(cpf).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<ProfessorDtoOut> buscarPorEmailDto(String email) {
        return professorRepository.findByEmail(email).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public List<ProfessorDtoOut> listarTodosDto() {
        return professorRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<ProfessorDtoOut> buscarPorNomeDto(String nome) {
        return professorRepository.findByNomeContainingIgnoreCase(nome).stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<ProfessorDtoOut> buscarPorFormacaoDto(String formacao) {
        return professorRepository.findByFormacaoContainingIgnoreCase(formacao).stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<ProfessorDtoOut> listarAtivosDto() {
        return professorRepository.findAll().stream()
                .filter(p -> p.getAtivo() != null && p.getAtivo())
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProfessorDtoOut> listarInativosDto() {
        return professorRepository.findAll().stream()
                .filter(p -> p.getAtivo() == null || !p.getAtivo())
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean cpfJaExiste(String cpf) {
        if (cpf == null) return false;
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        return professorRepository.findByCpf(cpfLimpo).isPresent();
    }

    private void validarDadosProfessor(Professor professor) {
        if (professor == null) {
            throw new IllegalArgumentException("Dados do professor não podem ser nulos");
        }

        if (professor.getNome() == null || professor.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do professor é obrigatório");
        }

        if (professor.getEmail() == null || professor.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email do professor é obrigatório");
        }

        if (!professor.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Formato de email inválido");
        }

        if (professor.getCpf() == null || professor.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF do professor é obrigatório");
        }

        String cpfLimpo = professor.getCpf().replaceAll("[^0-9]", "");
        if (cpfLimpo.length() != 11) {
            throw new IllegalArgumentException("CPF deve conter 11 dígitos");
        }
        professor.setCpf(cpfLimpo);
    }

    public void deletar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do professor não pode ser nulo");
        }
        if (!professorRepository.existsById(id)) {
            throw new RuntimeException("Professor não encontrado com ID: " + id);
        }
        professorRepository.deleteById(id);
    }
}