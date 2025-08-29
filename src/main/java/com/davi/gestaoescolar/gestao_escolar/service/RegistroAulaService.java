package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.model.Disciplina;
import com.davi.gestaoescolar.gestao_escolar.model.RegistroAula;
import com.davi.gestaoescolar.gestao_escolar.model.Turma;
import com.davi.gestaoescolar.gestao_escolar.repository.DisciplinaRepository;
import com.davi.gestaoescolar.gestao_escolar.repository.RegistroAulaRepository;
import com.davi.gestaoescolar.gestao_escolar.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RegistroAulaService {

    @Autowired
    private RegistroAulaRepository registroAulaRepository;
    
    @Autowired
    private TurmaRepository turmaRepository;
    
    @Autowired
    private DisciplinaRepository disciplinaRepository;

    /**
     * Salva um novo registro de aula
     */
    public RegistroAula salvar(RegistroAula registroAula) {
        validarDadosRegistroAula(registroAula);
        validarTurmaEDisciplina(registroAula.getTurma(), registroAula.getDisciplina());
        
        // Define a data como hoje se não foi informada
        if (registroAula.getData() == null) {
            registroAula.setData(LocalDate.now());
        }
        
        return registroAulaRepository.save(registroAula);
    }

    /**
     * Atualiza um registro de aula existente
     */
    public RegistroAula atualizar(Long id, RegistroAula registroAtualizado) {
        Optional<RegistroAula> registroExistente = registroAulaRepository.findById(id);
        if (registroExistente.isEmpty()) {
            throw new RuntimeException("Registro de aula não encontrado com ID: " + id);
        }

        validarDadosRegistroAula(registroAtualizado);
        validarTurmaEDisciplina(registroAtualizado.getTurma(), registroAtualizado.getDisciplina());
        
        RegistroAula registro = registroExistente.get();
        registro.setData(registroAtualizado.getData());
        registro.setDescricao(registroAtualizado.getDescricao());
        registro.setObservacoes(registroAtualizado.getObservacoes());
        registro.setTurma(registroAtualizado.getTurma());
        registro.setDisciplina(registroAtualizado.getDisciplina());
        registro.setConteudoPlanejado(registroAtualizado.getConteudoPlanejado());
        
        return registroAulaRepository.save(registro);
    }

    /**
     * Busca registro de aula por ID
     */
    @Transactional(readOnly = true)
    public Optional<RegistroAula> buscarPorId(Long id) {
        return registroAulaRepository.findById(id);
    }

    /**
     * Busca registros de aula por data
     */
    @Transactional(readOnly = true)
    public List<RegistroAula> buscarPorData(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException("Data não pode ser nula");
        }
        return registroAulaRepository.findByData(data);
    }

    /**
     * Busca registros de aula por período
     */
    @Transactional(readOnly = true)
    public List<RegistroAula> buscarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Datas de início e fim não podem ser nulas");
        }
        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data de início não pode ser posterior à data de fim");
        }
        return registroAulaRepository.findByDataBetween(dataInicio, dataFim);
    }

    /**
     * Busca registros de aula por turma
     */
    @Transactional(readOnly = true)
    public List<RegistroAula> buscarPorTurma(Long turmaId) {
        if (turmaId == null) {
            throw new IllegalArgumentException("ID da turma não pode ser nulo");
        }
        return registroAulaRepository.findByTurmaId(turmaId);
    }

    /**
     * Busca registros de aula por disciplina
     */
    @Transactional(readOnly = true)
    public List<RegistroAula> buscarPorDisciplina(Long disciplinaId) {
        if (disciplinaId == null) {
            throw new IllegalArgumentException("ID da disciplina não pode ser nulo");
        }
        return registroAulaRepository.findByDisciplinaId(disciplinaId);
    }

    /**
     * Busca registros de aula por turma e disciplina
     */
    @Transactional(readOnly = true)
    public List<RegistroAula> buscarPorTurmaEDisciplina(Long turmaId, Long disciplinaId) {
        if (turmaId == null) {
            throw new IllegalArgumentException("ID da turma não pode ser nulo");
        }
        if (disciplinaId == null) {
            throw new IllegalArgumentException("ID da disciplina não pode ser nulo");
        }
        return registroAulaRepository.findByTurmaIdAndDisciplinaId(turmaId, disciplinaId);
    }

    /**
     * Busca registros de aula por conteúdo planejado
     */
    @Transactional(readOnly = true)
    public List<RegistroAula> buscarPorConteudoPlanejado(Long conteudoPlanejadoId) {
        if (conteudoPlanejadoId == null) {
            throw new IllegalArgumentException("ID do conteúdo planejado não pode ser nulo");
        }
        return registroAulaRepository.findByConteudoPlanejadoId(conteudoPlanejadoId);
    }

    /**
     * Busca registros de aula por turma e período
     */
    @Transactional(readOnly = true)
    public List<RegistroAula> buscarPorTurmaEPeriodo(Long turmaId, LocalDate dataInicio, LocalDate dataFim) {
        if (turmaId == null) {
            throw new IllegalArgumentException("ID da turma não pode ser nulo");
        }
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Datas de início e fim não podem ser nulas");
        }
        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data de início não pode ser posterior à data de fim");
        }
        return registroAulaRepository.findByTurmaIdAndDataBetween(turmaId, dataInicio, dataFim);
    }

    /**
     * Busca registros de aula por disciplina e período
     */
    @Transactional(readOnly = true)
    public List<RegistroAula> buscarPorDisciplinaEPeriodo(Long disciplinaId, LocalDate dataInicio, LocalDate dataFim) {
        if (disciplinaId == null) {
            throw new IllegalArgumentException("ID da disciplina não pode ser nulo");
        }
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Datas de início e fim não podem ser nulas");
        }
        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data de início não pode ser posterior à data de fim");
        }
        return registroAulaRepository.findByDisciplinaIdAndDataBetween(disciplinaId, dataInicio, dataFim);
    }

    /**
     * Lista todos os registros de aula
     */
    @Transactional(readOnly = true)
    public List<RegistroAula> listarTodos() {
        return registroAulaRepository.findAll();
    }

    /**
     * Busca registros de aula de hoje
     */
    @Transactional(readOnly = true)
    public List<RegistroAula> buscarAulasDeHoje() {
        return registroAulaRepository.findByData(LocalDate.now());
    }

    /**
     * Busca registros de aula da semana atual
     */
    @Transactional(readOnly = true)
    public List<RegistroAula> buscarAulasDaSemana() {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioSemana = hoje.minusDays(hoje.getDayOfWeek().getValue() - 1);
        LocalDate fimSemana = inicioSemana.plusDays(6);
        return registroAulaRepository.findByDataBetween(inicioSemana, fimSemana);
    }

    /**
     * Busca registros de aula do mês atual
     */
    @Transactional(readOnly = true)
    public List<RegistroAula> buscarAulasDoMes() {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioMes = hoje.withDayOfMonth(1);
        LocalDate fimMes = hoje.withDayOfMonth(hoje.lengthOfMonth());
        return registroAulaRepository.findByDataBetween(inicioMes, fimMes);
    }

    /**
     * Deleta um registro de aula permanentemente
     */
    public void deletar(Long id) {
        if (!registroAulaRepository.existsById(id)) {
            throw new RuntimeException("Registro de aula não encontrado com ID: " + id);
        }
        registroAulaRepository.deleteById(id);
    }

    /**
     * Cria um registro de aula básico
     */
    public RegistroAula criarRegistroBasico(Long turmaId, Long disciplinaId, LocalDate data, String descricao) {
        Optional<Turma> turma = turmaRepository.findById(turmaId);
        if (turma.isEmpty()) {
            throw new RuntimeException("Turma não encontrada com ID: " + turmaId);
        }
        
        Optional<Disciplina> disciplina = disciplinaRepository.findById(disciplinaId);
        if (disciplina.isEmpty()) {
            throw new RuntimeException("Disciplina não encontrada com ID: " + disciplinaId);
        }
        
        RegistroAula registro = new RegistroAula();
        registro.setTurma(turma.get());
        registro.setDisciplina(disciplina.get());
        registro.setData(data != null ? data : LocalDate.now());
        registro.setDescricao(descricao);
        
        return salvar(registro);
    }

    /**
     * Valida os dados do registro de aula
     */
    private void validarDadosRegistroAula(RegistroAula registroAula) {
        if (registroAula == null) {
            throw new IllegalArgumentException("Registro de aula não pode ser nulo");
        }
        
        if (registroAula.getDescricao() == null || registroAula.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição da aula é obrigatória");
        }
        
        if (registroAula.getTurma() == null || registroAula.getTurma().getId() == null) {
            throw new IllegalArgumentException("Turma é obrigatória");
        }
        
        if (registroAula.getDisciplina() == null || registroAula.getDisciplina().getId() == null) {
            throw new IllegalArgumentException("Disciplina é obrigatória");
        }
        
        // Validação da data (não pode ser muito futura)
        if (registroAula.getData() != null && registroAula.getData().isAfter(LocalDate.now().plusDays(30))) {
            throw new IllegalArgumentException("Data da aula não pode ser mais de 30 dias no futuro");
        }
        
        // Validação do tamanho da descrição
        if (registroAula.getDescricao().length() > 1000) {
            throw new IllegalArgumentException("Descrição não pode exceder 1000 caracteres");
        }
        
        // Validação do tamanho das observações (se fornecidas)
        if (registroAula.getObservacoes() != null && registroAula.getObservacoes().length() > 500) {
            throw new IllegalArgumentException("Observações não podem exceder 500 caracteres");
        }
    }
    
    /**
     * Valida se turma e disciplina existem e estão ativas
     */
    private void validarTurmaEDisciplina(Turma turma, Disciplina disciplina) {
        Optional<Turma> turmaExistente = turmaRepository.findById(turma.getId());
        if (turmaExistente.isEmpty()) {
            throw new RuntimeException("Turma não encontrada com ID: " + turma.getId());
        }
        
        if (!turmaExistente.get().getAtivo()) {
            throw new RuntimeException("Não é possível registrar aula para uma turma inativa");
        }
        
        Optional<Disciplina> disciplinaExistente = disciplinaRepository.findById(disciplina.getId());
        if (disciplinaExistente.isEmpty()) {
            throw new RuntimeException("Disciplina não encontrada com ID: " + disciplina.getId());
        }
        
        if (!disciplinaExistente.get().getAtivo()) {
            throw new RuntimeException("Não é possível registrar aula para uma disciplina inativa");
        }
        
        // Verifica se a disciplina está associada à turma
        boolean disciplinaAssociadaATurma = turmaExistente.get().getDisciplinas().stream()
            .anyMatch(d -> d.getId().equals(disciplina.getId()));
        
        if (!disciplinaAssociadaATurma) {
            throw new RuntimeException("A disciplina não está associada à turma informada");
        }
    }
}