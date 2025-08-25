package com.davi.gestaoescolar.gestao_escolar.repository;

import com.davi.gestaoescolar.gestao_escolar.model.Escola;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EscolaRepository extends JpaRepository<Escola, Long> {
    
    Optional<Escola> findByCnpj(String cnpj);
    
    List<Escola> findByNomeContainingIgnoreCase(String nome);
    
    List<Escola> findByAtivoTrue();
    
    List<Escola> findByEnderecoContainingIgnoreCase(String endereco);
}