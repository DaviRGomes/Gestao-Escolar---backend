package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.model.Disciplina;
import com.davi.gestaoescolar.gestao_escolar.model.Professor;
import com.davi.gestaoescolar.gestao_escolar.repository.DisciplinaRepository;
import com.davi.gestaoescolar.gestao_escolar.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DisciplinaService {

    @Autowired
    private DisciplinaRepository disciplinaRepository;
    
    @Autowired
    private ProfessorService professorService;

    /**
     * Salva uma nova disciplina
     */
    public Disciplina salvar(Disciplina disciplina) {
        validarDadosDisciplina(disciplina);
        validarProfessor(disciplina.getProfessor());
        return disciplinaRepository.save(disciplina);
    }

    /**
     * Atualiza uma disciplina existente
     */
    public Disciplina atualizar(Long id, Disciplina disciplinaAtualizada) {
        Optional<Disciplina> disciplinaExistente = disciplinaRepository.findById(id);
        if (disciplinaExistente.isEmpty()) {
            throw new RuntimeException("Disciplina não encontrada com ID: " + id);
        }

        validarDadosDisciplina(disciplinaAtualizada);
        validarProfessor(disciplinaAtualizada.getProfessor());
        
        Disciplina disciplina = disciplinaExistente.get();
        disciplina.setNome(disciplinaAtualizada.getNome());
        disciplina.setCargaHoraria(disciplinaAtualizada.getCargaHoraria());
        disciplina.setDescricao(disciplinaAtualizada.getDescricao());
        disciplina.setProfessor(disciplinaAtualizada.getProfessor());
        disciplina.setAtivo(disciplinaAtualizada.getAtivo());
        
        return disciplinaRepository.save(disciplina);
    }

    /**
     * Busca disciplina por ID
     */
    @Transactional(readOnly = true)
    public Optional<Disciplina> buscarPorId(Long id) {
        return disciplinaRepository.findById(id);
    }

    /**
     * Busca disciplinas por nome (busca parcial, case insensitive)
     */
    @Transactional(readOnly = true)
    public List<Disciplina> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        return disciplinaRepository.findByNomeContainingIgnoreCase(nome.trim());
    }

    /**
     * Busca disciplinas por professor
     */
    @Transactional(readOnly = true)
    public List<Disciplina> buscarPorProfessor(Long professorId) {
        if (professorId == null) {
            throw new IllegalArgumentException("ID do professor não pode ser nulo");
        }
        return disciplinaRepository.findByProfessorId(professorId);
    }

    /**
     * Busca disciplinas por turma
     */
    @Transactional(readOnly = true)
    public List<Disciplina> buscarPorTurma(Long turmaId) {
        if (turmaId == null) {
            throw new IllegalArgumentException("ID da turma não pode ser nulo");
        }
        return disciplinaRepository.findByTurmasId(turmaId);
    }

    /**
     * Busca disciplinas por carga horária mínima
     */
    @Transactional(readOnly = true)
    public List<Disciplina> buscarPorCargaHorariaMinima(Integer cargaHorariaMinima) {
        if (cargaHorariaMinima == null || cargaHorariaMinima < 0) {
            throw new IllegalArgumentException("Carga horária mínima deve ser um valor positivo");
        }
        return disciplinaRepository.findByCargaHorariaGreaterThanEqual(cargaHorariaMinima);
    }

    /**
     * Lista todas as disciplinas
     */
    @Transactional(readOnly = true)
    public List<Disciplina> listarTodas() {
        return disciplinaRepository.findAll();
    }

    /**
     * Lista apenas disciplinas ativas
     */
    @Transactional(readOnly = true)
    public List<Disciplina> listarAtivas() {
        return disciplinaRepository.findByAtivoTrue();
    }

    /**
     * Desativa uma disciplina (soft delete)
     */
    public void desativar(Long id) {
        Optional<Disciplina> disciplina = disciplinaRepository.findById(id);
        if (disciplina.isEmpty()) {
            throw new RuntimeException("Disciplina não encontrada com ID: " + id);
        }
        
        Disciplina disciplinaEntity = disciplina.get();
        disciplinaEntity.setAtivo(false);
        disciplinaRepository.save(disciplinaEntity);
    }

    /**
     * Reativa uma disciplina
     */
    public void reativar(Long id) {
        Optional<Disciplina> disciplina = disciplinaRepository.findById(id);
        if (disciplina.isEmpty()) {
            throw new RuntimeException("Disciplina não encontrada com ID: " + id);
        }
        
        Disciplina disciplinaEntity = disciplina.get();
        disciplinaEntity.setAtivo(true);
        disciplinaRepository.save(disciplinaEntity);
    }

    /**
     * Deleta uma disciplina permanentemente
     */
    public void deletar(Long id) {
        if (!disciplinaRepository.existsById(id)) {
            throw new RuntimeException("Disciplina não encontrada com ID: " + id);
        }
        disciplinaRepository.deleteById(id);
    }

    /**
     * Atribui um professor a uma disciplina
     */
    public Disciplina atribuirProfessor(Long disciplinaId, Long professorId) {
        Optional<Disciplina> disciplinaOpt = disciplinaRepository.findById(disciplinaId);
        if (disciplinaOpt.isEmpty()) {
            throw new RuntimeException("Disciplina não encontrada com ID: " + disciplinaId);
        }
        
        Optional<Professor> professorOpt = professorService.buscarPorId(professorId);
        if (professorOpt.isEmpty()) {
            throw new RuntimeException("Professor não encontrado com ID: " + professorId);
        }
        
        Disciplina disciplina = disciplinaOpt.get();
        Professor professor = professorOpt.get();
        
        if (!professor.getAtivo()) {
            throw new RuntimeException("Não é possível atribuir um professor inativo à disciplina");
        }
        
        disciplina.setProfessor(professor);
        return disciplinaRepository.save(disciplina);
    }

    /**
     * Valida os dados da disciplina
     */
    private void validarDadosDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("Disciplina não pode ser nula");
        }
        
        if (disciplina.getNome() == null || disciplina.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da disciplina é obrigatório");
        }
        
        if (disciplina.getCargaHoraria() == null) {
            throw new IllegalArgumentException("Carga horária é obrigatória");
        }
        
        if (disciplina.getCargaHoraria() <= 0) {
            throw new IllegalArgumentException("Carga horária deve ser maior que zero");
        }
        
        if (disciplina.getCargaHoraria() > 1000) {
            throw new IllegalArgumentException("Carga horária não pode exceder 1000 horas");
        }
        
        // Validação do nome (não pode ser muito longo)
        if (disciplina.getNome().trim().length() > 100) {
            throw new IllegalArgumentException("Nome da disciplina não pode exceder 100 caracteres");
        }
        
        // Validação da descrição (se fornecida)
        if (disciplina.getDescricao() != null && disciplina.getDescricao().length() > 500) {
            throw new IllegalArgumentException("Descrição não pode exceder 500 caracteres");
        }
    }
    
    /**
     * Valida se o professor existe e está ativo
     */
    private void validarProfessor(Professor professor) {
        if (professor == null || professor.getId() == null) {
            throw new IllegalArgumentException("Professor é obrigatório");
        }
        
        Optional<Professor> professorExistente = professorService.buscarPorId(professor.getId());
        if (professorExistente.isEmpty()) {
            throw new RuntimeException("Professor não encontrado com ID: " + professor.getId());
        }
        
        if (!professorExistente.get().getAtivo()) {
            throw new RuntimeException("Não é possível atribuir um professor inativo à disciplina");
        }
    }
}