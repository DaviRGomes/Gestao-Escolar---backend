package com.davi.gestaoescolar.gestao_escolar.repository;

import com.davi.gestaoescolar.gestao_escolar.model.RegistroAula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RegistroAulaRepository extends JpaRepository<RegistroAula, Long> {
    
    List<RegistroAula> findByData(LocalDate data);
    
    List<RegistroAula> findByDataBetween(LocalDate dataInicio, LocalDate dataFim);
    
    List<RegistroAula> findByTurmaId(Long turmaId);
    
    List<RegistroAula> findByDisciplinaId(Long disciplinaId);
    
    List<RegistroAula> findByTurmaIdAndDisciplinaId(Long turmaId, Long disciplinaId);
    
    List<RegistroAula> findByConteudoPlanejadoId(Long conteudoPlanejadoId);
    
    List<RegistroAula> findByTurmaIdAndDataBetween(Long turmaId, LocalDate dataInicio, LocalDate dataFim);
    
    List<RegistroAula> findByDisciplinaIdAndDataBetween(Long disciplinaId, LocalDate dataInicio, LocalDate dataFim);
}