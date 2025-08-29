package com.davi.gestaoescolar.gestao_escolar.repository;

import com.davi.gestaoescolar.gestao_escolar.model.Comportamento;
import com.davi.gestaoescolar.gestao_escolar.model.enums.Gravidade;
import com.davi.gestaoescolar.gestao_escolar.model.enums.TipoComportamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ComportamentoRepository extends JpaRepository<Comportamento, Long> {
    
    List<Comportamento> findByTipo(TipoComportamento tipo);
    
    List<Comportamento> findByNivel(Gravidade nivel);
    
    List<Comportamento> findByDate(LocalDate date);
    
    List<Comportamento> findByDateBetween(LocalDate dataInicio, LocalDate dataFim);
    
    List<Comportamento> findByProfessorId(Long professorId);
    
    List<Comportamento> findByAlunoId(Long alunoId);
    
    @Query("SELECT c FROM Comportamento c JOIN c.aluno a JOIN a.matriculas m WHERE m.turma.id = :turmaId")
     List<Comportamento> findByTurmaId(@Param("turmaId") Long turmaId);
    
}