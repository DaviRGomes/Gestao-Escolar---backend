package com.davi.gestaoescolar.gestao_escolar.repository;

import com.davi.gestaoescolar.gestao_escolar.model.Presenca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PresencaRepository extends JpaRepository<Presenca, Long> {
    
    List<Presenca> findByAlunoId(Long alunoId);
    
    List<Presenca> findByRegistroAulaId(Long registroAulaId);
    
    List<Presenca> findByPresente(Boolean presente);
    
    List<Presenca> findByAlunoIdAndPresente(Long alunoId, Boolean presente);
    
    List<Presenca> findByRegistroAulaIdAndPresente(Long registroAulaId, Boolean presente);
    
    List<Presenca> findByAlunoIdAndRegistroAulaId(Long alunoId, Long registroAulaId);
}