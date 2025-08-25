package com.davi.gestaoescolar.gestao_escolar.repository;

import com.davi.gestaoescolar.gestao_escolar.model.Turma;
import com.davi.gestaoescolar.gestao_escolar.model.enums.Periodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {
    
    List<Turma> findByNomeContainingIgnoreCase(String nome);
    
    List<Turma> findByEscolaId(Long escolaId);
    
    List<Turma> findByAnoLetivo(String anoLetivo);
    
    List<Turma> findBySemestre(String semestre);
    
    List<Turma> findByPeriodo(Periodo periodo);
    
    List<Turma> findByAtivo(Boolean ativo);
    
    List<Turma> findByEscolaIdAndAtivo(Long escolaId, Boolean ativo);
    
    List<Turma> findByAnoLetivoAndSemestre(String anoLetivo, String semestre);
    
    List<Turma> findByEscolaIdAndAnoLetivoAndSemestre(Long escolaId, String anoLetivo, String semestre);
}