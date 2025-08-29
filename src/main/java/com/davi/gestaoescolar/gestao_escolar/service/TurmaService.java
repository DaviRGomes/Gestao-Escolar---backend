package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.model.Turma;
import com.davi.gestaoescolar.gestao_escolar.model.enums.Periodo;
import com.davi.gestaoescolar.gestao_escolar.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TurmaService {

    @Autowired
    private TurmaRepository turmaRepository;

    /**
     * Salva uma nova turma
     */
    public Turma salvar(Turma turma) {
        validarDadosTurma(turma);
        return turmaRepository.save(turma);
    }

    /**
     * Atualiza uma turma existente
     */
    public Turma atualizar(Long id, Turma turmaAtualizada) {
        Optional<Turma> turmaExistente = turmaRepository.findById(id);
        if (turmaExistente.isEmpty()) {
            throw new RuntimeException("Turma não encontrada com ID: " + id);
        }

        validarDadosTurma(turmaAtualizada);
        
        Turma turma = turmaExistente.get();
        turma.setNome(turmaAtualizada.getNome());
        turma.setAnoLetivo(turmaAtualizada.getAnoLetivo());
        turma.setSemestre(turmaAtualizada.getSemestre());
        turma.setPeriodo(turmaAtualizada.getPeriodo());
        turma.setAtivo(turmaAtualizada.getAtivo());
        
        return turmaRepository.save(turma);
    }

    /**
     * Busca turma por ID
     */
    @Transactional(readOnly = true)
    public Optional<Turma> buscarPorId(Long id) {
        return turmaRepository.findById(id);
    }

    /**
     * Busca turmas por nome (busca parcial, case insensitive)
     */
    @Transactional(readOnly = true)
    public List<Turma> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        return turmaRepository.findByNomeContainingIgnoreCase(nome.trim());
    }

    /**
     * Busca turmas por ano letivo
     */
    @Transactional(readOnly = true)
    public List<Turma> buscarPorAnoLetivo(String anoLetivo) {
        if (anoLetivo == null || anoLetivo.trim().isEmpty()) {
            throw new IllegalArgumentException("Ano letivo não pode ser vazio");
        }
        return turmaRepository.findByAnoLetivo(anoLetivo.trim());
    }

    /**
     * Busca turmas por semestre
     */
    @Transactional(readOnly = true)
    public List<Turma> buscarPorSemestre(String semestre) {
        if (semestre == null || semestre.trim().isEmpty()) {
            throw new IllegalArgumentException("Semestre não pode ser vazio");
        }
        return turmaRepository.findBySemestre(semestre.trim());
    }

    /**
     * Busca turmas por período
     */
    @Transactional(readOnly = true)
    public List<Turma> buscarPorPeriodo(Periodo periodo) {
        if (periodo == null) {
            throw new IllegalArgumentException("Período não pode ser nulo");
        }
        return turmaRepository.findByPeriodo(periodo);
    }

    /**
     * Busca turmas por ano letivo e semestre
     */
    @Transactional(readOnly = true)
    public List<Turma> buscarPorAnoLetivoESemestre(String anoLetivo, String semestre) {
        if (anoLetivo == null || anoLetivo.trim().isEmpty()) {
            throw new IllegalArgumentException("Ano letivo não pode ser vazio");
        }
        if (semestre == null || semestre.trim().isEmpty()) {
            throw new IllegalArgumentException("Semestre não pode ser vazio");
        }
        return turmaRepository.findByAnoLetivoAndSemestre(anoLetivo.trim(), semestre.trim());
    }

    /**
     * Lista todas as turmas
     */
    @Transactional(readOnly = true)
    public List<Turma> listarTodas() {
        return turmaRepository.findAll();
    }

    /**
     * Lista apenas turmas ativas
     */
    @Transactional(readOnly = true)
    public List<Turma> listarAtivas() {
        return turmaRepository.findByAtivo(true);
    }

    /**
     * Lista apenas turmas inativas
     */
    @Transactional(readOnly = true)
    public List<Turma> listarInativas() {
        return turmaRepository.findByAtivo(false);
    }

    /**
     * Desativa uma turma (soft delete)
     */
    public void desativar(Long id) {
        Optional<Turma> turma = turmaRepository.findById(id);
        if (turma.isEmpty()) {
            throw new RuntimeException("Turma não encontrada com ID: " + id);
        }
        
        Turma turmaEntity = turma.get();
        turmaEntity.setAtivo(false);
        turmaRepository.save(turmaEntity);
    }

    /**
     * Reativa uma turma
     */
    public void reativar(Long id) {
        Optional<Turma> turma = turmaRepository.findById(id);
        if (turma.isEmpty()) {
            throw new RuntimeException("Turma não encontrada com ID: " + id);
        }
        
        Turma turmaEntity = turma.get();
        turmaEntity.setAtivo(true);
        turmaRepository.save(turmaEntity);
    }

    /**
     * Deleta uma turma permanentemente
     */
    public void deletar(Long id) {
        if (!turmaRepository.existsById(id)) {
            throw new RuntimeException("Turma não encontrada com ID: " + id);
        }
        turmaRepository.deleteById(id);
    }

    /**
     * Valida os dados da turma
     */
    private void validarDadosTurma(Turma turma) {
        if (turma == null) {
            throw new IllegalArgumentException("Turma não pode ser nula");
        }
        
        if (turma.getNome() == null || turma.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da turma é obrigatório");
        }
        
        if (turma.getAnoLetivo() == null || turma.getAnoLetivo().trim().isEmpty()) {
            throw new IllegalArgumentException("Ano letivo é obrigatório");
        }
        
        if (turma.getSemestre() == null || turma.getSemestre().trim().isEmpty()) {
            throw new IllegalArgumentException("Semestre é obrigatório");
        }
        
        if (turma.getPeriodo() == null) {
            throw new IllegalArgumentException("Período é obrigatório");
        }
        
        // Validação do formato do ano letivo (deve ser um ano válido)
        try {
            int ano = Integer.parseInt(turma.getAnoLetivo().trim());
            if (ano < 1900 || ano > 2100) {
                throw new IllegalArgumentException("Ano letivo deve estar entre 1900 e 2100");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ano letivo deve ser um número válido");
        }
        
        // Validação do semestre (deve ser 1 ou 2)
        String semestre = turma.getSemestre().trim();
        if (!semestre.equals("1") && !semestre.equals("2")) {
            throw new IllegalArgumentException("Semestre deve ser '1' ou '2'");
        }
    }
}