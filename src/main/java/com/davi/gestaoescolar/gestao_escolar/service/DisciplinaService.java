package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.dto.Disciplina.DisciplinaDtoIn;
import com.davi.gestaoescolar.gestao_escolar.dto.Disciplina.DisciplinaDtoOut;
import com.davi.gestaoescolar.gestao_escolar.dto.Professor.ProfessorDtoOut;
import com.davi.gestaoescolar.gestao_escolar.exception.DisciplinaException;
import com.davi.gestaoescolar.gestao_escolar.model.Disciplina;
import com.davi.gestaoescolar.gestao_escolar.model.Professor;
import com.davi.gestaoescolar.gestao_escolar.repository.DisciplinaRepository;
import com.davi.gestaoescolar.gestao_escolar.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DisciplinaService {

    @Autowired
    private DisciplinaRepository disciplinaRepository;
    
    @Autowired
    private ProfessorRepository professorRepository;

    /**
     * Métodos auxiliares para conversão de DTOs
     */
    private List<DisciplinaDtoOut> toDtos(List<Disciplina> disciplinas) {
        return disciplinas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private DisciplinaDtoOut toDTO(Disciplina disciplina) {
        ProfessorDtoOut professorDto = new ProfessorDtoOut(
                disciplina.getProfessor().getId(),
                disciplina.getProfessor().getNome(),
                disciplina.getProfessor().getCpf(),
                disciplina.getProfessor().getFormacao(),
                disciplina.getProfessor().getTelefone(),
                disciplina.getProfessor().getDataContratacao(),
                disciplina.getProfessor().getCargo(),
                disciplina.getProfessor().getAtivo()
        );

        return new DisciplinaDtoOut(
                disciplina.getId(),
                disciplina.getNome(),
                disciplina.getCargaHoraria(),
                disciplina.getDescricao(),
                disciplina.getAtivo(),
                professorDto
        );
    }

    /**
     * Salva uma nova disciplina
     */
    public DisciplinaDtoOut salvar(DisciplinaDtoIn disciplinaDto) {
        validarDadosDisciplina(disciplinaDto);
        
        Professor professor = buscarProfessor(disciplinaDto.getProfessorId());
        validarProfessor(professor);
        
        Disciplina disciplina = new Disciplina();
        disciplina.setNome(disciplinaDto.getNome());
        disciplina.setCargaHoraria(disciplinaDto.getCargaHoraria());
        disciplina.setDescricao(disciplinaDto.getDescricao());
        disciplina.setAtivo(disciplinaDto.getAtivo() != null ? disciplinaDto.getAtivo() : true);
        disciplina.setProfessor(professor);
        
        Disciplina disciplinaSalva = disciplinaRepository.save(disciplina);
        return toDTO(disciplinaSalva);
    }

    /**
     * Atualiza uma disciplina existente
     */
    public DisciplinaDtoOut atualizar(Long id, DisciplinaDtoIn disciplinaDto) {
        if (id == null) {
            throw new DisciplinaException.DadosInvalidosException("ID não pode ser nulo");
        }
        
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new DisciplinaException.DisciplinaNaoEncontradaException(id));

        validarDadosDisciplina(disciplinaDto);
        
        Professor professor = buscarProfessor(disciplinaDto.getProfessorId());
        validarProfessor(professor);
        
        disciplina.setNome(disciplinaDto.getNome());
        disciplina.setCargaHoraria(disciplinaDto.getCargaHoraria());
        disciplina.setDescricao(disciplinaDto.getDescricao());
        disciplina.setAtivo(disciplinaDto.getAtivo());
        disciplina.setProfessor(professor);
        
        Disciplina disciplinaAtualizada = disciplinaRepository.save(disciplina);
        return toDTO(disciplinaAtualizada);
    }

    /**
     * Busca disciplina por ID
     */
    @Transactional(readOnly = true)
    public DisciplinaDtoOut buscarPorId(Long id) {
        if (id == null) {
            throw new DisciplinaException.DadosInvalidosException("ID não pode ser nulo");
        }
        
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new DisciplinaException.DisciplinaNaoEncontradaException(id));
        
        return toDTO(disciplina);
    }

    /**
     * Busca disciplinas por nome (busca parcial, case insensitive)
     */
    @Transactional(readOnly = true)
    public List<DisciplinaDtoOut> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new DisciplinaException.DadosInvalidosException("Nome não pode ser vazio");
        }
        return toDtos(disciplinaRepository.findByNomeContainingIgnoreCase(nome.trim()));
    }

    /**
     * Busca disciplinas por professor
     */
    @Transactional(readOnly = true)
    public List<DisciplinaDtoOut> buscarPorProfessor(Long professorId) {
        if (professorId == null) {
            throw new DisciplinaException.DadosInvalidosException("ID do professor não pode ser nulo");
        }
        return toDtos(disciplinaRepository.findByProfessorId(professorId));
    }

    /**
     * Busca disciplinas por turma
     */
    @Transactional(readOnly = true)
    public List<DisciplinaDtoOut> buscarPorTurma(Long turmaId) {
        if (turmaId == null) {
            throw new DisciplinaException.DadosInvalidosException("ID da turma não pode ser nulo");
        }
        return toDtos(disciplinaRepository.findByTurmasId(turmaId));
    }

    /**
     * Lista todas as disciplinas
     */
    @Transactional(readOnly = true)
    public List<DisciplinaDtoOut> listarTodas() {
        return toDtos(disciplinaRepository.findAll());
    }

    /**
     * Lista apenas disciplinas ativas
     */
    @Transactional(readOnly = true)
    public List<DisciplinaDtoOut> listarAtivas() {
        return toDtos(disciplinaRepository.findByAtivoTrue());
    }

    /**
     * Desativa uma disciplina (soft delete)
     */
    public void desativar(Long id) {
        if (id == null) {
            throw new DisciplinaException.DadosInvalidosException("ID não pode ser nulo");
        }
        
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new DisciplinaException.DisciplinaNaoEncontradaException(id));
        
        disciplina.setAtivo(false);
        disciplinaRepository.save(disciplina);
    }

    /**
     * Reativa uma disciplina
     */
    public void reativar(Long id) {
        if (id == null) {
            throw new DisciplinaException.DadosInvalidosException("ID não pode ser nulo");
        }
        
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new DisciplinaException.DisciplinaNaoEncontradaException(id));
        
        disciplina.setAtivo(true);
        disciplinaRepository.save(disciplina);
    }

    /**
     * Deleta uma disciplina permanentemente
     */
    public void deletar(Long id) {
        if (id == null) {
            throw new DisciplinaException.DadosInvalidosException("ID não pode ser nulo");
        }
        
        if (!disciplinaRepository.existsById(id)) {
            throw new DisciplinaException.DisciplinaNaoEncontradaException(id);
        }
        disciplinaRepository.deleteById(id);
    }

    /**
     * Atribui um professor a uma disciplina
     */
    public DisciplinaDtoOut atribuirProfessor(Long disciplinaId, Long professorId) {
        if (disciplinaId == null) {
            throw new DisciplinaException.DadosInvalidosException("ID da disciplina não pode ser nulo");
        }
        if (professorId == null) {
            throw new DisciplinaException.DadosInvalidosException("ID do professor não pode ser nulo");
        }
        
        Disciplina disciplina = disciplinaRepository.findById(disciplinaId)
                .orElseThrow(() -> new DisciplinaException.DisciplinaNaoEncontradaException(disciplinaId));
        
        Professor professor = buscarProfessor(professorId);
        validarProfessor(professor);
        
        disciplina.setProfessor(professor);
        Disciplina disciplinaAtualizada = disciplinaRepository.save(disciplina);
        return toDTO(disciplinaAtualizada);
    }

    /**
     * Métodos auxiliares privados
     */
    private Professor buscarProfessor(Long professorId) {
        return professorRepository.findById(professorId)
                .orElseThrow(() -> new DisciplinaException.ProfessorInvalidoException("Professor não encontrado com ID: " + professorId));
    }

    /**
     * Valida os dados da disciplina
     */
    private void validarDadosDisciplina(DisciplinaDtoIn disciplinaDto) {
        if (disciplinaDto == null) {
            throw new DisciplinaException.DadosInvalidosException("Dados da disciplina não podem ser nulos");
        }
        
        if (disciplinaDto.getNome() == null || disciplinaDto.getNome().trim().isEmpty()) {
            throw new DisciplinaException.DadosInvalidosException("Nome da disciplina é obrigatório");
        }
        
        if (disciplinaDto.getCargaHoraria() == null) {
            throw new DisciplinaException.DadosInvalidosException("Carga horária é obrigatória");
        }
        
        if (disciplinaDto.getCargaHoraria() <= 0) {
            throw new DisciplinaException.DadosInvalidosException("Carga horária deve ser maior que zero");
        }
        
        if (disciplinaDto.getCargaHoraria() > 1000) {
            throw new DisciplinaException.DadosInvalidosException("Carga horária não pode exceder 1000 horas");
        }
        
        if (disciplinaDto.getProfessorId() == null) {
            throw new DisciplinaException.DadosInvalidosException("Professor é obrigatório");
        }
        
        // Validação do nome (não pode ser muito longo)
        if (disciplinaDto.getNome().trim().length() > 100) {
            throw new DisciplinaException.DadosInvalidosException("Nome da disciplina não pode exceder 100 caracteres");
        }
        
        // Validação da descrição (se fornecida)
        if (disciplinaDto.getDescricao() != null && disciplinaDto.getDescricao().length() > 500) {
            throw new DisciplinaException.DadosInvalidosException("Descrição não pode exceder 500 caracteres");
        }
    }

    /**
     * Valida se professor existe e está ativo
     */
    private void validarProfessor(Professor professor) {
        if (!professor.getAtivo()) {
            throw new DisciplinaException.ProfessorInvalidoException("Não é possível atribuir um professor inativo à disciplina");
        }
    }
}