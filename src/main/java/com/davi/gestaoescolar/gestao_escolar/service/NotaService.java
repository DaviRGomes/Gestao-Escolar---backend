package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.dto.Aluno.AlunoDtoOut;
import com.davi.gestaoescolar.gestao_escolar.dto.Nota.NotaDtoIn;
import com.davi.gestaoescolar.gestao_escolar.dto.Nota.NotaDtoOut;
import com.davi.gestaoescolar.gestao_escolar.dto.RegistroAula.RegistroAulaDtoOut;
import com.davi.gestaoescolar.gestao_escolar.exception.NotaException;
import com.davi.gestaoescolar.gestao_escolar.exception.GlobalException;
import com.davi.gestaoescolar.gestao_escolar.model.Aluno;
import com.davi.gestaoescolar.gestao_escolar.model.Nota;
import com.davi.gestaoescolar.gestao_escolar.model.RegistroAula;
import com.davi.gestaoescolar.gestao_escolar.model.enums.TipoAvaliacao;
import com.davi.gestaoescolar.gestao_escolar.repository.AlunoRepository;
import com.davi.gestaoescolar.gestao_escolar.repository.NotaRepository;
import com.davi.gestaoescolar.gestao_escolar.repository.RegistroAulaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotaService {

    @Autowired
    private NotaRepository notaRepository;
    
    @Autowired
    private AlunoRepository alunoRepository;
    
    @Autowired
    private RegistroAulaRepository registroAulaRepository;

    /**
     * Método auxiliar para conversão de DTO
     */
    private NotaDtoOut toDTO(Nota nota) {
        RegistroAulaDtoOut registroAulaDto = new RegistroAulaDtoOut(
                nota.getRegistroAula() != null ? nota.getRegistroAula().getId() : null,
                nota.getRegistroAula() != null ? nota.getRegistroAula().getData() : null,
                nota.getRegistroAula() != null ? nota.getRegistroAula().getDescricao() : null
        );
        AlunoDtoOut alunoDto = new AlunoDtoOut(
                nota.getAluno() != null ? nota.getAluno().getId() : null,
                nota.getAluno() != null ? nota.getAluno().getNome() : null
        );

        return new NotaDtoOut(
                nota.getId(),
                nota.getValor(),
                nota.getTipo(),
                nota.getPeso(),
                nota.getObservacao(),
                registroAulaDto,
                alunoDto
        );
    }

    /**
     * Salva uma nova nota
     */
    public NotaDtoOut salvar(NotaDtoIn notaDto) {
        validarDadosNota(notaDto);
        
        Aluno aluno = buscarAluno(notaDto.getAlunoId());
        RegistroAula registroAula = buscarRegistroAula(notaDto.getRegistroAulaId());
        
        validarAluno(aluno);
        validarRegistroAula(registroAula);
        validarMatriculaAluno(aluno, registroAula);
        
        Nota nota = new Nota();
        nota.setValor(notaDto.getValor().setScale(2, RoundingMode.HALF_UP));
        nota.setTipo(notaDto.getTipo());
        nota.setPeso(notaDto.getPeso() != null ? notaDto.getPeso().setScale(2, RoundingMode.HALF_UP) : new BigDecimal("1.0"));
        nota.setObservacao(notaDto.getObservacao());
        nota.setAluno(aluno);
        nota.setRegistroAula(registroAula);
        
        Nota notaSalva = notaRepository.save(nota);
        return toDTO(notaSalva);
    }

    /**
     * Atualiza uma nota existente
     */
    public NotaDtoOut atualizar(Long id, NotaDtoIn notaDto) {
        Nota nota = notaRepository.findById(id)
                .orElseThrow(() -> new NotaException.NotaNaoEncontradaException(id));

        validarDadosNota(notaDto);
        
        Aluno aluno = buscarAluno(notaDto.getAlunoId());
        RegistroAula registroAula = buscarRegistroAula(notaDto.getRegistroAulaId());
        
        validarAluno(aluno);
        validarRegistroAula(registroAula);
        validarMatriculaAluno(aluno, registroAula);
        
        nota.setValor(notaDto.getValor().setScale(2, RoundingMode.HALF_UP));
        nota.setTipo(notaDto.getTipo());
        nota.setPeso(notaDto.getPeso() != null ? notaDto.getPeso().setScale(2, RoundingMode.HALF_UP) : new BigDecimal("1.0"));
        nota.setObservacao(notaDto.getObservacao());
        nota.setAluno(aluno);
        nota.setRegistroAula(registroAula);
        
        Nota notaAtualizada = notaRepository.save(nota);
        return toDTO(notaAtualizada);
    }

    /**
     * Busca nota por ID
     */
    @Transactional(readOnly = true)
    public NotaDtoOut buscarPorId(Long id) {
        return notaRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new NotaException.NotaNaoEncontradaException(id));
    }

    /**
     * Busca notas por aluno
     */
    @Transactional(readOnly = true)
    public List<NotaDtoOut> buscarPorAluno(Long alunoId) {
        if (alunoId == null) {
            throw new GlobalException.DadosInvalidosException("ID do aluno não pode ser nulo");
        }
        return notaRepository.findByAlunoId(alunoId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca notas por registro de aula
     */
    @Transactional(readOnly = true)
    public List<NotaDtoOut> buscarPorRegistroAula(Long registroAulaId) {
        if (registroAulaId == null) {
            throw new GlobalException.DadosInvalidosException("ID do registro de aula não pode ser nulo");
        }
        return notaRepository.findByRegistroAulaId(registroAulaId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca notas por tipo de avaliação
     */
    @Transactional(readOnly = true)
    public List<NotaDtoOut> buscarPorTipo(TipoAvaliacao tipo) {
        if (tipo == null) {
            throw new GlobalException.DadosInvalidosException("Tipo de avaliação não pode ser nulo");
        }
        return notaRepository.findByTipo(tipo).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca notas por valor mínimo
     */
    @Transactional(readOnly = true)
    public List<Nota> buscarPorValorMinimo(BigDecimal valorMinimo) {
        if (valorMinimo == null) {
            throw new IllegalArgumentException("Valor mínimo não pode ser nulo");
        }
        return notaRepository.findByValorGreaterThanEqual(valorMinimo);
    }

    /**
     * Busca notas por valor máximo
     */
    @Transactional(readOnly = true)
    public List<Nota> buscarPorValorMaximo(BigDecimal valorMaximo) {
        if (valorMaximo == null) {
            throw new IllegalArgumentException("Valor máximo não pode ser nulo");
        }
        return notaRepository.findByValorLessThanEqual(valorMaximo);
    }

    /**
     * Busca notas por aluno e registro de aula
     */
    @Transactional(readOnly = true)
    public List<Nota> buscarPorAlunoERegistroAula(Long alunoId, Long registroAulaId) {
        if (alunoId == null) {
            throw new IllegalArgumentException("ID do aluno não pode ser nulo");
        }
        if (registroAulaId == null) {
            throw new IllegalArgumentException("ID do registro de aula não pode ser nulo");
        }
        return notaRepository.findByAlunoIdAndRegistroAulaId(alunoId, registroAulaId);
    }

    /**
     * Busca notas por aluno e tipo de avaliação
     */
    @Transactional(readOnly = true)
    public List<Nota> buscarPorAlunoETipo(Long alunoId, TipoAvaliacao tipo) {
        if (alunoId == null) {
            throw new IllegalArgumentException("ID do aluno não pode ser nulo");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de avaliação não pode ser nulo");
        }
        return notaRepository.findByAlunoIdAndTipo(alunoId, tipo);
    }

    /**
     * Lista todas as notas
     */
    @Transactional(readOnly = true)
    public List<NotaDtoOut> listarTodas() {
        return notaRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Calcula a média das notas de um aluno
     */
    @Transactional(readOnly = true)
    public Double calcularMediaAluno(Long alunoId) {
        List<Nota> notas = notaRepository.findByAlunoId(alunoId);
        
        if (notas.isEmpty()) {
            return 0.00;
        }
        
        Double somaNotas = 0.0;
        Double somaPesos = 0.0;
        
        for (Nota nota : notas) {
            BigDecimal peso = nota.getPeso() != null ? nota.getPeso() : BigDecimal.ONE;
            somaNotas += nota.getValor().multiply(peso).doubleValue();
            somaPesos += peso.doubleValue();
        }
        
        if (somaPesos == 0.0) {
            return 0.00;
        }
        
        return somaNotas / somaPesos;
    }

    /**
     * Calcula a média das notas de um aluno por tipo de avaliação
     */
    @Transactional(readOnly = true)
    public Double calcularMediaAlunoPorTipo(Long alunoId, TipoAvaliacao tipo) {
        List<Nota> notas = buscarPorAlunoETipo(alunoId, tipo);
        
        if (notas.isEmpty()) {
            return 0.00;
        }
        
        BigDecimal somaNotas = BigDecimal.ZERO;
        BigDecimal somaPesos = BigDecimal.ZERO;
        
        for (Nota nota : notas) {
            BigDecimal peso = nota.getPeso() != null ? nota.getPeso() : BigDecimal.ONE;
            somaNotas = somaNotas.add(nota.getValor().multiply(peso));
            somaPesos = somaPesos.add(peso);
        }
        
        if (somaPesos.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        
        return somaNotas.divide(somaPesos, 2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * Calcula a média da turma em um registro de aula
     */
    @Transactional(readOnly = true)
    public Double calcularMediaTurma(Long registroAulaId) {
        List<Nota> notas = notaRepository.findByRegistroAulaId(registroAulaId);
        
        if (notas.isEmpty()) {
            return 0.0;
        }
        
        BigDecimal somaNotas = BigDecimal.ZERO;
        
        for (Nota nota : notas) {
            somaNotas = somaNotas.add(nota.getValor());
        }
        
        return somaNotas.divide(BigDecimal.valueOf(notas.size()), 2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * Busca notas em uma faixa de valores
     */
    @Transactional(readOnly = true)
    public List<Nota> buscarPorFaixaValor(BigDecimal valorMinimo, BigDecimal valorMaximo) {
        if (valorMinimo == null || valorMaximo == null) {
            throw new IllegalArgumentException("Valores mínimo e máximo não podem ser nulos");
        }
        if (valorMinimo.compareTo(valorMaximo) > 0) {
            throw new IllegalArgumentException("Valor mínimo não pode ser maior que o valor máximo");
        }
        
        return notaRepository.findAll().stream()
            .filter(nota -> nota.getValor().compareTo(valorMinimo) >= 0 && 
                           nota.getValor().compareTo(valorMaximo) <= 0)
            .toList();
    }

    /**
     * Verifica se aluno foi aprovado (média >= 7.0)
     */
    @Transactional(readOnly = true)
    public boolean verificarAprovacao(Long alunoId) {
        Double media = calcularMediaAluno(alunoId);
        return media.compareTo(7.0) >= 0;
    }

    /**
     * Deleta uma nota permanentemente
     */
    public void deletar(Long id) {
        if (!notaRepository.existsById(id)) {
            throw new RuntimeException("Nota não encontrada com ID: " + id);
        }
        notaRepository.deleteById(id);
    }

    /**
     * Cria uma nota básica
     */
    public Nota criarNotaBasica(Long alunoId, Long registroAulaId, BigDecimal valor, TipoAvaliacao tipo) {
        Aluno aluno = buscarAluno(alunoId);
        RegistroAula registroAula = buscarRegistroAula(registroAulaId);
        
        validarAluno(aluno);
        validarRegistroAula(registroAula);
        validarMatriculaAluno(aluno, registroAula);
        
        Nota nota = new Nota();
        nota.setAluno(aluno);
        nota.setRegistroAula(registroAula);
        nota.setValor(valor.setScale(2, RoundingMode.HALF_UP));
        nota.setTipo(tipo);
        nota.setPeso(BigDecimal.ONE);
        
        return notaRepository.save(nota);
    }


    /**
     * Métodos auxiliares para buscar entidades
     */
    private Aluno buscarAluno(Long alunoId) {
        return alunoRepository.findById(alunoId)
                .orElseThrow(() -> new NotaException.AlunoInvalidoException("Aluno não encontrado com ID: " + alunoId));
    }

    private RegistroAula buscarRegistroAula(Long registroAulaId) {
        return registroAulaRepository.findById(registroAulaId)
                .orElseThrow(() -> new NotaException.RegistroAulaInvalidoException("Registro de aula não encontrado com ID: " + registroAulaId));
    }

    /**
     * Valida os dados da nota
     */
    private void validarDadosNota(NotaDtoIn notaDto) {
        if (notaDto == null) {
            throw new NotaException.DadosInvalidosException("Dados da nota não podem ser nulos");
        }
        
        if (notaDto.getValor() == null) {
            throw new NotaException.DadosInvalidosException("Valor da nota é obrigatório");
        }
        
        if (notaDto.getValor().compareTo(BigDecimal.ZERO) < 0) {
            throw new NotaException.DadosInvalidosException("Valor da nota não pode ser negativo");
        }
        
        if (notaDto.getValor().compareTo(new BigDecimal("10.0")) > 0) {
            throw new NotaException.DadosInvalidosException("Valor da nota não pode ser maior que 10.0");
        }
        
        if (notaDto.getTipo() == null) {
            throw new NotaException.DadosInvalidosException("Tipo de avaliação é obrigatório");
        }
        
        if (notaDto.getAlunoId() == null) {
            throw new NotaException.DadosInvalidosException("ID do aluno é obrigatório");
        }
        
        if (notaDto.getRegistroAulaId() == null) {
            throw new NotaException.DadosInvalidosException("ID do registro de aula é obrigatório");
        }
        
        // Validação do peso
        if (notaDto.getPeso() != null && notaDto.getPeso().compareTo(BigDecimal.ZERO) <= 0) {
            throw new NotaException.DadosInvalidosException("Peso deve ser maior que zero");
        }
        
        // Validação do tamanho da observação
        if (notaDto.getObservacao() != null && notaDto.getObservacao().length() > 500) {
            throw new NotaException.DadosInvalidosException("Observação não pode exceder 500 caracteres");
        }
    }

    /**
     * Valida se aluno está ativo
     */
    private void validarAluno(Aluno aluno) {
        if (!aluno.getAtivo()) {
            throw new NotaException.AlunoInvalidoException("Não é possível atribuir nota para um aluno inativo");
        }
    }

    /**
     * Valida se registro de aula é válido
     */
    private void validarRegistroAula(RegistroAula registroAula) {
        // Validações específicas do registro de aula podem ser adicionadas aqui
        if (registroAula.getData() == null) {
            throw new NotaException.RegistroAulaInvalidoException("Registro de aula deve ter uma data válida");
        }
    }
    
    /**
     * Valida se aluno está matriculado na turma do registro de aula
     */
    private void validarMatriculaAluno(Aluno aluno, RegistroAula registroAula) {
        boolean alunoMatriculado = aluno.getMatriculas().stream()
            .anyMatch(matricula -> matricula.getTurma().getId().equals(registroAula.getTurma().getId()) &&
                                 matricula.getSituacao().name().equals("ATIVA"));
        
        if (!alunoMatriculado) {
            throw new NotaException.AlunoInvalidoException("Aluno não está matriculado na turma do registro de aula");
        }
    }
}