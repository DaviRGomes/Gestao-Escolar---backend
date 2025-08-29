package com.davi.gestaoescolar.gestao_escolar.repository;

import com.davi.gestaoescolar.gestao_escolar.model.AlunoResponsavel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoResponsavelRepository extends JpaRepository<AlunoResponsavel, Long> {
    
    List<AlunoResponsavel> findByAlunoId(Long alunoId);
    
    List<AlunoResponsavel> findByResponsavelId(Long responsavelId);
    
    Optional<AlunoResponsavel> findByAlunoIdAndResponsavelId(Long alunoId, Long responsavelId);
    
    List<AlunoResponsavel> findByPrincipalTrue();
    
    List<AlunoResponsavel> findByAlunoIdAndPrincipalTrue(Long alunoId);
}