package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.dto.Aluno.AlunoDtoOut;
import com.davi.gestaoescolar.gestao_escolar.dto.Presenca.PresencaDtoIn;
import com.davi.gestaoescolar.gestao_escolar.dto.Presenca.PresencaDtoOut;
import com.davi.gestaoescolar.gestao_escolar.dto.RegistroAula.RegistroAulaDtoOut;
import com.davi.gestaoescolar.gestao_escolar.model.Aluno;
import com.davi.gestaoescolar.gestao_escolar.model.Presenca;
import com.davi.gestaoescolar.gestao_escolar.model.RegistroAula;
import com.davi.gestaoescolar.gestao_escolar.repository.AlunoRepository;
import com.davi.gestaoescolar.gestao_escolar.repository.PresencaRepository;
import com.davi.gestaoescolar.gestao_escolar.repository.RegistroAulaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PresencaService {

    @Autowired
    private PresencaRepository presencaRepository;
    
    @Autowired
    private AlunoRepository alunoRepository;
    
    @Autowired
    private RegistroAulaRepository registroAulaRepository;

    public PresencaDtoOut toDTO(Presenca presenca) {
        return new PresencaDtoOut(
                presenca.getId(),
                presenca.getPresente(),
                presenca.getJustificativa(),
                new AlunoDtoOut(
                        presenca.getAluno() != null ? presenca.getAluno().getId() : null,
                        presenca.getAluno() != null ? presenca.getAluno().getNome() : null
                ),
                new RegistroAulaDtoOut(
                        presenca.getRegistroAula() != null ? presenca.getRegistroAula().getId() : null,
                        presenca.getRegistroAula() != null ? presenca.getRegistroAula().getData() : null,
                        presenca.getRegistroAula() != null ? presenca.getRegistroAula().getDescricao() : null
                )
        );
    }

    /**
     * Salva uma nova presença
     */
    public PresencaDtoOut salvar(PresencaDtoIn dtoIn) {
        validarDadosPresenca(dtoIn);
        validarAlunoERegistroAula(dtoIn.getAlunoId(), dtoIn.getRegistroAulaId());
        
        // Verifica se já existe presença para este aluno nesta aula
        List<Presenca> presencasExistentes = presencaRepository.findByAlunoIdAndRegistroAulaId(
            dtoIn.getAlunoId(), dtoIn.getRegistroAulaId());
        
        if (!presencasExistentes.isEmpty()) {
            throw new RuntimeException("Já existe registro de presença para este aluno nesta aula");
        }
        Aluno aluno = alunoRepository.findById(dtoIn.getAlunoId()).orElse(null);
        RegistroAula registroAula = registroAulaRepository.findById(dtoIn.getRegistroAulaId()).orElse(null);

        Presenca presenca = new Presenca(
            dtoIn.getPresente(),
            dtoIn.getJustificativa(),
            registroAula,
            aluno
        );

       return toDTO(presencaRepository.save(presenca));
    }

    /**
     * Atualiza uma presença existente
     */
    public PresencaDtoOut atualizar(Long id, PresencaDtoIn presencaAtualizada) {
        Optional<Presenca> presencaExistente = presencaRepository.findById(id);
        if (presencaExistente.isEmpty()) {
            throw new RuntimeException("Presença não encontrada com ID: " + id);
        }

        validarDadosPresenca(presencaAtualizada);
        validarAlunoERegistroAula(presencaAtualizada.getAlunoId(), presencaAtualizada.getRegistroAulaId());
        
        Presenca presenca = presencaExistente.get();
        presenca.setPresente(presencaAtualizada.getPresente());
        presenca.setJustificativa(presencaAtualizada.getJustificativa());
        presenca.setAluno(alunoRepository.findById(presencaAtualizada.getAlunoId()).orElse(null));
        presenca.setRegistroAula(registroAulaRepository.findById(presencaAtualizada.getRegistroAulaId()).orElse(null));
        
        return toDTO(presencaRepository.save(presenca));
    }

    /**
     * Busca presença por ID
     */
    @Transactional(readOnly = true)
    public Optional<PresencaDtoOut> buscarPorId(Long id) {
        Optional<Presenca> presenca = presencaRepository.findById(id);
        return presenca.map(this::toDTO);
    }

    /**
     * Busca presenças por aluno
     */
    @Transactional(readOnly = true)
    public List<PresencaDtoOut> buscarPorAluno(Long alunoId) {
        if (alunoId == null) {
            throw new IllegalArgumentException("ID do aluno não pode ser nulo");
        }
        return presencaRepository.
                findByAlunoId(alunoId).
                stream().map(this::toDTO).
                toList();
    }

    /**
     * Busca presenças por registro de aula
     */
    @Transactional(readOnly = true)
    public List<PresencaDtoOut> buscarPorRegistroAula(Long registroAulaId) {

        if (registroAulaId == null) {
            throw new IllegalArgumentException("ID do registro de aula não pode ser nulo");
        }
        return presencaRepository.
                findByRegistroAulaId(registroAulaId).
                stream().map(this::toDTO).
                toList();
    }

    /**
     * Busca presenças por status (presente/ausente)
     */
    @Transactional(readOnly = true)
    public List<PresencaDtoOut> buscarPorStatus(Boolean presente) {
        if (presente == null) {
            throw new IllegalArgumentException("Status de presença não pode ser nulo");
        }
        return presencaRepository.findByPresente(presente).
                stream().map(this::toDTO).
                toList();
    }

    /**
     * Busca presenças de um aluno por status
     */
    @Transactional(readOnly = true)
    public List<PresencaDtoOut> buscarPorAlunoEStatus(Long alunoId, Boolean presente) {
        if (alunoId == null) {
            throw new IllegalArgumentException("ID do aluno não pode ser nulo");
        }
        if (presente == null) {
            throw new IllegalArgumentException("Status de presença não pode ser nulo");
        }
        return presencaRepository.findByAlunoIdAndPresente(alunoId, presente).
                stream().map(this::toDTO).
                toList();
    }

    /**
     * Busca presenças de uma aula por status
     */
    @Transactional(readOnly = true)
    public List<PresencaDtoOut> buscarPorRegistroAulaEStatus(Long registroAulaId, Boolean presente) {
        if (registroAulaId == null) {
            throw new IllegalArgumentException("ID do registro de aula não pode ser nulo");
        }
        if (presente == null) {
            throw new IllegalArgumentException("Status de presença não pode ser nulo");
        }
        return presencaRepository.findByRegistroAulaIdAndPresente(registroAulaId, presente).
                stream().map(this::toDTO).
                toList();
    }

    /**
     * Busca presença específica de um aluno em uma aula
     */
    @Transactional(readOnly = true)
    public List<PresencaDtoOut> buscarPorAlunoERegistroAula(Long alunoId, Long registroAulaId) {
        if (alunoId == null) {
            throw new IllegalArgumentException("ID do aluno não pode ser nulo");
        }
        if (registroAulaId == null) {
            throw new IllegalArgumentException("ID do registro de aula não pode ser nulo");
        }
        return presencaRepository.findByAlunoIdAndRegistroAulaId(alunoId, registroAulaId).
                stream().map(this::toDTO).
                toList();
    }

    /**
     * Lista todas as presenças
     */
    @Transactional(readOnly = true)
    public List<PresencaDtoOut> listarTodas() {
        return presencaRepository.findAll().
                stream().map(this::toDTO).
                toList();
    }

    /**
     * Calcula a frequência de um aluno (percentual de presenças)
     */
    @Transactional(readOnly = true)
    public BigDecimal calcularFrequenciaAluno(Long alunoId) {
        List<PresencaDtoOut> todasPresencas = buscarPorAluno(alunoId);  
        
        if (todasPresencas.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        long presencas = todasPresencas.stream()
            .mapToLong(p -> p.getPresente() ? 1 : 0)
            .sum();
        
        BigDecimal percentual = BigDecimal.valueOf(presencas)
            .multiply(BigDecimal.valueOf(100))
            .divide(BigDecimal.valueOf(todasPresencas.size()), 2, RoundingMode.HALF_UP);
        
        return percentual;
    }

    /**
     * Calcula a frequência da turma em uma aula específica
     */
    @Transactional(readOnly = true)
    public BigDecimal calcularFrequenciaTurma(Long registroAulaId) {
        List<PresencaDtoOut> presencasAula = buscarPorRegistroAula(registroAulaId);
        
        if (presencasAula.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        long presentes = presencasAula.stream()
            .mapToLong(p -> p.getPresente() ? 1 : 0)
            .sum();
        
        BigDecimal percentual = BigDecimal.valueOf(presentes)
            .multiply(BigDecimal.valueOf(100))
            .divide(BigDecimal.valueOf(presencasAula.size()), 2, RoundingMode.HALF_UP);
        
        return percentual;
    }

    /**
     * Conta total de presenças de um aluno
     */
    @Transactional(readOnly = true)
    public long contarPresencasAluno(Long alunoId) {
        return buscarPorAlunoEStatus(alunoId, true).size();
    }

    /**
     * Conta total de faltas de um aluno
     */
    @Transactional(readOnly = true)
    public long contarFaltasAluno(Long alunoId) {
        return buscarPorAlunoEStatus(alunoId, false).size();
    }

    /**
     * Conta total de aulas de um aluno
     */
    @Transactional(readOnly = true)
    public long contarTotalAulasAluno(Long alunoId) {
        return buscarPorAluno(alunoId).size();
    }

    /**
     * Verifica se aluno tem frequência mínima (75%)
     */
    @Transactional(readOnly = true)
    public boolean verificarFrequenciaMinima(Long alunoId) {
        BigDecimal frequencia = calcularFrequenciaAluno(alunoId);
        return frequencia.compareTo(new BigDecimal("75.0")) >= 0;
    }

    /**
     * Deleta uma presença permanentemente
     */
    public void deletar(Long id) {
        if (!presencaRepository.existsById(id)) {
            throw new RuntimeException("Presença não encontrada com ID: " + id);
        }
        presencaRepository.deleteById(id);
    }

    /**
     * Busca alunos com baixa frequência (abaixo de 75%)
     */
    @Transactional(readOnly = true)
    public List<Long> buscarAlunosComBaixaFrequencia() {
        return alunoRepository.findAll().stream()
            .filter(aluno -> {
                BigDecimal frequencia = calcularFrequenciaAluno(aluno.getId());
                return frequencia.compareTo(new BigDecimal("75.0")) < 0;
            })
            .map(Aluno::getId)
            .toList();
    }

    /**
     * Gera relatório de frequência de um aluno
     */
    @Transactional(readOnly = true)
    public String gerarRelatorioFrequencia(Long alunoId) {
        long totalAulas = contarTotalAulasAluno(alunoId);
        long presencas = contarPresencasAluno(alunoId);
        long faltas = contarFaltasAluno(alunoId);
        BigDecimal frequencia = calcularFrequenciaAluno(alunoId);
        boolean frequenciaMinima = verificarFrequenciaMinima(alunoId);
        
        return String.format(
            "Relatório de Frequência:\n" +
            "Total de aulas: %d\n" +
            "Presenças: %d\n" +
            "Faltas: %d\n" +
            "Frequência: %.2f%%\n" +
            "Frequência mínima atingida: %s",
            totalAulas, presencas, faltas, frequencia, 
            frequenciaMinima ? "Sim" : "Não"
        );
    }

    /**
     * Valida os dados da presença
     */
    private void validarDadosPresenca(PresencaDtoIn dtoIn) {
        if (dtoIn == null) {
            throw new IllegalArgumentException("Presença não pode ser nula");
        }
        
        if (dtoIn.getPresente() == null) {
            throw new IllegalArgumentException("Status de presença é obrigatório");
        }
        
        if (dtoIn.getAlunoId() == null) {
            throw new IllegalArgumentException("Aluno é obrigatório");
        }
        
        if (dtoIn.getRegistroAulaId() == null) {
            throw new IllegalArgumentException("Registro de aula é obrigatório");
        }
        
        // Validação da justificativa para faltas
        if (!dtoIn.getPresente() && 
            (dtoIn.getJustificativa() == null || dtoIn.getJustificativa().trim().isEmpty())) {
            // Justificativa não é obrigatória, mas recomendada para faltas
        }
        
        // Validação do tamanho da justificativa
        if (dtoIn.getJustificativa() != null && dtoIn.getJustificativa().length() > 500) {
            throw new IllegalArgumentException("Justificativa não pode exceder 500 caracteres");
        }
    }
    
    /**
     * Valida se aluno e registro de aula existem e estão ativos
     */
    private void validarAlunoERegistroAula(long alunoId, long  registroAulaId) {
        Optional<Aluno> alunoExistente = alunoRepository.findById(alunoId);
        if (alunoExistente.isEmpty()) {
            throw new RuntimeException("Aluno não encontrado com ID: " + alunoId);
        }
        
        if (!alunoExistente.get().getAtivo()) {
            throw new RuntimeException("Não é possível registrar presença para um aluno inativo");
        }
        
        Optional<RegistroAula> registroExistente = registroAulaRepository.findById(registroAulaId);
        if (registroExistente.isEmpty()) {
            throw new RuntimeException("Registro de aula não encontrado com ID: " + registroAulaId);
        }
        
        // Verifica se o aluno está matriculado na turma do registro de aula
        boolean alunoMatriculado = alunoExistente.get().getMatriculas().stream()
            .anyMatch(matricula -> matricula.getTurma().getId().equals(registroExistente.get().getTurma().getId()) &&
                                 matricula.getSituacao().name().equals("ATIVA"));
        
        if (!alunoMatriculado) {
            throw new RuntimeException("Aluno não está matriculado na turma do registro de aula");
        }
    }
}