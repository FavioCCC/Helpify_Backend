package com.webcrafters.helpify.repositorios;

import com.webcrafters.helpify.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
}

