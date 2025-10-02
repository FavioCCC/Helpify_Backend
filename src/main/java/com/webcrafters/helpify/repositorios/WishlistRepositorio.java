package com.webcrafters.helpify.repositorios;

import com.webcrafters.helpify.seguridad.entidades.Usuario;
import com.webcrafters.helpify.entidades.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepositorio extends JpaRepository<Wishlist, Long> {
    Optional<Wishlist> findByUsuario(Usuario usuario);
}