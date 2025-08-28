package com.davi.gestaoescolar.gestao_escolar.repository;

import com.davi.gestaoescolar.gestao_escolar.model.Usuario;
import com.davi.gestaoescolar.gestao_escolar.model.enums.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByEmail(String email);
    
    List<Usuario> findByPerfil(Perfil perfil);
    
    List<Usuario> findByAtivo(Boolean ativo);
    
    
    List<Usuario> findByUltimoAcessoBefore(LocalDateTime data);
    
    List<Usuario> findByUltimoAcessoAfter(LocalDateTime data);
    

    
    List<Usuario> findByPerfilAndAtivo(Perfil perfil, Boolean ativo);
}