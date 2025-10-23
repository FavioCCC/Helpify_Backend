package com.webcrafters.helpify.controladores;

import com.webcrafters.helpify.DTO.ProyectoSoloConDatosDTO;
import com.webcrafters.helpify.seguridad.entidades.Usuario;
import com.webcrafters.helpify.seguridad.repositorios.UsuarioRepositorio;
import com.webcrafters.helpify.servicios.WishlistService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;
    private final UsuarioRepositorio usuarioRepositorio;

    public WishlistController(WishlistService wishlistService, UsuarioRepositorio usuarioRepositorio) {
        this.wishlistService = wishlistService;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    private Usuario obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new BadCredentialsException("Usuario no autenticado");
        }
        String username = authentication.getName();
        return usuarioRepositorio.findByNombre(username)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado: " + username));
    }

    @PreAuthorize("hasAnyRole('VOLUNTARIO', 'DONANTE')")
    @PostMapping("/agregar/{proyectoId}")
    public void agregarProyecto(@PathVariable Long proyectoId) {
        Usuario usuario = obtenerUsuarioAutenticado();
        wishlistService.agregarProyectoAWishlist(usuario.getIdusuario(), proyectoId);
    }

    @PreAuthorize("hasAnyRole('VOLUNTARIO', 'DONANTE')")
    @DeleteMapping("/quitar/{proyectoId}")
    public void quitarProyecto(@PathVariable Long proyectoId) {
        Usuario usuario = obtenerUsuarioAutenticado();
        wishlistService.quitarProyectoDeWishlist(usuario.getIdusuario(), proyectoId);
    }

    @PreAuthorize("hasAnyRole('VOLUNTARIO', 'DONANTE')")
    @GetMapping
    public List<ProyectoSoloConDatosDTO> obtenerWishlist() {
        Usuario usuario = obtenerUsuarioAutenticado();
        return wishlistService.obtenerWishlist(usuario.getIdusuario());
    }
}