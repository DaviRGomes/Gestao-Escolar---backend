package com.davi.gestaoescolar.gestao_escolar.repository;

import com.davi.gestaoescolar.gestao_escolar.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    
    Optional<Professor> findByCpf(String cpf);
    
    List<Professor> findByNomeContainingIgnoreCase(String nome);
    
    Optional<Professor> findByEmail(String email);
    
    List<Professor> findByFormacaoContainingIgnoreCase(String formacao);
    
    List<Professor> findByTelefoneContaining(String telefone);
}