package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.model.Disciplina;
import com.davi.gestaoescolar.gestao_escolar.model.Planejamento;
import com.davi.gestaoescolar.gestao_escolar.model.Turma;
import com.davi.gestaoescolar.gestao_escolar.repository.DisciplinaRepository;
import com.davi.gestaoescolar.gestao_escolar.repository.PlanejamentoRepository;
import com.davi.gestaoescolar.gestao_escolar.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlanejamentoService {

    @Autowired
    private PlanejamentoRepository planejamentoRepository;
    
    @Autowired
    private DisciplinaRepository disciplinaRepository;
    
    @Autowired
    private TurmaRepository turmaRepository;

    /**
     * Salva um novo planejamento
     */
    public Planejamento salvar(Planejamento planejamento) {
        validarDadosPlanejamento(planejamento);
        validarDisciplinaETurma(planejamento.getDisciplina(), planejamento.getTurma());
        verificarPlanejamentoDuplicado(planejamento);
        
        // Define a data de criação se não foi informada
        if (planejamento.getDataCriacao() == null) {
            planejamento.setDataCriacao(LocalDateTime.now());
        }
        
        return planejamentoRepository.save(planejamento);
    }

    /**
     * Atualiza um planejamento existente
     */
    public Planejamento atualizar(Long id, Planejamento planejamentoAtualizado) {
        Optional<Planejamento> planejamentoExistente = planejamentoRepository.findById(id);
        if (planejamentoExistente.isEmpty()) {
            throw new RuntimeException("Planejamento não encontrado com ID: " + id);
        }

        validarDadosPlanejamento(planejamentoAtualizado);
        
        Planejamento planejamento = planejamentoExistente.get();
        
        // Verifica se houve mudança nos campos únicos
        boolean mudouCamposUnicos = !planejamento.getDisciplina().getId().equals(planejamentoAtualizado.getDisciplina().getId()) ||
                                   !planejamento.getTurma().getId().equals(planejamentoAtualizado.getTurma().getId()) ||
                                   !planejamento.getSemestre().equals(planejamentoAtualizado.getSemestre()) ||
                                   !planejamento.getAno().equals(planejamentoAtualizado.getAno());
        
        if (mudouCamposUnicos) {
            validarDisciplinaETurma(planejamentoAtualizado.getDisciplina(), planejamentoAtualizado.getTurma());
            verificarPlanejamentoDuplicado(planejamentoAtualizado, id);
            
            planejamento.setDisciplina(planejamentoAtualizado.getDisciplina());
            planejamento.setTurma(planejamentoAtualizado.getTurma());
            planejamento.setSemestre(planejamentoAtualizado.getSemestre());
            planejamento.setAno(planejamentoAtualizado.getAno());
        }
        
        planejamento.setDescricao(planejamentoAtualizado.getDescricao());
        planejamento.setDataAtualizacao(LocalDateTime.now());
        
        return planejamentoRepository.save(planejamento);
    }

    /**
     * Busca planejamento por ID
     */
    @Transactional(readOnly = true)
    public Optional<Planejamento> buscarPorId(Long id) {
        return planejamentoRepository.findById(id);
    }

    /**
     * Busca planejamentos por disciplina
     */
    @Transactional(readOnly = true)
    public List<Planejamento> buscarPorDisciplina(Long disciplinaId) {
        if (disciplinaId == null) {
            throw new IllegalArgumentException("ID da disciplina não pode ser nulo");
        }
        return planejamentoRepository.findByDisciplinaId(disciplinaId);
    }

    /**
     * Busca planejamentos por turma
     */
    @Transactional(readOnly = true)
    public List<Planejamento> buscarPorTurma(Long turmaId) {
        if (turmaId == null) {
            throw new IllegalArgumentException("ID da turma não pode ser nulo");
        }
        return planejamentoRepository.findByTurmaId(turmaId);
    }

    /**
     * Busca planejamentos por ano
     */
    @Transactional(readOnly = true)
    public List<Planejamento> buscarPorAno(Integer ano) {
        if (ano == null) {
            throw new IllegalArgumentException("Ano não pode ser nulo");
        }
        return planejamentoRepository.findByAno(ano);
    }

    /**
     * Busca planejamentos por semestre
     */
    @Transactional(readOnly = true)
    public List<Planejamento> buscarPorSemestre(String semestre) {
        if (semestre == null || semestre.trim().isEmpty()) {
            throw new IllegalArgumentException("Semestre não pode ser vazio");
        }
        return planejamentoRepository.findBySemestre(semestre.trim());
    }

    /**
     * Busca planejamentos por ano e semestre
     */
    @Transactional(readOnly = true)
    public List<Planejamento> buscarPorAnoESemestre(Integer ano, String semestre) {
        if (ano == null) {
            throw new IllegalArgumentException("Ano não pode ser nulo");
        }
        if (semestre == null || semestre.trim().isEmpty()) {
            throw new IllegalArgumentException("Semestre não pode ser vazio");
        }
        return planejamentoRepository.findByAnoAndSemestre(ano, semestre.trim());
    }

    /**
     * Busca planejamento específico por disciplina, turma, semestre e ano
     */
    @Transactional(readOnly = true)
    public Optional<Planejamento> buscarEspecifico(Long disciplinaId, Long turmaId, String semestre, Integer ano) {
        if (disciplinaId == null) {
            throw new IllegalArgumentException("ID da disciplina não pode ser nulo");
        }
        if (turmaId == null) {
            throw new IllegalArgumentException("ID da turma não pode ser nulo");
        }
        if (semestre == null || semestre.trim().isEmpty()) {
            throw new IllegalArgumentException("Semestre não pode ser vazio");
        }
        if (ano == null) {
            throw new IllegalArgumentException("Ano não pode ser nulo");
        }
        
        return planejamentoRepository.findByDisciplinaIdAndTurmaIdAndSemestreAndAno(
            disciplinaId, turmaId, semestre.trim(), ano);
    }

    /**
     * Lista todos os planejamentos
     */
    @Transactional(readOnly = true)
    public List<Planejamento> listarTodos() {
        return planejamentoRepository.findAll();
    }

    /**
     * Deleta um planejamento permanentemente
     */
    public void deletar(Long id) {
        if (!planejamentoRepository.existsById(id)) {
            throw new RuntimeException("Planejamento não encontrado com ID: " + id);
        }
        planejamentoRepository.deleteById(id);
    }

    /**
     * Cria um planejamento básico para uma disciplina e turma
     */
    public Planejamento criarPlanejamentoBasico(Long disciplinaId, Long turmaId, String semestre, Integer ano) {
        Optional<Disciplina> disciplina = disciplinaRepository.findById(disciplinaId);
        if (disciplina.isEmpty()) {
            throw new RuntimeException("Disciplina não encontrada com ID: " + disciplinaId);
        }
        
        Optional<Turma> turma = turmaRepository.findById(turmaId);
        if (turma.isEmpty()) {
            throw new RuntimeException("Turma não encontrada com ID: " + turmaId);
        }
        
        Planejamento planejamento = new Planejamento();
        planejamento.setDisciplina(disciplina.get());
        planejamento.setTurma(turma.get());
        planejamento.setSemestre(semestre);
        planejamento.setAno(ano);
        planejamento.setDescricao("Planejamento para " + disciplina.get().getNome() + 
                                 " - Turma " + turma.get().getNome() + 
                                 " - " + semestre + "º semestre de " + ano);
        
        return salvar(planejamento);
    }

    /**
     * Valida os dados do planejamento
     */
    private void validarDadosPlanejamento(Planejamento planejamento) {
        if (planejamento == null) {
            throw new IllegalArgumentException("Planejamento não pode ser nulo");
        }
        
        if (planejamento.getSemestre() == null || planejamento.getSemestre().trim().isEmpty()) {
            throw new IllegalArgumentException("Semestre é obrigatório");
        }
        
        if (planejamento.getAno() == null) {
            throw new IllegalArgumentException("Ano é obrigatório");
        }
        
        if (planejamento.getDisciplina() == null || planejamento.getDisciplina().getId() == null) {
            throw new IllegalArgumentException("Disciplina é obrigatória");
        }
        
        if (planejamento.getTurma() == null || planejamento.getTurma().getId() == null) {
            throw new IllegalArgumentException("Turma é obrigatória");
        }
        
        // Validação do semestre (deve ser 1 ou 2)
        String semestre = planejamento.getSemestre().trim();
        if (!semestre.equals("1") && !semestre.equals("2")) {
            throw new IllegalArgumentException("Semestre deve ser '1' ou '2'");
        }
        
        // Validação do ano (deve ser um ano válido)
        if (planejamento.getAno() < 1900 || planejamento.getAno() > 2100) {
            throw new IllegalArgumentException("Ano deve estar entre 1900 e 2100");
        }
        
        // Validação da descrição (se fornecida)
        if (planejamento.getDescricao() != null && planejamento.getDescricao().length() > 1000) {
            throw new IllegalArgumentException("Descrição não pode exceder 1000 caracteres");
        }
    }
    
    /**
     * Valida se disciplina e turma existem e estão ativas
     */
    private void validarDisciplinaETurma(Disciplina disciplina, Turma turma) {
        Optional<Disciplina> disciplinaExistente = disciplinaRepository.findById(disciplina.getId());
        if (disciplinaExistente.isEmpty()) {
            throw new RuntimeException("Disciplina não encontrada com ID: " + disciplina.getId());
        }
        
        if (!disciplinaExistente.get().getAtivo()) {
            throw new RuntimeException("Não é possível criar planejamento para uma disciplina inativa");
        }
        
        Optional<Turma> turmaExistente = turmaRepository.findById(turma.getId());
        if (turmaExistente.isEmpty()) {
            throw new RuntimeException("Turma não encontrada com ID: " + turma.getId());
        }
        
        if (!turmaExistente.get().getAtivo()) {
            throw new RuntimeException("Não é possível criar planejamento para uma turma inativa");
        }
    }
    
    /**
     * Verifica se já existe planejamento para a combinação disciplina/turma/semestre/ano
     */
    private void verificarPlanejamentoDuplicado(Planejamento planejamento) {
        verificarPlanejamentoDuplicado(planejamento, null);
    }
    
    /**
     * Verifica se já existe planejamento para a combinação disciplina/turma/semestre/ano (excluindo um planejamento específico)
     */
    private void verificarPlanejamentoDuplicado(Planejamento planejamento, Long planejamentoIdExcluir) {
        Optional<Planejamento> planejamentoExistente = planejamentoRepository
            .findByDisciplinaIdAndTurmaIdAndSemestreAndAno(
                planejamento.getDisciplina().getId(),
                planejamento.getTurma().getId(),
                planejamento.getSemestre().trim(),
                planejamento.getAno()
            );
        
        if (planejamentoExistente.isPresent() && 
            (planejamentoIdExcluir == null || !planejamentoExistente.get().getId().equals(planejamentoIdExcluir))) {
            throw new RuntimeException("Já existe um planejamento para esta disciplina, turma, semestre e ano");
        }
    }
}