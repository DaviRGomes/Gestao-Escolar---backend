package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.dto.Aluno.AlunoDtoSimples;
import com.davi.gestaoescolar.gestao_escolar.dto.Matricula.MatriculaDtoIn;
import com.davi.gestaoescolar.gestao_escolar.dto.Matricula.MatriculaDtoOut;
import com.davi.gestaoescolar.gestao_escolar.dto.Turma.TurmaDtoSimples;
import com.davi.gestaoescolar.gestao_escolar.exception.MatriculaException;
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
import java.util.stream.Collectors;

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
    public MatriculaDtoOut salvarMatricula(MatriculaDtoIn dto) {
        validarDadosMatricula(dto);
        validarAlunoETurma(dto.getAlunoId(), dto.getTurmaId());
        verificarMatriculaDuplicada(dto.getAlunoId(), dto.getTurmaId());
        
        // Define a data de matrícula como hoje se não foi informada
        if (dto.getDataMatricula() == null) {
            dto.setDataMatricula(LocalDate.now());
        }
        
        // Define situação como ATIVA se não foi informada
        if (dto.getSituacao() == null) {
            dto.setSituacao(SituacaoMatricula.EM_PROCESSO);
        }
         
        Matricula matricula = new Matricula(
            dto.getDataMatricula(),
            dto.getSituacao(),
            alunoRepository.findById(dto.getAlunoId()).orElseThrow(() -> new MatriculaException("Aluno não encontrado")),
            turmaRepository.findById(dto.getTurmaId()).orElseThrow(() -> new MatriculaException("Turma não encontrada"))
        );

        return toDTO(matriculaRepository.save(matricula));
    }

    /**
     * Atualiza uma matrícula existente
     */
    public MatriculaDtoOut atualizarMatricula(Long id, MatriculaDtoIn matriculaAtualizada) {
        Optional<Matricula> matriculaExistente = matriculaRepository.findById(id);
        if (matriculaExistente.isEmpty()) {
            throw new RuntimeException("Matrícula não encontrada com ID: " + id);
        }

        validarDadosMatricula(matriculaAtualizada);
        
        Matricula matricula = matriculaExistente.get();
        matricula.setDataMatricula(matriculaAtualizada.getDataMatricula());
        matricula.setSituacao(matriculaAtualizada.getSituacao());
        
        // Só permite alterar aluno e turma se a situação permitir
        if (matricula.getSituacao() != SituacaoMatricula.DESATIVADO && 
            matricula.getSituacao() != SituacaoMatricula.DESATIVADO) {
            
            if (!matricula.getAluno().getId().equals(matriculaAtualizada.getAlunoId()) ||
                !matricula.getTurma().getId().equals(matriculaAtualizada.getTurmaId())) {
                
                validarAlunoETurma(matriculaAtualizada.getAlunoId(), matriculaAtualizada.getTurmaId());
                verificarMatriculaDuplicada(matriculaAtualizada.getAlunoId(), 
                                          matriculaAtualizada.getTurmaId(), id);
                
                matricula.setAluno(alunoRepository.findById(matriculaAtualizada.getAlunoId()).orElseThrow(() -> new MatriculaException("Aluno não encontrado")));
                matricula.setTurma(turmaRepository.findById(matriculaAtualizada.getTurmaId()).orElseThrow(() -> new MatriculaException("Turma não encontrada")));
            }
        }
        
        return toDTO(matriculaRepository.save(matricula));
    }

    /**
     * Busca matrícula por ID
     */
    @Transactional(readOnly = true)
    public Optional<MatriculaDtoOut> buscarPorId(Long id) {
        
        Optional<Matricula> matricula = matriculaRepository.findById(id);
        if (matricula.isEmpty()) {
            throw new MatriculaException.MatriculaNaoEncontradaException("Matrícula não encontrada com ID: " + id);
        }
        return toDTO(matricula);
    }

    /**
     * Busca matrículas por aluno
     */
    @Transactional(readOnly = true)
    public List<MatriculaDtoOut> buscarPorAluno(Long alunoId) {
        if (alunoId == null) {
            throw new IllegalArgumentException("ID do aluno não pode ser nulo");
        }
        List<Matricula> matriculas = matriculaRepository.findByAlunoId(alunoId);
        if (matriculas.isEmpty()) {
            throw new MatriculaException("Matrículas não encontradas para o aluno com ID: " + alunoId);
        }
        return matriculas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca matrículas por turma
     */
    @Transactional(readOnly = true)
    public List<MatriculaDtoOut> buscarPorTurma(Long turmaId) {
        if (turmaId == null) {
            throw new IllegalArgumentException("ID da turma não pode ser nulo");
        }
        List<Matricula> matriculas = matriculaRepository.findByTurmaId(turmaId);
        if (matriculas.isEmpty()) {
            throw new MatriculaException("Matrículas não encontradas para a turma com ID: " + turmaId);
        }
        return matriculas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca matrículas por situação
     */
    @Transactional(readOnly = true)
    public List<MatriculaDtoOut> buscarPorSituacao(SituacaoMatricula situacao) {
        if (situacao == null) {
            throw new IllegalArgumentException("Situação não pode ser nula");
        }
        List<Matricula> matriculas = matriculaRepository.findBySituacao(situacao);
        if (matriculas.isEmpty()) {
            throw new MatriculaException("Matrículas não encontradas para a situação: " + situacao);
        }
        return matriculas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca matrículas por data
     */
    @Transactional(readOnly = true)
    public List<MatriculaDtoOut> buscarPorData(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException("Data não pode ser nula");
        }
        List<Matricula> matriculas = matriculaRepository.findByDataMatricula(data);
        if (matriculas.isEmpty()) {
            throw new MatriculaException("Matrículas não encontradas para a data: " + data);
        }
        return matriculas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca matrículas por período
     */
    @Transactional(readOnly = true)
    public List<MatriculaDtoOut> buscarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Datas de início e fim não podem ser nulas");
        }
        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data de início não pode ser posterior à data de fim");
        }
        List<Matricula> matriculas = matriculaRepository.findByDataMatriculaBetween(dataInicio, dataFim);
        if (matriculas.isEmpty()) {
            throw new MatriculaException("Matrículas não encontradas para o período: " + dataInicio + " a " + dataFim);
        }
        return matriculas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca matrículas ativas por turma
     */
    @Transactional(readOnly = true)
    public List<MatriculaDtoOut> buscarAtivasPorTurma(Long turmaId) {
        if (turmaId == null) {
            throw new IllegalArgumentException("ID da turma não pode ser nulo");
        }
        List<Matricula> matriculas = matriculaRepository.findByTurmaIdAndSituacao(turmaId, SituacaoMatricula.ATIVA);
        if (matriculas.isEmpty()) {
            throw new MatriculaException("Matrículas não encontradas para a turma com ID: " + turmaId);
        }
        return matriculas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista todas as matrículas
     */
    @Transactional(readOnly = true)
    public List<MatriculaDtoOut> listarTodas() {
        List<Matricula> matriculas = matriculaRepository.findAll();
        return matriculas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    /**
     * Desativa uma matrícula
     */
    public void desativar(Long id) {
        Optional<Matricula> matricula = matriculaRepository.findById(id);
        if (matricula.isEmpty()) {
            throw new MatriculaException.MatriculaNaoEncontradaException("Matrícula não encontrada com ID: " + id);
        }
        
        Matricula matriculaEntity = matricula.get();
        if (matriculaEntity.getSituacao() == SituacaoMatricula.DESATIVADO) {
            throw new RuntimeException("Matrícula já está desativada");
        }
        
        matriculaEntity.setSituacao(SituacaoMatricula.DESATIVADO);
        matriculaRepository.save(matriculaEntity);
    }

    /**
     * Coloca uma matrícula em processo
     */
    public void colocarEmProcesso(Long id) {
        Optional<Matricula> matricula = matriculaRepository.findById(id);
        if (matricula.isEmpty()) {
            throw new MatriculaException.MatriculaNaoEncontradaException("Matrícula não encontrada com ID: " + id);
        }
        
        Matricula matriculaEntity = matricula.get();
        if (matriculaEntity.getSituacao() != SituacaoMatricula.ATIVA) {
            throw new RuntimeException("Só é possível colocar em processo matrículas ativas");
        }
        
        matriculaEntity.setSituacao(SituacaoMatricula.EM_PROCESSO);
        matriculaRepository.save(matriculaEntity);
    }

    /**
     * Ativa uma matrícula
     */
    public void ativar(Long id) {
        Optional<Matricula> matricula = matriculaRepository.findById(id);
        if (matricula.isEmpty()) {
            throw new MatriculaException.MatriculaNaoEncontradaException("Matrícula não encontrada com ID: " + id);
        }
        
        Matricula matriculaEntity = matricula.get();
        if (matriculaEntity.getSituacao() == SituacaoMatricula.ATIVA) {
            throw new RuntimeException("Matrícula já está ativa");
        }
        
        matriculaEntity.setSituacao(SituacaoMatricula.ATIVA);
        matriculaRepository.save(matriculaEntity);
    }

    /**
     * Finaliza uma matrícula (marca como desativada)
     */
    public void finalizar(Long id) {
        Optional<Matricula> matricula = matriculaRepository.findById(id);
        if (matricula.isEmpty()) {
            throw new RuntimeException("Matrícula não encontrada com ID: " + id);
        }
        
        Matricula matriculaEntity = matricula.get();
        if (matriculaEntity.getSituacao() != SituacaoMatricula.ATIVA) {
            throw new RuntimeException("Só é possível finalizar matrículas ativas");
        }
        
        matriculaEntity.setSituacao(SituacaoMatricula.DESATIVADO);
        matriculaRepository.save(matriculaEntity);
    }

    /**
     * Transfere um aluno para outra turma
     */
    public MatriculaDtoOut transferirTurma(Long matriculaId, Long novaTurmaId) {
        // Buscar a matrícula existente
        Optional<Matricula> matriculaOpt = matriculaRepository.findById(matriculaId);
        if (matriculaOpt.isEmpty()) {
            throw new MatriculaException.MatriculaNaoEncontradaException("Matrícula não encontrada com ID: " + matriculaId);
        }
        
        Matricula matricula = matriculaOpt.get();
        
        // Verificar se a matrícula está ativa
        if (matricula.getSituacao() != SituacaoMatricula.ATIVA) {
            throw new RuntimeException("Só é possível transferir matrículas ativas");
        }
        
        // Buscar a nova turma
        Optional<Turma> novaTurmaOpt = turmaRepository.findById(novaTurmaId);
        if (novaTurmaOpt.isEmpty()) {
            throw new RuntimeException("Turma não encontrada com ID: " + novaTurmaId);
        }
        
        Turma novaTurma = novaTurmaOpt.get();
        
        // Verificar se a turma está ativa
        if (!novaTurma.getAtivo()) {
            throw new RuntimeException("Não é possível transferir para uma turma inativa");
        }
        
        // Verificar se já existe matrícula do aluno na nova turma
        verificarMatriculaDuplicada(matricula.getAluno().getId(), novaTurmaId, matriculaId);
        
        // Atualizar a turma da matrícula
        matricula.setTurma(novaTurma);
        
        return toDTO(matriculaRepository.save(matricula));
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
    private void validarDadosMatricula(MatriculaDtoIn matricula) {
        if (matricula == null) {
            throw new IllegalArgumentException("Matrícula não pode ser nula");
        }
        
        if (matricula.getDataMatricula() != null && matricula.getDataMatricula().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de matrícula não pode ser futura");
        }
        
        if (matricula.getAlunoId() == null) {
            throw new IllegalArgumentException("Aluno é obrigatório");
        }
        
        if (matricula.getTurmaId() == null) {
            throw new IllegalArgumentException("Turma é obrigatória");
        }
    }
    
    /**
     * Valida se aluno e turma existem e estão ativos
     */
    private void validarAlunoETurma(Long alunoId, Long turmaId) {
        Optional<Aluno> alunoExistente = alunoRepository.findById(alunoId);
        if (alunoExistente.isEmpty()) {
            throw new RuntimeException("Aluno não encontrado com ID: " + alunoId);
        }
        
        if (!alunoExistente.get().getAtivo()) {
            throw new RuntimeException("Não é possível matricular um aluno inativo");
        }
        
        Optional<Turma> turmaExistente = turmaRepository.findById(turmaId);
        if (turmaExistente.isEmpty()) {
            throw new RuntimeException("Turma não encontrada com ID: " + turmaId);
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

    /**
     * Método privado para converter Matricula para MatriculaDtoOut
     */
    private MatriculaDtoOut toDTO(Matricula matricula) {
        AlunoDtoSimples alunoDto = new AlunoDtoSimples(
                matricula.getAluno().getId(),
                matricula.getAluno().getNome()
                );

        TurmaDtoSimples turmaDto = new TurmaDtoSimples(
                matricula.getTurma().getId(),
                matricula.getTurma().getNome()
        );

        return new MatriculaDtoOut(
                matricula.getId(),
                matricula.getDataMatricula(),
                matricula.getSituacao(),
                alunoDto,
                turmaDto
        );
    }

    private Optional<MatriculaDtoOut> toDTO(Optional<Matricula> matricula) {
        return matricula.map(this::toDTO);
    }

    private List<MatriculaDtoOut> toDTO(List<Matricula> matriculas) {
        return matriculas.stream().map(this::toDTO).collect(Collectors.toList());
    }
}