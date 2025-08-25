package com.davi.gestaoescolar.gestao_escolar.repository;

import com.davi.gestaoescolar.gestao_escolar.model.Planejamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanejamentoRepository extends JpaRepository<Planejamento, Long> {
    
    List<Planejamento> findByDisciplinaId(Long disciplinaId);
    
    List<Planejamento> findByTurmaId(Long turmaId);
    
    List<Planejamento> findByAno(Integer ano);
    
    List<Planejamento> findBySemestre(String semestre);
    
    List<Planejamento> findByAnoAndSemestre(Integer ano, String semestre);
    
    Optional<Planejamento> findByDisciplinaIdAndTurmaIdAndSemestreAndAno(Long disciplinaId, Long turmaId, String semestre, Integer ano);
}