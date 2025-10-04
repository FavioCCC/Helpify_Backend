package com.webcrafters.helpify.controladores;

import com.webcrafters.helpify.DTO.ProyectoSoloConDatosDTO;
import com.webcrafters.helpify.servicios.WishlistService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;


    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PreAuthorize("hasAnyRole('VOLUNTARIO', 'DONANTE')")
    @PostMapping("/{usuarioId}/agregar/{proyectoId}")
    public void agregarProyecto(@PathVariable Long usuarioId, @PathVariable Long proyectoId) {
        wishlistService.agregarProyectoAWishlist(usuarioId, proyectoId);
    }

    @PreAuthorize("hasAnyRole('VOLUNTARIO', 'DONANTE')")
    @DeleteMapping("/{usuarioId}/quitar/{proyectoId}")
    public void quitarProyecto(@PathVariable Long usuarioId, @PathVariable Long proyectoId) {
        wishlistService.quitarProyectoDeWishlist(usuarioId, proyectoId);
    }

    @PreAuthorize("hasAnyRole('VOLUNTARIO', 'DONANTE')")
    @GetMapping("/{usuarioId}")
    public List<ProyectoSoloConDatosDTO> obtenerWishlist(@PathVariable Long usuarioId) {
        return wishlistService.obtenerWishlist(usuarioId);
    }
}