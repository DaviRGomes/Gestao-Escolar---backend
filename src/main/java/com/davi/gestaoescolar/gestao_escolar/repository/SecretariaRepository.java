package com.davi.gestaoescolar.gestao_escolar.repository;

import com.davi.gestaoescolar.gestao_escolar.model.Secretaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SecretariaRepository extends JpaRepository<Secretaria, Long> {

    // Buscar por email
    Optional<Secretaria> findByEmail(String email);

    // Buscar por CPF
    Optional<Secretaria> findByCpf(String cpf);

    // Buscar por matrícula
    Optional<Secretaria> findByMatricula(String matricula);

    // Buscar por nome (contendo)
    List<Secretaria> findByNomeContainingIgnoreCase(String nome);

    // Buscar por cargo
    List<Secretaria> findByCargo(String cargo);

    // Buscar por status ativo
    List<Secretaria> findByAtivo(Boolean ativo);

    // Buscar por período de contratação
    List<Secretaria> findByDataContratacaoBetween(LocalDate dataInicio, LocalDate dataFim);

    // Buscar por data de contratação
    List<Secretaria> findByDataContratacao(LocalDate dataContratacao);
}