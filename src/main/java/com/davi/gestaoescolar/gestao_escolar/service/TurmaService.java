package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.model.Turma;
import com.davi.gestaoescolar.gestao_escolar.model.enums.Periodo;
import com.davi.gestaoescolar.gestao_escolar.repository.TurmaRepository;
import com.davi.gestaoescolar.gestao_escolar.dto.Turma.TurmaDtoIn;
import com.davi.gestaoescolar.gestao_escolar.dto.Turma.TurmaDtoOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TurmaService {

    @Autowired
    private TurmaRepository turmaRepository;



    // Conversão de entidade -> DTO
    private TurmaDtoOut toDTO(Turma turma) {
        return new TurmaDtoOut(
            turma.getId(),
            turma.getNome(),
            turma.getAnoLetivo(),
            turma.getSemestre(),
            turma.getPeriodo(),
            turma.getAtivo()
        );
    }


    /**
     * Salva uma nova turma usando DTO (espelhado no AlunoService)
     */
    public TurmaDtoOut salvar(TurmaDtoIn dtoIn) {
        validarDadosTurma(dtoIn);

        Turma turma = new Turma();
        turma.setNome(dtoIn.getNome());
        turma.setAnoLetivo(dtoIn.getAnoLetivo());
        turma.setSemestre(dtoIn.getSemestre());
        turma.setPeriodo(dtoIn.getPeriodo());
        if (dtoIn.getAtivo() != null) {
            turma.setAtivo(dtoIn.getAtivo());   
        }

        Turma turmaSalva = turmaRepository.save(turma);
        return toDTO(turmaSalva);
    }

    /**
     * Atualiza uma turma existente
     */
    public TurmaDtoOut atualizar(Long id, TurmaDtoIn dtoIn) {
        Optional<Turma> turmaExistente = turmaRepository.findById(id);
        if (turmaExistente.isEmpty()) {
            throw new RuntimeException("Turma não encontrada com ID: " + id);
        }

        validarDadosTurma(dtoIn);

        Turma turma = turmaExistente.get();
        turma.setNome(dtoIn.getNome());
        turma.setAnoLetivo(dtoIn.getAnoLetivo());           
        turma.setSemestre(dtoIn.getSemestre());
        turma.setPeriodo(dtoIn.getPeriodo());
        turma.setAtivo(dtoIn.getAtivo() != null ? dtoIn.getAtivo() : turma.getAtivo());

        return toDTO(turmaRepository.save(turma));
    }

    /**
     * Busca turma por ID
     */
    @Transactional(readOnly = true)
    public Optional<TurmaDtoOut> buscarPorId(Long id) {
        return turmaRepository.findById(id).map(this::toDTO);
    }

    /**
     * Busca turmas por nome (busca parcial, case insensitive)
     */
    @Transactional(readOnly = true)
    public List<TurmaDtoOut> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        return turmaRepository.findByNomeContainingIgnoreCase(nome.trim())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca turmas por ano letivo
     */
    @Transactional(readOnly = true)
    public List<TurmaDtoOut> buscarPorAnoLetivo(String anoLetivo) {
        if (anoLetivo == null || anoLetivo.trim().isEmpty()) {
            throw new IllegalArgumentException("Ano letivo não pode ser vazio");
        }
        return turmaRepository.findByAnoLetivo(anoLetivo.trim())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca turmas por semestre
     */
    @Transactional(readOnly = true)
    public List<TurmaDtoOut> buscarPorSemestre(String semestre) {
        if (semestre == null || semestre.trim().isEmpty()) {
            throw new IllegalArgumentException("Semestre não pode ser vazio");
        }
        return turmaRepository.findBySemestre(semestre.trim())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca turmas por período
     */
    @Transactional(readOnly = true)
    public List<TurmaDtoOut> buscarPorPeriodo(Periodo periodo) {
        if (periodo == null) {
            throw new IllegalArgumentException("Período não pode ser nulo");
        }
        return turmaRepository.findByPeriodo(periodo)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca turmas por ano letivo e semestre
     */
    @Transactional(readOnly = true)
    public List<TurmaDtoOut> buscarPorAnoLetivoESemestre(String anoLetivo, String semestre) {
        if (anoLetivo == null || anoLetivo.trim().isEmpty()) {
            throw new IllegalArgumentException("Ano letivo não pode ser vazio");
        }
        if (semestre == null || semestre.trim().isEmpty()) {
            throw new IllegalArgumentException("Semestre não pode ser vazio");
        }
        return turmaRepository.findByAnoLetivoAndSemestre(anoLetivo.trim(), semestre.trim())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista todas as turmas
     */
    @Transactional(readOnly = true)
    public List<TurmaDtoOut> listarTodas() {
        return turmaRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista apenas turmas ativas
     */
    @Transactional(readOnly = true)
    public List<TurmaDtoOut> listarAtivas() {
        return turmaRepository.findByAtivo(true)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista apenas turmas inativas
     */
    @Transactional(readOnly = true)
    public List<TurmaDtoOut> listarInativas() {
        return turmaRepository.findByAtivo(false)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
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
     * Valida os dados da turma usando DTO
     */
    private void validarDadosTurma(TurmaDtoIn dtoIn) {
        if (dtoIn == null) {
            throw new IllegalArgumentException("Turma não pode ser nula");
        }

        if (dtoIn.getNome() == null || dtoIn.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da turma é obrigatório");
        }

        if (dtoIn.getAnoLetivo() == null || dtoIn.getAnoLetivo().trim().isEmpty()) {
            throw new IllegalArgumentException("Ano letivo é obrigatório");
        }

        if (dtoIn.getSemestre() == null || dtoIn.getSemestre().trim().isEmpty()) {
            throw new IllegalArgumentException("Semestre é obrigatório");
        }

        if (dtoIn.getPeriodo() == null) {
            throw new IllegalArgumentException("Período é obrigatório");
        }

        try {
            int ano = Integer.parseInt(dtoIn.getAnoLetivo().trim());
            if (ano < 1900 || ano > 2100) {
                throw new IllegalArgumentException("Ano letivo deve estar entre 1900 e 2100");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ano letivo deve ser um número válido");
        }

        String semestre = dtoIn.getSemestre().trim();
        if (!semestre.equals("1") && !semestre.equals("2")) {
            throw new IllegalArgumentException("Semestre deve ser '1' ou '2'");
        }
    }
}