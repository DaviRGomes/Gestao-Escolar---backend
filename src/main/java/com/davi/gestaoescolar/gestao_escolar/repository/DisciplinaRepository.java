package com.davi.gestaoescolar.gestao_escolar.repository;

import com.davi.gestaoescolar.gestao_escolar.model.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
    
    List<Disciplina> findByNomeContainingIgnoreCase(String nome);
    
    List<Disciplina> findByProfessorId(Long professorId);
    
    List<Disciplina> findByAtivoTrue();
    
    List<Disciplina> findByTurmasId(Long turmaId);
    
    List<Disciplina> findByCargaHorariaGreaterThanEqual(Integer cargaHorariaMinima);
}