package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.dto.Aluno.AlunoDtoIn;
import com.davi.gestaoescolar.gestao_escolar.dto.Aluno.AlunoDtoOut;
import com.davi.gestaoescolar.gestao_escolar.dto.Matricula.MatriculaDtoSimples;
import com.davi.gestaoescolar.gestao_escolar.dto.Responsavel.ResponsavelDtoSimples;
import com.davi.gestaoescolar.gestao_escolar.exception.AlunoException;
import com.davi.gestaoescolar.gestao_escolar.model.Aluno;
import com.davi.gestaoescolar.gestao_escolar.model.AlunoResponsavel;
import com.davi.gestaoescolar.gestao_escolar.model.Responsavel;
import com.davi.gestaoescolar.gestao_escolar.repository.AlunoRepository;
import com.davi.gestaoescolar.gestao_escolar.repository.AlunoResponsavelRepository;
import com.davi.gestaoescolar.gestao_escolar.repository.ResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;
    
    @Autowired
    private AlunoResponsavelRepository alunoResponsavelRepository;
    
    @Autowired
    private ResponsavelRepository responsavelRepository;

    /**
     * Métodos auxiliares para conversão de DTOs
     */
    private List<AlunoDtoOut> toDtos(List<Aluno> alunos) {
        return alunos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private AlunoDtoOut toDTO(Aluno aluno) {
        List<MatriculaDtoSimples> matriculasDTO = aluno.getMatriculas().stream()
                .map(m -> new MatriculaDtoSimples(
                    // preencha os campos conforme seu DTO simples de matrícula
                ))
                .collect(Collectors.toList());

        List<ResponsavelDtoSimples> responsaveisDTO = aluno.getResponsaveis().stream()
                .map(ar -> new ResponsavelDtoSimples(
                    ar.getResponsavel().getId(),
                    ar.getResponsavel().getNome(),
                    ar.getResponsavel().getParentesco()
                ))
                .collect(Collectors.toList());

        return new AlunoDtoOut(
                aluno.getId(),
                aluno.getNome(),
                aluno.getDataNascimento(),
                aluno.getCpf(),
                aluno.getObservacoes(),
                aluno.getAtivo(),
                matriculasDTO,
                responsaveisDTO
        );
    }

    private Optional<AlunoDtoOut> toDTO(Optional<Aluno> aluno) {
        return aluno.map(this::toDTO);
    }

    /**
     * Salva um novo aluno no banco de dados
     * @param alunoCreate Dados do aluno a ser criado
     * @return DTO do aluno salvo
     */
    public AlunoDtoOut salvar(AlunoDtoIn alunoCreate) {
        validarDadosAluno(alunoCreate);
        
        if (cpfJaExiste(alunoCreate.getCpf())) {
            throw new AlunoException.ConflitoException("CPF já cadastrado no sistema");
        }
        
        Aluno aluno = new Aluno();
        aluno.setNome(alunoCreate.getNome());
        aluno.setDataNascimento(alunoCreate.getDataNascimento());
        aluno.setCpf(alunoCreate.getCpf());
        aluno.setObservacoes(alunoCreate.getObservacoes());
        aluno.setAtivo(alunoCreate.getAtivo());
        
        Aluno alunoSalvo = alunoRepository.save(aluno);
        
        // Se há responsáveis, criar as relações
        if (alunoCreate.getResponsaveis() != null && !alunoCreate.getResponsaveis().isEmpty()) {
            for (AlunoDtoIn.ResponsavelDTO responsavelDTO : alunoCreate.getResponsaveis()) {
                criarRelacaoResponsavel(alunoSalvo, responsavelDTO);
            }
        }
        
        return toDTO(alunoSalvo);
    }
    
    private void criarRelacaoResponsavel(Aluno aluno, AlunoDtoIn.ResponsavelDTO responsavelDTO) {
        // Buscar ou criar responsável
        Responsavel responsavel = responsavelRepository.findByCpf(responsavelDTO.getCpf())
                .orElseGet(() -> {
                    Responsavel novoResponsavel = new Responsavel();
                    novoResponsavel.setNome(responsavelDTO.getNome());
                    novoResponsavel.setTelefone(responsavelDTO.getTelefone());
                    novoResponsavel.setCpf(responsavelDTO.getCpf());
                    novoResponsavel.setParentesco(responsavelDTO.getParentesco());
                    return responsavelRepository.save(novoResponsavel);
                });
        
        // Verificar se já existe relação entre aluno e responsável
        Optional<AlunoResponsavel> relacaoExistente = alunoResponsavelRepository
                .findByAlunoIdAndResponsavelId(aluno.getId(), responsavel.getId());
        
        if (relacaoExistente.isPresent()) {
            throw new AlunoException.ConflitoException("Relacionamento já existe entre este aluno e responsável");
        }
        
        // Criar relação aluno-responsável
        AlunoResponsavel alunoResponsavel = new AlunoResponsavel();
        alunoResponsavel.setAluno(aluno);
        alunoResponsavel.setResponsavel(responsavel);
        
        // Definir se é principal baseado no DTO ou se é o primeiro responsável
        Boolean isPrincipal = responsavelDTO.getPrincipal() != null ? responsavelDTO.getPrincipal() : false;
        
        // Se for marcado como principal, verificar se já existe um responsável principal
        if (isPrincipal) {
            List<AlunoResponsavel> responsaveisPrincipais = alunoResponsavelRepository
                    .findByAlunoIdAndPrincipalTrue(aluno.getId());
            
            if (!responsaveisPrincipais.isEmpty()) {
                throw new AlunoException.ConflitoException("Aluno já possui um responsável principal");
            }
        }
        
        alunoResponsavel.setPrincipal(isPrincipal);
        
        // Salvar a relação no banco de dados
        alunoResponsavelRepository.save(alunoResponsavel);
    }

    /**
     * Atualiza um aluno existente
     * @param id ID do aluno a ser atualizado
     * @param alunoCreate Dados atualizados do aluno
     * @return DTO do aluno atualizado
     */
    public AlunoDtoOut atualizar(Long id, AlunoDtoIn alunoCreate) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new AlunoException.AlunoNaoEncontradoException(id));
        
        // Verificar se o CPF foi alterado e se já existe
        if (!aluno.getCpf().equals(alunoCreate.getCpf()) && cpfJaExiste(alunoCreate.getCpf())) {
            throw new AlunoException.ConflitoException("CPF já cadastrado: " + alunoCreate.getCpf());
        }
        
        validarDadosAluno(alunoCreate);
        
        aluno.setNome(alunoCreate.getNome());
        aluno.setDataNascimento(alunoCreate.getDataNascimento());
        aluno.setCpf(alunoCreate.getCpf());
        aluno.setObservacoes(alunoCreate.getObservacoes());
        aluno.setAtivo(alunoCreate.getAtivo());
        
        Aluno alunoAtualizado = alunoRepository.save(aluno);
        
        // Se há responsáveis para atualizar, remover os existentes e criar novos
        if (alunoCreate.getResponsaveis() != null) {
            // Remover relações existentes
            List<AlunoResponsavel> relacoesExistentes = alunoResponsavelRepository.findByAlunoId(aluno.getId());
            alunoResponsavelRepository.deleteAll(relacoesExistentes);
            
            // Criar novas relações
            for (AlunoDtoIn.ResponsavelDTO responsavelDTO : alunoCreate.getResponsaveis()) {
                criarRelacaoResponsavel(alunoAtualizado, responsavelDTO);
            }
        }
        
        return toDTO(alunoAtualizado);
    }

    /**
     * Busca um aluno por ID
     * @param id ID do aluno
     * @return DTO do aluno encontrado ou Optional vazio
     */
    public Optional<AlunoDtoOut> buscarPorId(Long id) {
        Optional<AlunoDtoOut> aluno = toDTO(alunoRepository.findById(id));
        if(aluno.isEmpty()){
            throw new AlunoException.AlunoNaoEncontradoException(id);
        }
        return aluno;
    }

    /**
     * Busca um aluno por CPF
     * @param cpf CPF do aluno
     * @return DTO do aluno encontrado ou Optional vazio
     */
    public Optional<AlunoDtoOut> buscarPorCpf(String cpf) {
        return toDTO(alunoRepository.findByCpf(cpf));
    }

    /**
     * Busca alunos por nome (busca parcial)
     * @param nome Nome ou parte do nome
     * @return Lista de DTOs dos alunos encontrados
     */
    public List<AlunoDtoOut> buscarPorNome(String nome) {
        return toDtos(alunoRepository.findByNomeContainingIgnoreCase(nome));
    }

    /**
     * Lista todos os alunos
     * @return Lista de DTOs de todos os alunos
     */
    public List<AlunoDtoOut> listarTodos() {
        return toDtos(alunoRepository.findAll());
    }

    /**
     * Lista apenas alunos ativos
     * @return Lista de DTOs dos alunos ativos
     */
    public List<AlunoDtoOut> listarAtivos() {
        return toDtos(alunoRepository.findByAtivoTrue());
    }

    public AlunoDtoOut ativar(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new AlunoException.AlunoNaoEncontradoException(id));
        aluno.setAtivo(true);
        Aluno alunoAtivado = alunoRepository.save(aluno);
        return toDTO(alunoAtivado);
    }

    public AlunoDtoOut desativar(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new AlunoException.AlunoNaoEncontradoException(id));
        aluno.setAtivo(false);
        Aluno alunoDesativado = alunoRepository.save(aluno);
        return toDTO(alunoDesativado);
    }

    /**
     * Altera o status de ativo do aluno a partir de 0 ou 1 (validação de dados com IllegalArgumentException)
     */
    public AlunoDtoOut alterarStatus(Long id, Integer ativoParam) {
        if (ativoParam == null || (ativoParam != 0 && ativoParam != 1)) {
            throw new IllegalArgumentException("Parâmetro 'ativo' deve ser 0 ou 1");
        }
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new AlunoException.AlunoNaoEncontradoException(id));

        aluno.setAtivo(ativoParam == 1);

        Aluno alunoAtualizado = alunoRepository.save(aluno);
        return toDTO(alunoAtualizado);
    }

    /**
     * Deleta um aluno permanentemente
     * @param id ID do aluno a ser deletado
     */
    public void deletar(Long id) {
        if (!alunoRepository.existsById(id)) {
            throw new AlunoException.AlunoNaoEncontradoException(id);
        }
        alunoRepository.deleteById(id);
    }

    /**
     * Verifica se um CPF já existe no banco de dados
     * @param cpf CPF a ser verificado
     * @return true se o CPF já existe, false caso contrário
     */
    private boolean cpfJaExiste(String cpf) {
        return alunoRepository.findByCpf(cpf).isPresent();
    }

    /**
     * Validações dos dados do aluno
     */
    private void validarDadosAluno(AlunoDtoIn alunoCreate) {
        validarDadosAluno(alunoCreate.getNome(), alunoCreate.getCpf(), alunoCreate.getDataNascimento());
    }

    
    private void validarDadosAluno(String nome, String cpf, LocalDate dataNascimento) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (nome.length() < 2) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres");
        }
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório");
        }
        if (!validarFormatoCpf(cpf)) {
            throw new IllegalArgumentException("CPF deve ter formato válido (XXX.XXX.XXX-XX)");
        }
        if (dataNascimento == null) {
            throw new IllegalArgumentException("Data de nascimento é obrigatória");
        }
        if (dataNascimento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de nascimento não pode ser futura");
        }
    }

    /**
     * Valida formato do CPF
     */
    private boolean validarFormatoCpf(String cpf) {
        if (cpf == null) return false;
        // Remove pontos e traços
        String cpfLimpo = cpf.replaceAll("[.\\-]", "");
        // Verifica se tem 11 dígitos
        return cpfLimpo.matches("\\d{11}");
    }
}