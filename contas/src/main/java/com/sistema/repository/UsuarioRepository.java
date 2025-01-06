package com.sistema.repository;

import com.sistema.model.Usuario;
import com.sistema.model.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsuario(String usuario);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByTipo(TipoUsuario tipo);
    boolean existsByUsuario(String usuario);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE u.admin = :admin")
    List<Usuario> findByAdmin(@Param("admin") Usuario admin);

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.admin = :admin")
    long countByAdmin(@Param("admin") Usuario admin);
}