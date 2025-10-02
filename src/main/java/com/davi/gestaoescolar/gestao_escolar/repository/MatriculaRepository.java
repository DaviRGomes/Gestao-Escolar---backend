package com.davi.gestaoescolar.gestao_escolar.repository;

import com.davi.gestaoescolar.gestao_escolar.model.Matricula;
import com.davi.gestaoescolar.gestao_escolar.model.enums.SituacaoMatricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    
    List<Matricula> findByAlunoId(Long alunoId);
    
    List<Matricula> findByTurmaId(Long turmaId);
    
    List<Matricula> findBySituacao(SituacaoMatricula situacao);
    
    List<Matricula> findByDataMatricula(LocalDate dataMatricula);
    
    List<Matricula> findByDataMatriculaBetween(LocalDate dataInicio, LocalDate dataFim);
    
    List<Matricula> findByTurmaIdAndSituacao(Long turmaId, SituacaoMatricula situacao);
    
    List<Matricula> findByAlunoIdAndTurmaIdAndSituacao(Long alunoId, Long turmaId, SituacaoMatricula situacao);
}