package com.webcrafters.helpify.seguridad.repositorios;

import com.webcrafters.helpify.seguridad.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByNombre(String nombre);
    // Detección de duplicados usando los nombres de campos del DTO (correo, numerodocumento)
    boolean existsByCorreo(String correo);
    boolean existsByNumerodocumento(String numerodocumento);
}

