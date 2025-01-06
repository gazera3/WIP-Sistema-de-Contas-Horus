package com.sistema.repository;

import com.sistema.model.UsuarioAdmin;
import com.sistema.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UsuarioAdminRepository extends JpaRepository<UsuarioAdmin, Long> {
    List<UsuarioAdmin> findByAdmin(Usuario admin);
    int countByAdmin(Usuario admin);
}