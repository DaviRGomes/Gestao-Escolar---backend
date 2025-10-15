package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.dto.Comportamento.ComportamentoDtoIn;
import com.davi.gestaoescolar.gestao_escolar.dto.Comportamento.ComportamentoDtoOut;
import com.davi.gestaoescolar.gestao_escolar.dto.Aluno.AlunoDtoOut;
import com.davi.gestaoescolar.gestao_escolar.dto.Professor.ProfessorDtoOut;
import com.davi.gestaoescolar.gestao_escolar.exception.ComportamentoException;
import com.davi.gestaoescolar.gestao_escolar.exception.GlobalException;
import com.davi.gestaoescolar.gestao_escolar.model.Aluno;
import com.davi.gestaoescolar.gestao_escolar.model.Comportamento;
import com.davi.gestaoescolar.gestao_escolar.model.Professor;
import com.davi.gestaoescolar.gestao_escolar.model.enums.Gravidade;
import com.davi.gestaoescolar.gestao_escolar.model.enums.TipoComportamento;
import com.davi.gestaoescolar.gestao_escolar.repository.AlunoRepository;
import com.davi.gestaoescolar.gestao_escolar.repository.ComportamentoRepository;
import com.davi.gestaoescolar.gestao_escolar.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ComportamentoService {

    @Autowired
    private ComportamentoRepository comportamentoRepository;
    
    @Autowired
    private ProfessorRepository professorRepository;
    
    @Autowired
    private AlunoRepository alunoRepository;

    /**
     * Métodos auxiliares para conversão de DTOs
     */

    private ComportamentoDtoOut toDTO(Comportamento comportamento) {
        ProfessorDtoOut professorDto = new ProfessorDtoOut(
                comportamento.getProfessor() != null ? comportamento.getProfessor().getId() : null,
                comportamento.getProfessor() != null ? comportamento.getProfessor().getNome() : null
        );

        AlunoDtoOut alunoDto = new AlunoDtoOut(
                comportamento.getAluno() != null ? comportamento.getAluno().getId() : null,
                comportamento.getAluno() != null ? comportamento.getAluno().getNome() : null
        );

        return new ComportamentoDtoOut(
                comportamento.getId(),
                comportamento.getDescricao(),
                comportamento.getDate(),
                comportamento.getTipo(),
                comportamento.getNivel(),
                professorDto,
                alunoDto
        );
    }

    /**
     * Salva um novo registro de comportamento
     */
    public ComportamentoDtoOut salvar(ComportamentoDtoIn comportamentoDto) {
        validarDadosComportamento(comportamentoDto);
        
        Professor professor = buscarProfessor(comportamentoDto.getProfessorId());
        Aluno aluno = buscarAluno(comportamentoDto.getAlunoId());
        
        validarProfessor(professor);
        validarAluno(aluno);
        
        Comportamento comportamento = new Comportamento();
        comportamento.setDescricao(comportamentoDto.getDescricao());
        comportamento.setDate(comportamentoDto.getDate() != null ? comportamentoDto.getDate() : LocalDate.now());
        comportamento.setTipo(comportamentoDto.getTipo());
        comportamento.setNivel(comportamentoDto.getNivel() != null ? comportamentoDto.getNivel() : Gravidade.BAIXA);
        comportamento.setProfessor(professor);
        comportamento.setAluno(aluno);
        
        Comportamento comportamentoSalvo = comportamentoRepository.save(comportamento);
        return toDTO(comportamentoSalvo);
    }

    /**
     * Atualiza um registro de comportamento existente
     */
    public ComportamentoDtoOut atualizar(Long id, ComportamentoDtoIn comportamentoDto) {
        Comportamento comportamento = comportamentoRepository.findById(id)
                .orElseThrow(() -> new ComportamentoException.ComportamentoNaoEncontradoException(id));

        validarDadosComportamento(comportamentoDto);
        
        Professor professor = buscarProfessor(comportamentoDto.getProfessorId());
        Aluno aluno = buscarAluno(comportamentoDto.getAlunoId());
        
        validarProfessor(professor);
        validarAluno(aluno);
        
        comportamento.setDescricao(comportamentoDto.getDescricao());
        comportamento.setDate(comportamentoDto.getDate());
        comportamento.setTipo(comportamentoDto.getTipo());
        comportamento.setNivel(comportamentoDto.getNivel());
        comportamento.setProfessor(professor);
        comportamento.setAluno(aluno);
        
        Comportamento comportamentoAtualizado = comportamentoRepository.save(comportamento);
        return toDTO(comportamentoAtualizado);
    }

    /**
     * Busca comportamento por ID
     */
    @Transactional(readOnly = true)
    public Optional<ComportamentoDtoOut> buscarPorId(Long id) {
        Optional<Comportamento> comportamentoOptional = comportamentoRepository.findById(id);
        return comportamentoOptional.map(this::toDTO);
    }

    /**
     * Busca comportamentos por data
     */
    @Transactional(readOnly = true)
    public List<ComportamentoDtoOut> buscarPorData(LocalDate data) {
        if (data == null) {
            throw new GlobalException.DadosInvalidosException("Data não pode ser nula");
        }
        return comportamentoRepository.findByDate(data)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca comportamentos por período
     */
    @Transactional(readOnly = true)
    public List<ComportamentoDtoOut> buscarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new GlobalException.DadosInvalidosException("Datas de início e fim são obrigatórias");
        }
        if (dataInicio.isAfter(dataFim)) {
            throw new GlobalException.DadosInvalidosException("Data de início não pode ser posterior à data de fim");
        }
        return comportamentoRepository.findByDateBetween(dataInicio, dataFim)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca comportamentos por tipo
     */
    @Transactional(readOnly = true)
    public List<ComportamentoDtoOut> buscarPorTipo(TipoComportamento tipo) {
        if (tipo == null) {
            throw new GlobalException.DadosInvalidosException("Tipo de comportamento não pode ser nulo");
        }
        return comportamentoRepository.findByTipo(tipo)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca comportamentos por nível de gravidade
     */
    @Transactional(readOnly = true)
    public List<ComportamentoDtoOut> buscarPorNivel(Gravidade nivel) {
        if (nivel == null) {
            throw new GlobalException.DadosInvalidosException("Nível de gravidade não pode ser nulo");
        }
        return comportamentoRepository.findByNivel(nivel)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca comportamentos por professor
     */
    @Transactional(readOnly = true)
    public List<ComportamentoDtoOut> buscarPorProfessor(Long professorId) {
        if (professorId == null) {
            throw new GlobalException.DadosInvalidosException("ID do professor não pode ser nulo");
        }
        return comportamentoRepository.findByProfessorId(professorId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca comportamentos por aluno
     */
    @Transactional(readOnly = true)
    public List<ComportamentoDtoOut> buscarPorAluno(Long alunoId) {
        if (alunoId == null) {
            throw new GlobalException.DadosInvalidosException("ID do aluno não pode ser nulo");
        }
        return comportamentoRepository.findByAlunoId(alunoId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca comportamentos por turma
     */
    @Transactional(readOnly = true)
    public List<ComportamentoDtoOut> buscarPorTurma(Long turmaId) {
        if (turmaId == null) {
            throw new GlobalException.DadosInvalidosException("ID da turma não pode ser nulo");
        }
        return comportamentoRepository.findByTurmaId(turmaId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista todos os comportamentos
     */
    @Transactional(readOnly = true)
    public List<ComportamentoDtoOut> listarTodos() {
        return comportamentoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca comportamentos de hoje
     */
    @Transactional(readOnly = true)
    public List<ComportamentoDtoOut> buscarComportamentosDeHoje() {
        return comportamentoRepository.findByDate(LocalDate.now())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Deleta um comportamento permanentemente
     */
    public void deletar(Long id) {
        if (!comportamentoRepository.existsById(id)) {
            throw new ComportamentoException.ComportamentoNaoEncontradoException(id);
        }
        comportamentoRepository.deleteById(id);
    }

    /**
     * Gera relatório de comportamentos por professor
     */
    @Transactional(readOnly = true)
    public String gerarRelatorioPorProfessor(Long professorId, LocalDate dataInicio, LocalDate dataFim) {
        List<ComportamentoDtoOut> comportamentos = buscarPorProfessor(professorId).stream()
            .filter(c -> !c.getDate().isBefore(dataInicio) && !c.getDate().isAfter(dataFim))
            .collect(Collectors.toList());
        
        long positivos = comportamentos.stream()
            .mapToLong(c -> c.getTipo() == TipoComportamento.POSITIVO ? 1 : 0)
            .sum();
        
        long negativos = comportamentos.stream()
            .mapToLong(c -> c.getTipo() == TipoComportamento.NEGATIVO ? 1 : 0)
            .sum();
        
        return String.format(
            "Relatório do Professor (ID: %d) - %s a %s:\n" +
            "Total de registros: %d\n" +
            "Comportamentos positivos: %d\n" +
            "Comportamentos negativos: %d",
            professorId, dataInicio, dataFim, comportamentos.size(), positivos, negativos
        );
    }

    /**
     * Métodos auxiliares privados
     */
    private Professor buscarProfessor(Long professorId) {
        return professorRepository.findById(professorId)
                .orElseThrow(() -> new ComportamentoException.ProfessorInvalidoException("Professor não encontrado com ID: " + professorId));
    }

    private Aluno buscarAluno(Long alunoId) {
        return alunoRepository.findById(alunoId)
                .orElseThrow(() -> new ComportamentoException.AlunoInvalidoException("Aluno não encontrado com ID: " + alunoId));
    }

    /**
     * Valida os dados do comportamento
     */
    private void validarDadosComportamento(ComportamentoDtoIn comportamentoDto) {
        if (comportamentoDto == null) {
            throw new GlobalException.DadosInvalidosException("Dados do comportamento não podem ser nulos");
        }
        
        if (comportamentoDto.getDescricao() == null || comportamentoDto.getDescricao().trim().isEmpty()) {
            throw new GlobalException.DadosInvalidosException("Descrição do comportamento é obrigatória");
        }
        
        if (comportamentoDto.getTipo() == null) {
            throw new GlobalException.DadosInvalidosException("Tipo de comportamento é obrigatório");
        }
        
        if (comportamentoDto.getProfessorId() == null) {
            throw new GlobalException.DadosInvalidosException("Professor é obrigatório");
        }
        
        if (comportamentoDto.getAlunoId() == null) {
            throw new GlobalException.DadosInvalidosException("Aluno é obrigatório");
        }
        
        // Validação da data (não pode ser muito futura)
        if (comportamentoDto.getDate() != null && comportamentoDto.getDate().isAfter(LocalDate.now().plusDays(1))) {
            throw new GlobalException.DadosInvalidosException("Data do comportamento não pode ser no futuro");
        }
        
        // Validação do tamanho da descrição
        if (comportamentoDto.getDescricao().length() > 1000) {
            throw new GlobalException.DadosInvalidosException("Descrição não pode exceder 1000 caracteres");
        }
    }

    /**
     * Valida se professor existe e está ativo
     */
    private void validarProfessor(Professor professor) {
        if (!professor.getAtivo()) {
            throw new ComportamentoException.ProfessorInvalidoException("Não é possível registrar comportamento para um professor inativo");
        }
    }

    /**
     * Valida se aluno existe e está ativo
     */
    private void validarAluno(Aluno aluno) {
        if (!aluno.getAtivo()) {
            throw new ComportamentoException.AlunoInvalidoException("Não é possível registrar comportamento para um aluno inativo");
        }
    }
}