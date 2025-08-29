package com.davi.gestaoescolar.gestao_escolar.service;

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
import java.util.Optional;

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
     * Salva uma nova nota
     */
    public Nota salvar(Nota nota) {
        validarDadosNota(nota);
        validarAlunoERegistroAula(nota.getAluno(), nota.getRegistroAula());
        
        // Define peso padrão se não foi informado
        if (nota.getPeso() == null) {
            nota.setPeso(BigDecimal.ONE);
        }
        
        return notaRepository.save(nota);
    }

    /**
     * Atualiza uma nota existente
     */
    public Nota atualizar(Long id, Nota notaAtualizada) {
        Optional<Nota> notaExistente = notaRepository.findById(id);
        if (notaExistente.isEmpty()) {
            throw new RuntimeException("Nota não encontrada com ID: " + id);
        }

        validarDadosNota(notaAtualizada);
        validarAlunoERegistroAula(notaAtualizada.getAluno(), notaAtualizada.getRegistroAula());
        
        Nota nota = notaExistente.get();
        nota.setValor(notaAtualizada.getValor());
        nota.setTipo(notaAtualizada.getTipo());
        nota.setPeso(notaAtualizada.getPeso());
        nota.setObservacao(notaAtualizada.getObservacao());
        nota.setAluno(notaAtualizada.getAluno());
        nota.setRegistroAula(notaAtualizada.getRegistroAula());
        
        return notaRepository.save(nota);
    }

    /**
     * Busca nota por ID
     */
    @Transactional(readOnly = true)
    public Optional<Nota> buscarPorId(Long id) {
        return notaRepository.findById(id);
    }

    /**
     * Busca notas por aluno
     */
    @Transactional(readOnly = true)
    public List<Nota> buscarPorAluno(Long alunoId) {
        if (alunoId == null) {
            throw new IllegalArgumentException("ID do aluno não pode ser nulo");
        }
        return notaRepository.findByAlunoId(alunoId);
    }

    /**
     * Busca notas por registro de aula
     */
    @Transactional(readOnly = true)
    public List<Nota> buscarPorRegistroAula(Long registroAulaId) {
        if (registroAulaId == null) {
            throw new IllegalArgumentException("ID do registro de aula não pode ser nulo");
        }
        return notaRepository.findByRegistroAulaId(registroAulaId);
    }

    /**
     * Busca notas por tipo de avaliação
     */
    @Transactional(readOnly = true)
    public List<Nota> buscarPorTipo(TipoAvaliacao tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de avaliação não pode ser nulo");
        }
        return notaRepository.findByTipo(tipo);
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
    public List<Nota> listarTodas() {
        return notaRepository.findAll();
    }

    /**
     * Calcula a média das notas de um aluno
     */
    @Transactional(readOnly = true)
    public BigDecimal calcularMediaAluno(Long alunoId) {
        List<Nota> notas = buscarPorAluno(alunoId);
        
        if (notas.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal somaNotas = BigDecimal.ZERO;
        BigDecimal somaPesos = BigDecimal.ZERO;
        
        for (Nota nota : notas) {
            BigDecimal peso = nota.getPeso() != null ? nota.getPeso() : BigDecimal.ONE;
            somaNotas = somaNotas.add(nota.getValor().multiply(peso));
            somaPesos = somaPesos.add(peso);
        }
        
        if (somaPesos.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        return somaNotas.divide(somaPesos, 2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula a média das notas de um aluno por tipo de avaliação
     */
    @Transactional(readOnly = true)
    public BigDecimal calcularMediaAlunoPorTipo(Long alunoId, TipoAvaliacao tipo) {
        List<Nota> notas = buscarPorAlunoETipo(alunoId, tipo);
        
        if (notas.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal somaNotas = BigDecimal.ZERO;
        BigDecimal somaPesos = BigDecimal.ZERO;
        
        for (Nota nota : notas) {
            BigDecimal peso = nota.getPeso() != null ? nota.getPeso() : BigDecimal.ONE;
            somaNotas = somaNotas.add(nota.getValor().multiply(peso));
            somaPesos = somaPesos.add(peso);
        }
        
        if (somaPesos.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        return somaNotas.divide(somaPesos, 2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula a média da turma em um registro de aula
     */
    @Transactional(readOnly = true)
    public BigDecimal calcularMediaTurma(Long registroAulaId) {
        List<Nota> notas = buscarPorRegistroAula(registroAulaId);
        
        if (notas.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal somaNotas = BigDecimal.ZERO;
        
        for (Nota nota : notas) {
            somaNotas = somaNotas.add(nota.getValor());
        }
        
        return somaNotas.divide(BigDecimal.valueOf(notas.size()), 2, RoundingMode.HALF_UP);
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
        BigDecimal media = calcularMediaAluno(alunoId);
        return media.compareTo(new BigDecimal("7.0")) >= 0;
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
        Optional<Aluno> aluno = alunoRepository.findById(alunoId);
        if (aluno.isEmpty()) {
            throw new RuntimeException("Aluno não encontrado com ID: " + alunoId);
        }
        
        Optional<RegistroAula> registroAula = registroAulaRepository.findById(registroAulaId);
        if (registroAula.isEmpty()) {
            throw new RuntimeException("Registro de aula não encontrado com ID: " + registroAulaId);
        }
        
        Nota nota = new Nota();
        nota.setAluno(aluno.get());
        nota.setRegistroAula(registroAula.get());
        nota.setValor(valor);
        nota.setTipo(tipo);
        nota.setPeso(BigDecimal.ONE);
        
        return salvar(nota);
    }

    /**
     * Valida os dados da nota
     */
    private void validarDadosNota(Nota nota) {
        if (nota == null) {
            throw new IllegalArgumentException("Nota não pode ser nula");
        }
        
        if (nota.getValor() == null) {
            throw new IllegalArgumentException("Valor da nota é obrigatório");
        }
        
        if (nota.getValor().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor da nota não pode ser negativo");
        }
        
        if (nota.getValor().compareTo(new BigDecimal("10.0")) > 0) {
            throw new IllegalArgumentException("Valor da nota não pode ser maior que 10.0");
        }
        
        if (nota.getTipo() == null) {
            throw new IllegalArgumentException("Tipo de avaliação é obrigatório");
        }
        
        if (nota.getAluno() == null || nota.getAluno().getId() == null) {
            throw new IllegalArgumentException("Aluno é obrigatório");
        }
        
        if (nota.getRegistroAula() == null || nota.getRegistroAula().getId() == null) {
            throw new IllegalArgumentException("Registro de aula é obrigatório");
        }
        
        // Validação do peso
        if (nota.getPeso() != null && nota.getPeso().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Peso deve ser maior que zero");
        }
        
        // Validação do tamanho da observação
        if (nota.getObservacao() != null && nota.getObservacao().length() > 500) {
            throw new IllegalArgumentException("Observação não pode exceder 500 caracteres");
        }
        
        // Arredonda o valor para 2 casas decimais
        nota.setValor(nota.getValor().setScale(2, RoundingMode.HALF_UP));
        
        // Arredonda o peso para 2 casas decimais se fornecido
        if (nota.getPeso() != null) {
            nota.setPeso(nota.getPeso().setScale(2, RoundingMode.HALF_UP));
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
            throw new RuntimeException("Não é possível atribuir nota para um aluno inativo");
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