package com.davi.gestaoescolar.gestao_escolar.service;

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

    /**
     * Salva uma nova presença
     */
    public Presenca salvar(Presenca presenca) {
        validarDadosPresenca(presenca);
        validarAlunoERegistroAula(presenca.getAluno(), presenca.getRegistroAula());
        
        // Verifica se já existe presença para este aluno nesta aula
        List<Presenca> presencasExistentes = presencaRepository.findByAlunoIdAndRegistroAulaId(
            presenca.getAluno().getId(), presenca.getRegistroAula().getId());
        
        if (!presencasExistentes.isEmpty()) {
            throw new RuntimeException("Já existe registro de presença para este aluno nesta aula");
        }
        
        return presencaRepository.save(presenca);
    }

    /**
     * Atualiza uma presença existente
     */
    public Presenca atualizar(Long id, Presenca presencaAtualizada) {
        Optional<Presenca> presencaExistente = presencaRepository.findById(id);
        if (presencaExistente.isEmpty()) {
            throw new RuntimeException("Presença não encontrada com ID: " + id);
        }

        validarDadosPresenca(presencaAtualizada);
        validarAlunoERegistroAula(presencaAtualizada.getAluno(), presencaAtualizada.getRegistroAula());
        
        Presenca presenca = presencaExistente.get();
        presenca.setPresente(presencaAtualizada.getPresente());
        presenca.setJustificativa(presencaAtualizada.getJustificativa());
        presenca.setAluno(presencaAtualizada.getAluno());
        presenca.setRegistroAula(presencaAtualizada.getRegistroAula());
        
        return presencaRepository.save(presenca);
    }

    /**
     * Busca presença por ID
     */
    @Transactional(readOnly = true)
    public Optional<Presenca> buscarPorId(Long id) {
        return presencaRepository.findById(id);
    }

    /**
     * Busca presenças por aluno
     */
    @Transactional(readOnly = true)
    public List<Presenca> buscarPorAluno(Long alunoId) {
        if (alunoId == null) {
            throw new IllegalArgumentException("ID do aluno não pode ser nulo");
        }
        return presencaRepository.findByAlunoId(alunoId);
    }

    /**
     * Busca presenças por registro de aula
     */
    @Transactional(readOnly = true)
    public List<Presenca> buscarPorRegistroAula(Long registroAulaId) {
        if (registroAulaId == null) {
            throw new IllegalArgumentException("ID do registro de aula não pode ser nulo");
        }
        return presencaRepository.findByRegistroAulaId(registroAulaId);
    }

    /**
     * Busca presenças por status (presente/ausente)
     */
    @Transactional(readOnly = true)
    public List<Presenca> buscarPorStatus(Boolean presente) {
        if (presente == null) {
            throw new IllegalArgumentException("Status de presença não pode ser nulo");
        }
        return presencaRepository.findByPresente(presente);
    }

    /**
     * Busca presenças de um aluno por status
     */
    @Transactional(readOnly = true)
    public List<Presenca> buscarPorAlunoEStatus(Long alunoId, Boolean presente) {
        if (alunoId == null) {
            throw new IllegalArgumentException("ID do aluno não pode ser nulo");
        }
        if (presente == null) {
            throw new IllegalArgumentException("Status de presença não pode ser nulo");
        }
        return presencaRepository.findByAlunoIdAndPresente(alunoId, presente);
    }

    /**
     * Busca presenças de uma aula por status
     */
    @Transactional(readOnly = true)
    public List<Presenca> buscarPorRegistroAulaEStatus(Long registroAulaId, Boolean presente) {
        if (registroAulaId == null) {
            throw new IllegalArgumentException("ID do registro de aula não pode ser nulo");
        }
        if (presente == null) {
            throw new IllegalArgumentException("Status de presença não pode ser nulo");
        }
        return presencaRepository.findByRegistroAulaIdAndPresente(registroAulaId, presente);
    }

    /**
     * Busca presença específica de um aluno em uma aula
     */
    @Transactional(readOnly = true)
    public List<Presenca> buscarPorAlunoERegistroAula(Long alunoId, Long registroAulaId) {
        if (alunoId == null) {
            throw new IllegalArgumentException("ID do aluno não pode ser nulo");
        }
        if (registroAulaId == null) {
            throw new IllegalArgumentException("ID do registro de aula não pode ser nulo");
        }
        return presencaRepository.findByAlunoIdAndRegistroAulaId(alunoId, registroAulaId);
    }

    /**
     * Lista todas as presenças
     */
    @Transactional(readOnly = true)
    public List<Presenca> listarTodas() {
        return presencaRepository.findAll();
    }

    /**
     * Calcula a frequência de um aluno (percentual de presenças)
     */
    @Transactional(readOnly = true)
    public BigDecimal calcularFrequenciaAluno(Long alunoId) {
        List<Presenca> todasPresencas = buscarPorAluno(alunoId);
        
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
        List<Presenca> presencasAula = buscarPorRegistroAula(registroAulaId);
        
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
     * Marca presença de um aluno
     */
    public Presenca marcarPresenca(Long alunoId, Long registroAulaId) {
        return criarPresencaBasica(alunoId, registroAulaId, true, null);
    }

    /**
     * Marca falta de um aluno
     */
    public Presenca marcarFalta(Long alunoId, Long registroAulaId, String justificativa) {
        return criarPresencaBasica(alunoId, registroAulaId, false, justificativa);
    }

    /**
     * Altera status de presença
     */
    public Presenca alterarStatusPresenca(Long presencaId, Boolean novoStatus, String justificativa) {
        Optional<Presenca> presencaExistente = presencaRepository.findById(presencaId);
        if (presencaExistente.isEmpty()) {
            throw new RuntimeException("Presença não encontrada com ID: " + presencaId);
        }
        
        Presenca presenca = presencaExistente.get();
        presenca.setPresente(novoStatus);
        presenca.setJustificativa(justificativa);
        
        return presencaRepository.save(presenca);
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
     * Cria uma presença básica
     */
    public Presenca criarPresencaBasica(Long alunoId, Long registroAulaId, Boolean presente, String justificativa) {
        Optional<Aluno> aluno = alunoRepository.findById(alunoId);
        if (aluno.isEmpty()) {
            throw new RuntimeException("Aluno não encontrado com ID: " + alunoId);
        }
        
        Optional<RegistroAula> registroAula = registroAulaRepository.findById(registroAulaId);
        if (registroAula.isEmpty()) {
            throw new RuntimeException("Registro de aula não encontrado com ID: " + registroAulaId);
        }
        
        Presenca presenca = new Presenca();
        presenca.setAluno(aluno.get());
        presenca.setRegistroAula(registroAula.get());
        presenca.setPresente(presente);
        presenca.setJustificativa(justificativa);
        
        return salvar(presenca);
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
    private void validarDadosPresenca(Presenca presenca) {
        if (presenca == null) {
            throw new IllegalArgumentException("Presença não pode ser nula");
        }
        
        if (presenca.getPresente() == null) {
            throw new IllegalArgumentException("Status de presença é obrigatório");
        }
        
        if (presenca.getAluno() == null || presenca.getAluno().getId() == null) {
            throw new IllegalArgumentException("Aluno é obrigatório");
        }
        
        if (presenca.getRegistroAula() == null || presenca.getRegistroAula().getId() == null) {
            throw new IllegalArgumentException("Registro de aula é obrigatório");
        }
        
        // Validação da justificativa para faltas
        if (!presenca.getPresente() && 
            (presenca.getJustificativa() == null || presenca.getJustificativa().trim().isEmpty())) {
            // Justificativa não é obrigatória, mas recomendada para faltas
        }
        
        // Validação do tamanho da justificativa
        if (presenca.getJustificativa() != null && presenca.getJustificativa().length() > 500) {
            throw new IllegalArgumentException("Justificativa não pode exceder 500 caracteres");
        }
    }
    
    /**
     * Valida se aluno e registro de aula existem e estão ativos
     */
    private void validarAlunoERegistroAula(Aluno aluno, RegistroAula registroAula) {
        Optional<Aluno> alunoExistente = alunoRepository.findById(aluno.getId());
        if (alunoExistente.isEmpty()) {
            throw new RuntimeException("Aluno não encontrado com ID: " + aluno.getId());
        }
        
        if (!alunoExistente.get().getAtivo()) {
            throw new RuntimeException("Não é possível registrar presença para um aluno inativo");
        }
        
        Optional<RegistroAula> registroExistente = registroAulaRepository.findById(registroAula.getId());
        if (registroExistente.isEmpty()) {
            throw new RuntimeException("Registro de aula não encontrado com ID: " + registroAula.getId());
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