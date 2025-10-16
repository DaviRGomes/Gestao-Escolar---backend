package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.dto.RegistroAula.RegistroAulaDtoIn;
import com.davi.gestaoescolar.gestao_escolar.dto.RegistroAula.RegistroAulaDtoOut;
import com.davi.gestaoescolar.gestao_escolar.dto.Disciplina.DisciplinaDtoOut;
import com.davi.gestaoescolar.gestao_escolar.dto.Turma.TurmaDtoOut;
import com.davi.gestaoescolar.gestao_escolar.dto.ConteudoPlanejado.ConteudoPlanejadoDtoOut;
import com.davi.gestaoescolar.gestao_escolar.model.ConteudoPlanejado;
import com.davi.gestaoescolar.gestao_escolar.model.Disciplina;
import com.davi.gestaoescolar.gestao_escolar.model.RegistroAula;
import com.davi.gestaoescolar.gestao_escolar.model.Turma;
import com.davi.gestaoescolar.gestao_escolar.repository.ConteudoPlanejadoRepository;
import com.davi.gestaoescolar.gestao_escolar.repository.DisciplinaRepository;
import com.davi.gestaoescolar.gestao_escolar.repository.RegistroAulaRepository;
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
public class RegistroAulaService {

    @Autowired
    private RegistroAulaRepository registroAulaRepository;
    
    @Autowired
    private TurmaRepository turmaRepository;
    
    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private ConteudoPlanejadoRepository conteudoPlanejadoRepository;
    
    public RegistroAulaDtoOut toDto(RegistroAula registroAula) {
        TurmaDtoOut turmaDto = null;
        if (registroAula.getTurma() != null) {
            turmaDto = new TurmaDtoOut(
                registroAula.getTurma().getId(),
                registroAula.getTurma().getNome()
            );
        }

        DisciplinaDtoOut disciplinaDto = null;
        if (registroAula.getDisciplina() != null) {
            disciplinaDto = new DisciplinaDtoOut(
                registroAula.getDisciplina().getId(),
                registroAula.getDisciplina().getNome()
            );
        }

        ConteudoPlanejadoDtoOut conteudoDto = null;
        if (registroAula.getConteudoPlanejado() != null) {
            conteudoDto = new ConteudoPlanejadoDtoOut(
                registroAula.getConteudoPlanejado().getId(),
                registroAula.getConteudoPlanejado().getConteudo(),
                registroAula.getConteudoPlanejado().getDataPrevista()
            );
        }

        return new RegistroAulaDtoOut(
            registroAula.getId(),
            registroAula.getData(),
            registroAula.getDescricao(),
            registroAula.getObservacoes(),
            disciplinaDto,
            turmaDto,
            conteudoDto
        );
    }

    /**
     * Salva um novo registro de aula
     */
    public RegistroAulaDtoOut salvar(RegistroAulaDtoIn dtoIn) {
        validarDadosRegistroAula(dtoIn);
        validarTurmaEDisciplina(dtoIn.getTurmaId(), dtoIn.getDisciplinaId());

        Turma turma = turmaRepository.findById(dtoIn.getTurmaId()).
            orElseThrow(() -> new RuntimeException("Turma não encontrada com ID: " + dtoIn.getTurmaId()));

        Disciplina disciplina = disciplinaRepository.findById(dtoIn.getDisciplinaId()).
            orElseThrow(() -> new RuntimeException("Disciplina não encontrada com ID: " + dtoIn.getDisciplinaId()));
        

        ConteudoPlanejado conteudoPlanejado = conteudoPlanejadoRepository.findById(dtoIn.getConteudoPlanejadoId()).
            orElseThrow(() -> new RuntimeException("Conteúdo planejado não encontrado com ID: " + dtoIn.getConteudoPlanejadoId()));
        
        // Define a data como hoje se não foi informada
        if (dtoIn.getData() == null) {
            dtoIn.setData(LocalDate.now());
        }
       
        RegistroAula registroAula = new RegistroAula(
            dtoIn.getData(),
            dtoIn.getDescricao(),
            dtoIn.getObservacoes(),
            turma,
            disciplina,
            null,
            null,
            conteudoPlanejado
        );
        
        return toDto(registroAulaRepository.save(registroAula));
    }

