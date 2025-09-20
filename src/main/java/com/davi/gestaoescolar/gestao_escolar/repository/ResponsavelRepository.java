package com.davi.gestaoescolar.gestao_escolar.repository;

import com.davi.gestaoescolar.gestao_escolar.model.Responsavel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResponsavelRepository extends JpaRepository<Responsavel, Long> {
    
    Optional<Responsavel> findByCpf(String cpf);
    
    List<Responsavel> findByNomeContainingIgnoreCase(String nome);

    
    List<Responsavel> findByTelefoneContaining(String telefone);
  
}