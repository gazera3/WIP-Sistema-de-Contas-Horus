package com.sistema.controller;

import com.sistema.model.Usuario;
import com.sistema.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.Data;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final UsuarioService usuarioService;

    @Autowired
    public AdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/criar-super-admin")
    public ResponseEntity<?> criarSuperAdmin(@RequestBody SuperAdminRequest request) {
        try {
            Usuario superAdmin = usuarioService.criarSuperAdmin(
                    request.getUsuario(),
                    request.getNome(),
                    request.getEmail(),
                    request.getDocumento(),
                    request.getSenha()
            );
            return ResponseEntity.ok(superAdmin);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/criar")
    public ResponseEntity<?> criarAdmin(@RequestBody AdminRequest request) {
        try {
            Usuario admin = usuarioService.criarAdmin(
                    request.getUsuario(),
                    request.getNome(),
                    request.getEmail(),
                    request.getDocumento(),
                    request.getSenha(),
                    request.getSuperAdminId(),
                    request.getMaxConexoes()
            );
            return ResponseEntity.ok(admin);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Implementar lógica de login
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarAdmins(@RequestParam Long superAdminId) {
        try {
            List<Usuario> admins = usuarioService.listarTodosAdmins(superAdminId);
            return ResponseEntity.ok(admins);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

@Data
class SuperAdminRequest {
    private String usuario;
    private String nome;
    private String email;
    private String documento;
    private String senha;
}

@Data
class AdminRequest {
    private String usuario;
    private String nome;
    private String email;
    private String documento;
    private String senha;
    private Long superAdminId;
    private int maxConexoes;
}

@Data
class LoginRequest {
    private String usuario;
    private String senha;
}