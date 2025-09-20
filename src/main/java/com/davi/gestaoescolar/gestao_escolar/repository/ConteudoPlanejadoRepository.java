package com.davi.gestaoescolar.gestao_escolar.repository;

import com.davi.gestaoescolar.gestao_escolar.model.ConteudoPlanejado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConteudoPlanejadoRepository extends JpaRepository<ConteudoPlanejado, Long> {
    
    List<ConteudoPlanejado> findByPlanejamentoId(Long planejamentoId);
    
    List<ConteudoPlanejado> findByDataPrevista(LocalDate dataPrevista);
    
    List<ConteudoPlanejado> findByDataPrevistaBetween(LocalDate dataInicio, LocalDate dataFim);
    
    List<ConteudoPlanejado> findByConcluidoTrue();
    
    List<ConteudoPlanejado> findByConcluidoFalse();
    

}