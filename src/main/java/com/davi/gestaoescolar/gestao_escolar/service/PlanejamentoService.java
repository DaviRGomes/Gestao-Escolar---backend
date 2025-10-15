package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.dto.Disciplina.DisciplinaDtoSimples;
import com.davi.gestaoescolar.gestao_escolar.dto.Planejamento.PlanejamentoDtoIn;
import com.davi.gestaoescolar.gestao_escolar.dto.Planejamento.PlanejamentoDtoOut;
import com.davi.gestaoescolar.gestao_escolar.dto.Turma.TurmaDtoSimples;
import com.davi.gestaoescolar.gestao_escolar.exception.PlanejamentoException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
     * Metodo auxiliares
     */
    private List<PlanejamentoDtoOut> toDtos (List<Planejamento> planejamentos){
        return planejamentos.stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    private Optional<PlanejamentoDtoOut> toDTO(Optional<Planejamento> planejamento) {
        return planejamento.map(this::toDTO);
    }

    private PlanejamentoDtoOut toDTO(Planejamento planejamento){
        return new PlanejamentoDtoOut(
            planejamento.getId(),
            planejamento.getDescricao(),
            planejamento.getSemestre(),
            planejamento.getAno(),
            new DisciplinaDtoSimples(
                planejamento.getDisciplina() != null ? planejamento.getDisciplina().getId() : null,
                planejamento.getDisciplina() != null ? planejamento.getDisciplina().getNome() : null
            ),
            new TurmaDtoSimples(
                planejamento.getTurma() != null ? planejamento.getTurma().getId() : null, 
                planejamento.getTurma() != null ? planejamento.getTurma().getNome() : null
            ),
            planejamento.getDataCriacao(),
            planejamento.getDataAtualizacao()
        );
    }



    /**
     * Salva um novo planejamento
     */
    public PlanejamentoDtoIn salvar(PlanejamentoDtoIn planejamentoDTO) {
        validarDadosPlanejamento(planejamentoDTO);
        validarDisciplinaETurma(planejamentoDTO.getDisciplinaId(), planejamentoDTO.getTurmaId());
        verificarPlanejamentoDuplicado(planejamentoDTO);
        
        // Define a data de criação se não foi informada
        if (planejamentoDTO.getDataCriacao() == null) {
            planejamentoDTO.setDataCriacao(LocalDateTime.now());
        }



        // 🔹 Buscar entidades no banco
        Disciplina disciplina = disciplinaRepository.findById(
                        planejamentoDTO.getDisciplinaId())
                .orElseThrow(() -> new PlanejamentoException("Disciplina não encontrada"));

        Turma turma = turmaRepository.findById(
                        planejamentoDTO.getTurmaId())
                .orElseThrow(() -> new PlanejamentoException("Turma não encontrada"));

        // 🔹 Criar entidade Planejamento
        Planejamento planejamento = new Planejamento(
                planejamentoDTO.getDescricao(),
                planejamentoDTO.getSemestre(),
                planejamentoDTO.getAno(),
                planejamentoDTO.getDataCriacao(),
                null, // dataAtualizacao
                disciplina,
                turma,
                new ArrayList<>() // conteudos vazios no início
        );

        planejamentoRepository.save(planejamento);

        return planejamentoDTO;
    }

    /**
     * Atualiza um planejamento existente
     */
    public Planejamento atualizar(Long id, PlanejamentoDtoIn planejamentoAtualizado) {
        // Buscar planejamento existente
        Optional<Planejamento> planejamentoExistente = planejamentoRepository.findById(id);
        if (planejamentoExistente.isEmpty()) {
            throw new PlanejamentoException("Planejamento não encontrado com ID: " + id);
        }

        // Validar dados do DTO
        validarDadosPlanejamento(planejamentoAtualizado);

        Planejamento planejamento = planejamentoExistente.get();

        // Buscar entidades Disciplina e Turma no banco
        Disciplina disciplina = disciplinaRepository.findById(planejamentoAtualizado.getDisciplinaId())
                .orElseThrow(() -> new PlanejamentoException("Disciplina não encontrada"));
        Turma turma = turmaRepository.findById(planejamentoAtualizado.getTurmaId())
                .orElseThrow(() -> new PlanejamentoException("Turma não encontrada"));

        // Verifica se houve mudança nos campos únicos (disciplina, turma, semestre, ano)
        boolean mudouCamposUnicos = !planejamento.getDisciplina().getId().equals(disciplina.getId()) ||
                !planejamento.getTurma().getId().equals(turma.getId()) ||
                !planejamento.getSemestre().equals(planejamentoAtualizado.getSemestre()) ||
                !planejamento.getAno().equals(planejamentoAtualizado.getAno());

        if (mudouCamposUnicos) {
            // Valida se disciplina/turma já estão associadas a outro planejamento
            validarDisciplinaETurma(disciplina.getId(), turma.getId());
            verificarPlanejamentoDuplicado(planejamentoAtualizado, id);

            // Atualiza campos únicos
            planejamento.setDisciplina(disciplina);
            planejamento.setTurma(turma);
            planejamento.setSemestre(planejamentoAtualizado.getSemestre());
            planejamento.setAno(planejamentoAtualizado.getAno());
        }

        // Atualiza outros campos
        planejamento.setDescricao(planejamentoAtualizado.getDescricao());
        planejamento.setDataAtualizacao(LocalDateTime.now());

        // Salvar atualização no banco
        return planejamentoRepository.save(planejamento);
    }


    /**
     * Busca planejamento por ID
     */
    @Transactional(readOnly = true)
    public Optional<PlanejamentoDtoOut> buscarPorId(Long id) {


        return toDTO(planejamentoRepository.findById(id));
    }

    /**
     * Busca planejamentos por disciplina
     */
    @Transactional(readOnly = true)
    public List<PlanejamentoDtoOut> buscarPorDisciplina(Long disciplinaId) {
        if (disciplinaId == null) {
            throw new IllegalArgumentException("ID da disciplina não pode ser nulo");
        }
        List<Planejamento> planejamento = planejamentoRepository.findByDisciplinaId(disciplinaId);

        return toDtos(planejamento);

    }

    /**
     * Busca planejamentos por turma
     */
    @Transactional(readOnly = true)
    public List<PlanejamentoDtoOut> buscarPorTurma(Long turmaId) {
        if (turmaId == null) {
            throw new IllegalArgumentException("ID da turma não pode ser nulo");
        }
        return toDtos(planejamentoRepository.findByTurmaId(turmaId));
    }

    /**
     * Busca planejamentos por ano
     */
    @Transactional(readOnly = true)
    public List<PlanejamentoDtoOut> buscarPorAno(Integer ano) {
        if (ano == null) {
            throw new IllegalArgumentException("Ano não pode ser nulo");
        }
        return toDtos(planejamentoRepository.findByAno(ano));
    }

    /**
     * Busca planejamentos por semestre
     */
    @Transactional(readOnly = true)
    public List<PlanejamentoDtoOut> buscarPorSemestre(String semestre) {
        if (semestre == null || semestre.trim().isEmpty()) {
            throw new IllegalArgumentException("Semestre não pode ser vazio");
        }
        return toDtos(planejamentoRepository.findBySemestre(semestre.trim()));
    }

    /**
     * Busca planejamentos por ano e semestre
     */
    @Transactional(readOnly = true)
    public List<PlanejamentoDtoOut> buscarPorAnoESemestre(Integer ano, String semestre) {
        if (ano == null) {
            throw new IllegalArgumentException("Ano não pode ser nulo");
        }
        if (semestre == null || semestre.trim().isEmpty()) {
            throw new IllegalArgumentException("Semestre não pode ser vazio");
        }
        return toDtos(planejamentoRepository.findByAnoAndSemestre(ano, semestre.trim()));
    }

    /**
     * Busca planejamento específico por disciplina, turma, semestre e ano
     */
    @Transactional(readOnly = true)
    public Optional<PlanejamentoDtoOut> buscarEspecifico(Long disciplinaId, Long turmaId, String semestre, Integer ano) {
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

        Optional<Planejamento> planejamento = planejamentoRepository.findByDisciplinaIdAndTurmaIdAndSemestreAndAno(
                disciplinaId, turmaId, semestre.trim(), ano);
        if(planejamento.isPresent()){
            return toDTO(planejamento);
        }
        
        return null;
    }

    /**
     * Lista todos os planejamentos
     */
    @Transactional(readOnly = true)
    public List<PlanejamentoDtoOut> listarTodos() {

        return toDtos(planejamentoRepository.findAll());
    }

    /**
     * Deleta um planejamento permanentemente
     */
    public void deletar(Long id) {
        if (!planejamentoRepository.existsById(id)) {
            throw new PlanejamentoException("Planejamento não encontrado com ID: " + id);
        }
        planejamentoRepository.deleteById(id);  
        }
    
    private void validarDadosPlanejamento(PlanejamentoDtoIn planejamento) {
        if (planejamento == null) {
            throw new PlanejamentoException("Planejamento não pode ser nulo");
        }
        
        if (planejamento.getSemestre() == null || planejamento.getSemestre().trim().isEmpty()) {
            throw new IllegalArgumentException("Semestre é obrigatório");
        }
        
        if (planejamento.getAno() == null) {
            throw new IllegalArgumentException("Ano é obrigatório");
        }
        
        if (planejamento.getDisciplinaId() == null) {
            throw new IllegalArgumentException("Disciplina é obrigatória");
        }
        
        if (planejamento.getTurmaId() == null) {
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
    private void validarDisciplinaETurma(Long disciplinaId, Long turmaId) {
        Optional<Disciplina> disciplinaExistente = disciplinaRepository.findById(disciplinaId);
        if (disciplinaExistente.isEmpty()) {
            throw new PlanejamentoException("Disciplina não encontrada com ID: " + disciplinaId);
        }
        
        if (!disciplinaExistente.get().getAtivo()) {
            throw new PlanejamentoException("Não é possível criar planejamento para uma disciplina inativa");
        }
        
        Optional<Turma> turmaExistente = turmaRepository.findById(turmaId);
        if (turmaExistente.isEmpty()) {
            throw new PlanejamentoException("Turma não encontrada com ID: " + turmaId);
        }
        
        if (!turmaExistente.get().getAtivo()) {
            throw new PlanejamentoException("Não é possível criar planejamento para uma turma inativa");
        }
    }
    
    /**
     * Verifica se já existe planejamento para a combinação disciplina/turma/semestre/ano
     */
    private void verificarPlanejamentoDuplicado(PlanejamentoDtoIn planejamento) {
        verificarPlanejamentoDuplicado(planejamento, null);
    }
    
    /**
     * Verifica se já existe planejamento para a combinação disciplina/turma/semestre/ano (excluindo um planejamento específico)
     */
    private void verificarPlanejamentoDuplicado(PlanejamentoDtoIn planejamento, Long planejamentoIdExcluir) {
        Optional<Planejamento> planejamentoExistente = planejamentoRepository
            .findByDisciplinaIdAndTurmaIdAndSemestreAndAno(
                planejamento.getDisciplinaId(),
                planejamento.getTurmaId(),
                planejamento.getSemestre().trim(),
                planejamento.getAno()
            );
        
        if (planejamentoExistente.isPresent() && 
            (planejamentoIdExcluir == null || !planejamentoExistente.get().getId().equals(planejamentoIdExcluir))) {
            throw new PlanejamentoException("Já existe um planejamento para esta disciplina, turma, semestre e ano");
        }
    }
}