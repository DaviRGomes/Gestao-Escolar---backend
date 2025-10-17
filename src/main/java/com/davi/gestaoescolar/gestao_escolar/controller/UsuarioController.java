package com.davi.gestaoescolar.gestao_escolar.controller;

import com.davi.gestaoescolar.gestao_escolar.dto.Usuario.UsuarioDtoIn;
import com.davi.gestaoescolar.gestao_escolar.dto.Usuario.UsuarioDtoOut;
import com.davi.gestaoescolar.gestao_escolar.model.Usuario;
import com.davi.gestaoescolar.gestao_escolar.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Criar novo usuário (DTO)
     */
    @PostMapping
    public ResponseEntity<?> criarUsuario(@RequestBody UsuarioDtoIn dto) {
        UsuarioDtoOut usuarioSalvo = usuarioService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }

    /**
     * Atualizar usuário existente (converte DTO para entidade e retorna DTO)
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDtoIn dto) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setPerfil(dto.getPerfil());
        usuario.setDataNascimento(dto.getDataNascimento());
        usuario.setEndereco(dto.getEndereco());
        usuario.setAtivo(dto.getAtivo());

        Usuario usuarioAtualizado = usuarioService.atualizar(usuario);

        UsuarioDtoOut body = new UsuarioDtoOut(
            usuarioAtualizado.getId(),
            usuarioAtualizado.getEmail(),
            usuarioAtualizado.getPerfil(),
            usuarioAtualizado.getUltimoAcesso(),
            usuarioAtualizado.getDataNascimento(),
            usuarioAtualizado.getEndereco(),
            usuarioAtualizado.getAtivo()
        );
        return ResponseEntity.ok(body);
    }

    /**
     * Buscar usuário por ID (retorna DTO)
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.buscarPorId(id);
        if (usuario.isPresent()) {
            Usuario u = usuario.get();
            UsuarioDtoOut body = new UsuarioDtoOut(
                u.getId(),
                u.getEmail(),
                u.getPerfil(),
                u.getUltimoAcesso(),
                u.getDataNascimento(),
                u.getEndereco(),
                u.getAtivo()
            );
            return ResponseEntity.ok(body);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Buscar usuário por email (retorna DTO)
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> buscarUsuarioPorEmail(@PathVariable String email) {
        Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);
        if (usuario.isPresent()) {
            Usuario u = usuario.get();
            UsuarioDtoOut body = new UsuarioDtoOut(
                u.getId(),
                u.getEmail(),
                u.getPerfil(),
                u.getUltimoAcesso(),
                u.getDataNascimento(),
                u.getEndereco(),
                u.getAtivo()
            );
            return ResponseEntity.ok(body);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Listar todos os usuários (retorna lista de DTO)
     */
    @GetMapping
    public ResponseEntity<?> listarTodosUsuarios(@RequestParam(required = false) String email) {
        if (email != null && !email.trim().isEmpty()) {
            Optional<Usuario> usuario = usuarioService.buscarPorEmail(email.trim());
            List<UsuarioDtoOut> resultado = usuario
                .map(u -> Collections.singletonList(new UsuarioDtoOut(
                    u.getId(),
                    u.getEmail(),
                    u.getPerfil(),
                    u.getUltimoAcesso(),
                    u.getDataNascimento(),
                    u.getEndereco(),
                    u.getAtivo()
                )))
                .orElseGet(Collections::emptyList);
            return ResponseEntity.ok(resultado);
        }
        List<Usuario> usuarios = usuarioService.listarTodos();
        List<UsuarioDtoOut> body = usuarios.stream()
            .map(u -> new UsuarioDtoOut(
                u.getId(),
                u.getEmail(),
                u.getPerfil(),
                u.getUltimoAcesso(),
                u.getDataNascimento(),
                u.getEndereco(),
                u.getAtivo()
            ))
            .collect(Collectors.toList());
        return ResponseEntity.ok(body);
    }

    /**
     * Deletar usuário permanentemente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.ok("Usuário deletado com sucesso");
    }
}