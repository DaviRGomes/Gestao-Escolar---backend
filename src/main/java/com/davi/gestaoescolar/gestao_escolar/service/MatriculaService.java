package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.model.Aluno;
import com.davi.gestaoescolar.gestao_escolar.model.Matricula;
import com.davi.gestaoescolar.gestao_escolar.model.Turma;
import com.davi.gestaoescolar.gestao_escolar.model.enums.SituacaoMatricula;
import com.davi.gestaoescolar.gestao_escolar.repository.AlunoRepository;
import com.davi.gestaoescolar.gestao_escolar.repository.MatriculaRepository;
import com.davi.gestaoescolar.gestao_escolar.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;
    
    @Autowired
    private AlunoRepository alunoRepository;
    
    @Autowired
    private TurmaRepository turmaRepository;

    /**
     * Realiza uma nova matrícula
     */
    public Matricula matricular(Matricula matricula) {
        validarDadosMatricula(matricula);
        validarAlunoETurma(matricula.getAluno(), matricula.getTurma());
        verificarMatriculaDuplicada(matricula.getAluno().getId(), matricula.getTurma().getId());
        
        // Define a data de matrícula como hoje se não foi informada
        if (matricula.getDataMatricula() == null) {
            matricula.setDataMatricula(LocalDate.now());
        }
        
        // Define situação como ATIVA se não foi informada
        if (matricula.getSituacao() == null) {
            matricula.setSituacao(SituacaoMatricula.ATIVA);
        }
        
        return matriculaRepository.save(matricula);
    }

    /**
     * Atualiza uma matrícula existente
     */
    public Matricula atualizar(Long id, Matricula matriculaAtualizada) {
        Optional<Matricula> matriculaExistente = matriculaRepository.findById(id);
        if (matriculaExistente.isEmpty()) {
            throw new RuntimeException("Matrícula não encontrada com ID: " + id);
        }

        validarDadosMatricula(matriculaAtualizada);
        
        Matricula matricula = matriculaExistente.get();
        matricula.setDataMatricula(matriculaAtualizada.getDataMatricula());
        matricula.setSituacao(matriculaAtualizada.getSituacao());
        
        // Só permite alterar aluno e turma se a situação permitir
        if (matricula.getSituacao() != SituacaoMatricula.CANCELADA && 
            matricula.getSituacao() != SituacaoMatricula.CONCLUIDA) {
            
            if (!matricula.getAluno().getId().equals(matriculaAtualizada.getAluno().getId()) ||
                !matricula.getTurma().getId().equals(matriculaAtualizada.getTurma().getId())) {
                
                validarAlunoETurma(matriculaAtualizada.getAluno(), matriculaAtualizada.getTurma());
                verificarMatriculaDuplicada(matriculaAtualizada.getAluno().getId(), 
                                          matriculaAtualizada.getTurma().getId(), id);
                
                matricula.setAluno(matriculaAtualizada.getAluno());
                matricula.setTurma(matriculaAtualizada.getTurma());
            }
        }
        
        return matriculaRepository.save(matricula);
    }

    /**
     * Busca matrícula por ID
     */
    @Transactional(readOnly = true)
    public Optional<Matricula> buscarPorId(Long id) {
        return matriculaRepository.findById(id);
    }

    /**
     * Busca matrículas por aluno
     */
    @Transactional(readOnly = true)
    public List<Matricula> buscarPorAluno(Long alunoId) {
        if (alunoId == null) {
            throw new IllegalArgumentException("ID do aluno não pode ser nulo");
        }
        return matriculaRepository.findByAlunoId(alunoId);
    }

    /**
     * Busca matrículas por turma
     */
    @Transactional(readOnly = true)
    public List<Matricula> buscarPorTurma(Long turmaId) {
        if (turmaId == null) {
            throw new IllegalArgumentException("ID da turma não pode ser nulo");
        }
        return matriculaRepository.findByTurmaId(turmaId);
    }

    /**
     * Busca matrículas por situação
     */
    @Transactional(readOnly = true)
    public List<Matricula> buscarPorSituacao(SituacaoMatricula situacao) {
        if (situacao == null) {
            throw new IllegalArgumentException("Situação não pode ser nula");
        }
        return matriculaRepository.findBySituacao(situacao);
    }

    /**
     * Busca matrículas por data
     */
    @Transactional(readOnly = true)
    public List<Matricula> buscarPorData(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException("Data não pode ser nula");
        }
        return matriculaRepository.findByDataMatricula(data);
    }

    /**
     * Busca matrículas por período
     */
    @Transactional(readOnly = true)
    public List<Matricula> buscarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Datas de início e fim não podem ser nulas");
        }
        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data de início não pode ser posterior à data de fim");
        }
        return matriculaRepository.findByDataMatriculaBetween(dataInicio, dataFim);
    }

    /**
     * Busca matrículas ativas por turma
     */
    @Transactional(readOnly = true)
    public List<Matricula> buscarAtivasPorTurma(Long turmaId) {
        if (turmaId == null) {
            throw new IllegalArgumentException("ID da turma não pode ser nulo");
        }
        return matriculaRepository.findByTurmaIdAndSituacao(turmaId, SituacaoMatricula.ATIVA);
    }

    /**
     * Lista todas as matrículas
     */
    @Transactional(readOnly = true)
    public List<Matricula> listarTodas() {
        return matriculaRepository.findAll();
    }

    /**
     * Cancela uma matrícula
     */
    public void cancelar(Long id) {
        Optional<Matricula> matricula = matriculaRepository.findById(id);
        if (matricula.isEmpty()) {
            throw new RuntimeException("Matrícula não encontrada com ID: " + id);
        }
        
        Matricula matriculaEntity = matricula.get();
        if (matriculaEntity.getSituacao() == SituacaoMatricula.CANCELADA) {
            throw new RuntimeException("Matrícula já está cancelada");
        }
        
        matriculaEntity.setSituacao(SituacaoMatricula.CANCELADA);
        matriculaRepository.save(matriculaEntity);
    }

    /**
     * Suspende uma matrícula
     */
    public void suspender(Long id) {
        Optional<Matricula> matricula = matriculaRepository.findById(id);
        if (matricula.isEmpty()) {
            throw new RuntimeException("Matrícula não encontrada com ID: " + id);
        }
        
        Matricula matriculaEntity = matricula.get();
        if (matriculaEntity.getSituacao() != SituacaoMatricula.ATIVA) {
            throw new RuntimeException("Só é possível suspender matrículas ativas");
        }
        
        matriculaEntity.setSituacao(SituacaoMatricula.SUSPENSA);
        matriculaRepository.save(matriculaEntity);
    }

    /**
     * Reativa uma matrícula suspensa
     */
    public void reativar(Long id) {
        Optional<Matricula> matricula = matriculaRepository.findById(id);
        if (matricula.isEmpty()) {
            throw new RuntimeException("Matrícula não encontrada com ID: " + id);
        }
        
        Matricula matriculaEntity = matricula.get();
        if (matriculaEntity.getSituacao() != SituacaoMatricula.SUSPENSA) {
            throw new RuntimeException("Só é possível reativar matrículas suspensas");
        }
        
        matriculaEntity.setSituacao(SituacaoMatricula.ATIVA);
        matriculaRepository.save(matriculaEntity);
    }

    /**
     * Conclui uma matrícula
     */
    public void concluir(Long id) {
        Optional<Matricula> matricula = matriculaRepository.findById(id);
        if (matricula.isEmpty()) {
            throw new RuntimeException("Matrícula não encontrada com ID: " + id);
        }
        
        Matricula matriculaEntity = matricula.get();
        if (matriculaEntity.getSituacao() != SituacaoMatricula.ATIVA) {
            throw new RuntimeException("Só é possível concluir matrículas ativas");
        }
        
        matriculaEntity.setSituacao(SituacaoMatricula.CONCLUIDA);
        matriculaRepository.save(matriculaEntity);
    }

    /**
     * Deleta uma matrícula permanentemente
     */
    public void deletar(Long id) {
        if (!matriculaRepository.existsById(id)) {
            throw new RuntimeException("Matrícula não encontrada com ID: " + id);
        }
        matriculaRepository.deleteById(id);
    }

    /**
     * Valida os dados da matrícula
     */
    private void validarDadosMatricula(Matricula matricula) {
        if (matricula == null) {
            throw new IllegalArgumentException("Matrícula não pode ser nula");
        }
        
        if (matricula.getDataMatricula() != null && matricula.getDataMatricula().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de matrícula não pode ser futura");
        }
        
        if (matricula.getAluno() == null || matricula.getAluno().getId() == null) {
            throw new IllegalArgumentException("Aluno é obrigatório");
        }
        
        if (matricula.getTurma() == null || matricula.getTurma().getId() == null) {
            throw new IllegalArgumentException("Turma é obrigatória");
        }
    }
    
    /**
     * Valida se aluno e turma existem e estão ativos
     */
    private void validarAlunoETurma(Aluno aluno, Turma turma) {
        Optional<Aluno> alunoExistente = alunoRepository.findById(aluno.getId());
        if (alunoExistente.isEmpty()) {
            throw new RuntimeException("Aluno não encontrado com ID: " + aluno.getId());
        }
        
        if (!alunoExistente.get().getAtivo()) {
            throw new RuntimeException("Não é possível matricular um aluno inativo");
        }
        
        Optional<Turma> turmaExistente = turmaRepository.findById(turma.getId());
        if (turmaExistente.isEmpty()) {
            throw new RuntimeException("Turma não encontrada com ID: " + turma.getId());
        }
        
        if (!turmaExistente.get().getAtivo()) {
            throw new RuntimeException("Não é possível matricular em uma turma inativa");
        }
    }
    
    /**
     * Verifica se já existe matrícula ativa para o aluno na turma
     */
    private void verificarMatriculaDuplicada(Long alunoId, Long turmaId) {
        verificarMatriculaDuplicada(alunoId, turmaId, null);
    }
    
    /**
     * Verifica se já existe matrícula ativa para o aluno na turma (excluindo uma matrícula específica)
     */
    private void verificarMatriculaDuplicada(Long alunoId, Long turmaId, Long matriculaIdExcluir) {
        List<Matricula> matriculasExistentes = matriculaRepository.findByAlunoId(alunoId);
        
        for (Matricula matricula : matriculasExistentes) {
            if (matricula.getTurma().getId().equals(turmaId) && 
                matricula.getSituacao() == SituacaoMatricula.ATIVA &&
                (matriculaIdExcluir == null || !matricula.getId().equals(matriculaIdExcluir))) {
                throw new RuntimeException("Aluno já possui matrícula ativa nesta turma");
            }
        }
    }
}