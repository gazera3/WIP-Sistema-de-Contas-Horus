package com.sistema.repository;

import com.sistema.model.AdminConfig;
import com.sistema.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminConfigRepository extends JpaRepository<AdminConfig, Long> {
    Optional<AdminConfig> findByAdmin(Usuario admin);
}