    /**
     * Atualiza um registro de aula existente
     */
    public RegistroAulaDtoOut atualizar(Long id, RegistroAulaDtoIn dtoIn) {
        Optional<RegistroAula> registroExistente = registroAulaRepository.findById(id);
        if (registroExistente.isEmpty()) {
            throw new RuntimeException("Registro de aula não encontrado com ID: " + id);
        }

        validarDadosRegistroAula(dtoIn);
        validarTurmaEDisciplina(dtoIn.getTurmaId(), dtoIn.getDisciplinaId());
        
        RegistroAula registro = registroExistente.get();
        registro.setData(dtoIn.getData());
        registro.setDescricao(dtoIn.getDescricao());
        registro.setObservacoes(dtoIn.getObservacoes());            
        registro.setTurma(turmaRepository.findById(dtoIn.getTurmaId()).
            orElseThrow(() -> new RuntimeException("Turma não encontrada com ID: " + dtoIn.getTurmaId())));
        registro.setDisciplina(disciplinaRepository.findById(dtoIn.getDisciplinaId()).
            orElseThrow(() -> new RuntimeException("Disciplina não encontrada com ID: " + dtoIn.getDisciplinaId())));
        registro.setConteudoPlanejado(conteudoPlanejadoRepository.findById(dtoIn.getConteudoPlanejadoId()).
            orElseThrow(() -> new RuntimeException("Conteúdo planejado não encontrado com ID: " + dtoIn.getConteudoPlanejadoId())));
        
        return toDto(registroAulaRepository.save(registro));
    }

    /**
     * Busca registro de aula por ID
     */
    @Transactional(readOnly = true)
    public Optional<RegistroAulaDtoOut> buscarPorId(Long id) {
        return registroAulaRepository.findById(id).map(this::toDto);
    }

