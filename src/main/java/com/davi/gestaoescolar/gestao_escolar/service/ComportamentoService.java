package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.model.Comportamento;
import com.davi.gestaoescolar.gestao_escolar.model.Professor;
import com.davi.gestaoescolar.gestao_escolar.model.enums.Gravidade;
import com.davi.gestaoescolar.gestao_escolar.model.enums.TipoComportamento;
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

    /**
     * Salva um novo registro de comportamento
     */
    public Comportamento salvar(Comportamento comportamento) {
        validarDadosComportamento(comportamento);
        validarProfessor(comportamento.getProfessor());
        
        // Define a data como hoje se não foi informada
        if (comportamento.getDate() == null) {
            comportamento.setDate(LocalDate.now());
        }
        
        // Define nível padrão se não foi informado
        if (comportamento.getNivel() == null) {
            comportamento.setNivel(Gravidade.BAIXA);
        }
        
        return comportamentoRepository.save(comportamento);
    }

    /**
     * Atualiza um registro de comportamento existente
     */
    public Comportamento atualizar(Long id, Comportamento comportamentoAtualizado) {
        Optional<Comportamento> comportamentoExistente = comportamentoRepository.findById(id);
        if (comportamentoExistente.isEmpty()) {
            throw new RuntimeException("Comportamento não encontrado com ID: " + id);
        }

        validarDadosComportamento(comportamentoAtualizado);
        validarProfessor(comportamentoAtualizado.getProfessor());
        
        Comportamento comportamento = comportamentoExistente.get();
        comportamento.setDescricao(comportamentoAtualizado.getDescricao());
        comportamento.setDate(comportamentoAtualizado.getDate());
        comportamento.setTipo(comportamentoAtualizado.getTipo());
        comportamento.setNivel(comportamentoAtualizado.getNivel());
        comportamento.setProfessor(comportamentoAtualizado.getProfessor());
        comportamento.setAluno(comportamentoAtualizado.getAluno());
        
        return comportamentoRepository.save(comportamento);
    }

    /**
     * Busca comportamento por ID
     */
    @Transactional(readOnly = true)
    public Optional<Comportamento> buscarPorId(Long id) {
        return comportamentoRepository.findById(id);
    }

   

    /**
     * Busca comportamentos por data
     */
    @Transactional(readOnly = true)
    public List<Comportamento> buscarPorData(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException("Data não pode ser nula");
        }
        return comportamentoRepository.findByDate(data);
    }

    /**
     * Busca comportamentos por período
     */
    @Transactional(readOnly = true)
    public List<Comportamento> buscarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Datas de início e fim não podem ser nulas");
        }
        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data de início não pode ser posterior à data de fim");
        }
        return comportamentoRepository.findByDateBetween(dataInicio, dataFim);
    }

    /**
     * Busca comportamentos por professor
     */
    @Transactional(readOnly = true)
    public List<Comportamento> buscarPorProfessor(Long professorId) {
        if (professorId == null) {
            throw new IllegalArgumentException("ID do professor não pode ser nulo");
        }
        return comportamentoRepository.findByProfessorId(professorId);
    }

    /**
     * Lista todos os comportamentos
     */
    @Transactional(readOnly = true)
    public List<Comportamento> listarTodos() {
        return comportamentoRepository.findAll();
    }

    

    /**
     * Busca comportamentos de hoje
     */
    @Transactional(readOnly = true)
    public List<Comportamento> buscarComportamentosDeHoje() {
        return comportamentoRepository.findByDate(LocalDate.now());
    }

    /**
     * Busca comportamentos da semana atual
     */
    @Transactional(readOnly = true)
    public List<Comportamento> buscarComportamentosDaSemana() {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioSemana = hoje.minusDays(hoje.getDayOfWeek().getValue() - 1);
        LocalDate fimSemana = inicioSemana.plusDays(6);
        return comportamentoRepository.findByDateBetween(inicioSemana, fimSemana);
    }

    /**
     * Busca comportamentos do mês atual
     */
    @Transactional(readOnly = true)
    public List<Comportamento> buscarComportamentosDoMes() {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioMes = hoje.withDayOfMonth(1);
        LocalDate fimMes = hoje.withDayOfMonth(hoje.lengthOfMonth());
        return comportamentoRepository.findByDateBetween(inicioMes, fimMes);
    }

    /**
     * Deleta um comportamento permanentemente
     */
    public void deletar(Long id) {
        if (!comportamentoRepository.existsById(id)) {
            throw new RuntimeException("Comportamento não encontrado com ID: " + id);
        }
        comportamentoRepository.deleteById(id);
    }


    /**
     * Gera relatório de comportamentos por período
     */
    @Transactional(readOnly = true)
    public String gerarRelatorioComportamentos(LocalDate dataInicio, LocalDate dataFim) {
        List<Comportamento> comportamentos = buscarPorPeriodo(dataInicio, dataFim);
        
        long positivos = comportamentos.stream()
            .mapToLong(c -> c.getTipo() == TipoComportamento.POSITIVO ? 1 : 0)
            .sum();
        
        long negativos = comportamentos.stream()
            .mapToLong(c -> c.getTipo() == TipoComportamento.NEGATIVO ? 1 : 0)
            .sum();
        
        long graves = comportamentos.stream()
            .mapToLong(c -> c.getNivel() == Gravidade.ALTA ? 1 : 0)
            .sum();
        
        return String.format(
            "Relatório de Comportamentos (%s a %s):\n" +
            "Total de registros: %d\n" +
            "Comportamentos positivos: %d\n" +
            "Comportamentos negativos: %d\n" +
            "Comportamentos graves: %d\n" +
            "Percentual positivo: %.2f%%",
            dataInicio, dataFim, comportamentos.size(), positivos, negativos, graves,
            comportamentos.size() > 0 ? (positivos * 100.0 / comportamentos.size()) : 0.0
        );
    }
    /**
     * Gera todos as anotações - comportamentos do aluno
     */
    @Transactional(readOnly = true)
    public List<Comportamento> buscarTodosOsComportamentosDoAluno(Long alunoId) {
        if (alunoId == null) {
            throw new IllegalArgumentException("ID do aluno não pode ser nulo");
        }
        return comportamentoRepository.findByAlunoId(alunoId);
    }


    /**
     * Gera relatório de comportamentos por professor
     */
    @Transactional(readOnly = true)
    public String gerarRelatorioPorProfessor(Long professorId, LocalDate dataInicio, LocalDate dataFim) {
        List<Comportamento> comportamentos = buscarPorProfessor(professorId).stream()
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
     * Valida os dados do comportamento
     */
    private void validarDadosComportamento(Comportamento comportamento) {
        if (comportamento == null) {
            throw new IllegalArgumentException("Comportamento não pode ser nulo");
        }
        
        if (comportamento.getDescricao() == null || comportamento.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição do comportamento é obrigatória");
        }
        
        if (comportamento.getTipo() == null) {
            throw new IllegalArgumentException("Tipo de comportamento é obrigatório");
        }
        
        if (comportamento.getProfessor() == null || comportamento.getProfessor().getId() == null) {
            throw new IllegalArgumentException("Professor é obrigatório");
        }
        
        // Validação da data (não pode ser muito futura)
        if (comportamento.getDate() != null && comportamento.getDate().isAfter(LocalDate.now().plusDays(1))) {
            throw new IllegalArgumentException("Data do comportamento não pode ser no futuro");
        }
        
        // Validação do tamanho da descrição
        if (comportamento.getDescricao().length() > 1000) {
            throw new IllegalArgumentException("Descrição não pode exceder 1000 caracteres");
        }
    }
    
    /**
     * Valida se professor existe e está ativo
     */
    private void validarProfessor(Professor professor) {
        Optional<Professor> professorExistente = professorRepository.findById(professor.getId());
        if (professorExistente.isEmpty()) {
            throw new RuntimeException("Professor não encontrado com ID: " + professor.getId());
        }
        
        if (!professorExistente.get().getAtivo()) {
            throw new RuntimeException("Não é possível registrar comportamento para um professor inativo");
        }
    }
}