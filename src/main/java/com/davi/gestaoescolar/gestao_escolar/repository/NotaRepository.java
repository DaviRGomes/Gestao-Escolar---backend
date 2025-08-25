package com.davi.gestaoescolar.gestao_escolar.repository;

import com.davi.gestaoescolar.gestao_escolar.model.Nota;
import com.davi.gestaoescolar.gestao_escolar.model.enums.TipoAvaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Long> {
    
    List<Nota> findByAlunoId(Long alunoId);
    
    List<Nota> findByRegistroAulaId(Long registroAulaId);
    
    List<Nota> findByTipo(TipoAvaliacao tipo);
    
    List<Nota> findByValorGreaterThanEqual(BigDecimal valorMinimo);
    
    List<Nota> findByValorLessThanEqual(BigDecimal valorMaximo);
    
    List<Nota> findByAlunoIdAndRegistroAulaId(Long alunoId, Long registroAulaId);
    
    List<Nota> findByAlunoIdAndTipo(Long alunoId, TipoAvaliacao tipo);
}