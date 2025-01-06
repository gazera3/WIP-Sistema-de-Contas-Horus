package com.sistema.controller;

import com.sistema.model.Usuario;
import com.sistema.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final UsuarioService usuarioService;

    @Autowired
    public DashboardController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/admins")
    public ResponseEntity<?> getAdmins(@RequestParam Long superAdminId) {
        try {
            List<Usuario> admins = usuarioService.listarTodosAdmins(superAdminId);
            return ResponseEntity.ok(admins);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@RequestParam Long superAdminId) {
        try {
            List<Usuario> users = usuarioService.listarTodosUsuarios(superAdminId);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/admin/{adminId}")
    public ResponseEntity<?> removeAdmin(@RequestParam Long superAdminId, @PathVariable Long adminId) {
        try {
            usuarioService.removerAdmin(superAdminId, adminId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> removeUser(@RequestParam Long superAdminId, @PathVariable Long userId) {
        try {
            usuarioService.removerQualquerUsuario(superAdminId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}