    /**
     * Busca registros de aula por data
     */
    @Transactional(readOnly = true)
    public List<RegistroAulaDtoOut> buscarPorData(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException("Data não pode ser nula");
        }
        return registroAulaRepository.findByData(data).stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Busca registros de aula por período
     */
    @Transactional(readOnly = true)
    public List<RegistroAulaDtoOut> buscarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Datas de início e fim não podem ser nulas");
        }
        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data de início não pode ser posterior à data de fim");
        }
        return registroAulaRepository.findByDataBetween(dataInicio, dataFim).stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Busca registros de aula por turma
     */
    @Transactional(readOnly = true)
    public List<RegistroAulaDtoOut> buscarPorTurma(Long turmaId) {
        if (turmaId == null) {
            throw new IllegalArgumentException("ID da turma não pode ser nulo");
        }
        return registroAulaRepository.findByTurmaId(turmaId).stream().map(this::toDto).collect(Collectors.toList());   
    }

    /**
     * Busca registros de aula por disciplina
     */
    @Transactional(readOnly = true)
    public List<RegistroAulaDtoOut> buscarPorDisciplina(Long disciplinaId) {
        if (disciplinaId == null) {
            throw new IllegalArgumentException("ID da disciplina não pode ser nulo");
        }
        return registroAulaRepository.findByDisciplinaId(disciplinaId).stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Busca registros de aula por turma e disciplina
     */
    @Transactional(readOnly = true)
    public List<RegistroAulaDtoOut> buscarPorTurmaEDisciplina(Long turmaId, Long disciplinaId) {
        if (turmaId == null) {
            throw new IllegalArgumentException("ID da turma não pode ser nulo");
        }
        if (disciplinaId == null) {
            throw new IllegalArgumentException("ID da disciplina não pode ser nulo");
        }
        return registroAulaRepository.findByTurmaIdAndDisciplinaId(turmaId, disciplinaId).stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Busca registros de aula por conteúdo planejado
     */
    @Transactional(readOnly = true)
    public List<RegistroAulaDtoOut> buscarPorConteudoPlanejado(Long conteudoPlanejadoId) {
        if (conteudoPlanejadoId == null) {
            throw new IllegalArgumentException("ID do conteúdo planejado não pode ser nulo");
        }
        return registroAulaRepository.findByConteudoPlanejadoId(conteudoPlanejadoId).stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Busca registros de aula por turma e período
     */
    @Transactional(readOnly = true)
    public List<RegistroAulaDtoOut> buscarPorTurmaEPeriodo(Long turmaId, LocalDate dataInicio, LocalDate dataFim) {
        if (turmaId == null) {
            throw new IllegalArgumentException("ID da turma não pode ser nulo");
        }
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Datas de início e fim não podem ser nulas");
        }
        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data de início não pode ser posterior à data de fim");
        }
        return registroAulaRepository.findByTurmaIdAndDataBetween(turmaId, dataInicio, dataFim).stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Busca registros de aula por disciplina e período
     */
    @Transactional(readOnly = true)
    public List<RegistroAulaDtoOut> buscarPorDisciplinaEPeriodo(Long disciplinaId, LocalDate dataInicio, LocalDate dataFim) {
        if (disciplinaId == null) {
            throw new IllegalArgumentException("ID da disciplina não pode ser nulo");
        }
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Datas de início e fim não podem ser nulas");
        }
        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data de início não pode ser posterior à data de fim");
        }
        return registroAulaRepository.findByDisciplinaIdAndDataBetween(disciplinaId, dataInicio, dataFim).stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Lista todos os registros de aula
     */
    @Transactional(readOnly = true)
    public List<RegistroAulaDtoOut> listarTodos() {
        return registroAulaRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Busca registros de aula de hoje
     */
    @Transactional(readOnly = true)
    public List<RegistroAulaDtoOut> buscarAulasDeHoje() {
        return registroAulaRepository.findByData(LocalDate.now()).stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Busca registros de aula da semana atual
     */
    @Transactional(readOnly = true)
    public List<RegistroAulaDtoOut> buscarAulasDaSemana() {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioSemana = hoje.minusDays(hoje.getDayOfWeek().getValue() - 1);
        LocalDate fimSemana = inicioSemana.plusDays(6);
        return registroAulaRepository.findByDataBetween(inicioSemana, fimSemana).stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Busca registros de aula do mês atual
     */
    @Transactional(readOnly = true)
    public List<RegistroAulaDtoOut> buscarAulasDoMes() {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioMes = hoje.withDayOfMonth(1);
        LocalDate fimMes = hoje.withDayOfMonth(hoje.lengthOfMonth());
        return registroAulaRepository.findByDataBetween(inicioMes, fimMes).stream().map(this::toDto).collect(Collectors.toList());
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
     * Valida os dados do registro de aula
     */
    private void validarDadosRegistroAula(RegistroAulaDtoIn dtoIn) {
        if (dtoIn == null) {
            throw new IllegalArgumentException("Registro de aula não pode ser nulo");
        }
        
        if (dtoIn.getConteudoMinistrado() == null || dtoIn.getConteudoMinistrado().trim().isEmpty()) {
            throw new IllegalArgumentException("Conteúdo ministrado é obrigatório");
        }
        
        if (dtoIn.getTurmaId() == null) {
            throw new IllegalArgumentException("Turma é obrigatória");
        }
        
        if (dtoIn.getDisciplinaId() == null) {
            throw new IllegalArgumentException("Disciplina é obrigatória");
        }
        
        // Validação da data (não pode ser muito futura)
        if (dtoIn.getData() != null && dtoIn.getData().isAfter(LocalDate.now().plusDays(30))) {
            throw new IllegalArgumentException("Data da aula não pode ser mais de 30 dias no futuro");
        }
        
        // Validação do tamanho da descrição
        if (dtoIn.getConteudoMinistrado().length() > 1000) {
            throw new IllegalArgumentException("Conteúdo ministrado não pode exceder 1000 caracteres");
        }
        
        // Validação do tamanho das observações (se fornecidas)
        if (dtoIn.getObservacoes() != null && dtoIn.getObservacoes().length() > 500) {
            throw new IllegalArgumentException("Observações não podem exceder 500 caracteres");
        }
    }
    
    /**
     * Valida se turma e disciplina existem e estão ativas
     */
    private void validarTurmaEDisciplina(Long turmaId, Long disciplinaId) {
        Optional<Turma> turmaExistente = turmaRepository.findById(turmaId);
        if (turmaExistente.isEmpty()) {
            throw new RuntimeException("Turma não encontrada com ID: " + turmaId);
        }
        
        if (!turmaExistente.get().getAtivo()) {
            throw new RuntimeException("Não é possível registrar aula para uma turma inativa");
        }
        
        Optional<Disciplina> disciplinaExistente = disciplinaRepository.findById(disciplinaId);
        if (disciplinaExistente.isEmpty()) {
            throw new RuntimeException("Disciplina não encontrada com ID: " + disciplinaId);    
        }
        
        if (!disciplinaExistente.get().getAtivo()) {
            throw new RuntimeException("Não é possível registrar aula para uma disciplina inativa");
        }
        
        // Verifica se a disciplina está associada à turma
        boolean disciplinaAssociadaATurma = turmaExistente.get().getDisciplinas().stream()
            .anyMatch(d -> d.getId().equals(disciplinaId));
        
        if (!disciplinaAssociadaATurma) {
            throw new RuntimeException("A disciplina não está associada à turma informada");
        }
    }
}