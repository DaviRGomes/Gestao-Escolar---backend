package com.davi.gestaoescolar.gestao_escolar.service;

import com.davi.gestaoescolar.gestao_escolar.model.Usuario;
import com.davi.gestaoescolar.gestao_escolar.model.enums.Perfil;
import com.davi.gestaoescolar.gestao_escolar.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Salva um novo usuário
     */
    public Usuario salvar(Usuario usuario) {
        if (usuario.getSenha() != null) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
        usuario.setAtivo(true);
        return usuarioRepository.save(usuario);
    }

    /**
     * Atualiza um usuário existente
     */
    public Usuario atualizar(Usuario usuario) {
        if (usuario.getId() == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo para atualização");
        }
        
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(usuario.getId());
        if (usuarioExistente.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado com ID: " + usuario.getId());
        }
        
        // Se a senha foi alterada, criptografar
        if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        } else {
            // Manter a senha existente se não foi fornecida nova senha
            usuario.setSenha(usuarioExistente.get().getSenha());
        }
        
        return usuarioRepository.save(usuario);
    }

    /**
     * Busca usuário por ID
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Busca usuário por email
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Lista todos os usuários
     */
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    /**
     * Lista usuários por perfil
     */
    @Transactional(readOnly = true)
    public List<Usuario> listarPorPerfil(Perfil perfil) {
        return usuarioRepository.findByPerfil(perfil);
    }

    /**
     * Lista usuários ativos
     */
    @Transactional(readOnly = true)
    public List<Usuario> listarAtivos() {
        return usuarioRepository.findByAtivo(true);
    }

    /**
     * Lista usuários inativos
     */
    @Transactional(readOnly = true)
    public List<Usuario> listarInativos() {
        return usuarioRepository.findByAtivo(false);
    }

    /**
     * Lista usuários por perfil e status ativo
     */
    @Transactional(readOnly = true)
    public List<Usuario> listarPorPerfilEAtivo(Perfil perfil, Boolean ativo) {
        return usuarioRepository.findByPerfilAndAtivo(perfil, ativo);
    }

    /**
     * Desativa um usuário (soft delete)
     */
    public void desativar(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            usuario.get().setAtivo(false);
            usuarioRepository.save(usuario.get());
        } else {
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }
    }

    /**
     * Reativa um usuário
     */
    public void reativar(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            usuario.get().setAtivo(true);
            usuarioRepository.save(usuario.get());
        } else {
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }
    }

    /**
     * Atualiza o último acesso do usuário
     */
    public void atualizarUltimoAcesso(String email) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if (usuario.isPresent()) {
            usuario.get().setUltimoAcesso(LocalDateTime.now());
            usuarioRepository.save(usuario.get());
        }
    }

    /**
     * Verifica se email já existe
     */
    @Transactional(readOnly = true)
    public boolean emailJaExiste(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    /**
     * Verifica se email já existe para outro usuário (útil para atualizações)
     */
    @Transactional(readOnly = true)
    public boolean emailJaExisteParaOutroUsuario(String email, Long idUsuario) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        return usuario.isPresent() && !usuario.get().getId().equals(idUsuario);
    }

    /**
     * Altera senha do usuário
     */
    public void alterarSenha(Long id, String novaSenha) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            usuario.get().setSenha(passwordEncoder.encode(novaSenha));
            usuarioRepository.save(usuario.get());
        } else {
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }
    }

    /**
     * Remove usuário permanentemente (hard delete)
     */
    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    /**
     * Lista usuários com último acesso antes de uma data específica
     */
    @Transactional(readOnly = true)
    public List<Usuario> listarComUltimoAcessoAntesDe(LocalDateTime data) {
        return usuarioRepository.findByUltimoAcessoBefore(data);
    }

    /**
     * Lista usuários com último acesso depois de uma data específica
     */
    @Transactional(readOnly = true)
    public List<Usuario> listarComUltimoAcessoDepoisDe(LocalDateTime data) {
        return usuarioRepository.findByUltimoAcessoAfter(data);
    }
}