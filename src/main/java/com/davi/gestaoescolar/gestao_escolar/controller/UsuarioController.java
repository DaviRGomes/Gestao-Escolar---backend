package com.davi.gestaoescolar.gestao_escolar.controller;

import com.davi.gestaoescolar.gestao_escolar.dto.UsuarioDTO;
import com.davi.gestaoescolar.gestao_escolar.model.Usuario;
import com.davi.gestaoescolar.gestao_escolar.model.enums.Perfil;
import com.davi.gestaoescolar.gestao_escolar.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Criar novo usuário
     */
    @PostMapping
    public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario) {
        Usuario usuarioSalvo = usuarioService.salvar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }

    /**
     * Atualizar usuário existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        usuario.setId(id);
        Usuario usuarioAtualizado = usuarioService.atualizar(usuario);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    /**
     * Buscar usuário por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.buscarPorId(id);
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Buscar usuário por email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> buscarUsuarioPorEmail(@PathVariable String email) {
        Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Listar todos os usuários
     */
    @GetMapping
    public ResponseEntity<?> listarTodosUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
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