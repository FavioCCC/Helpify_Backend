package com.webcrafters.helpify.controladores;

import com.webcrafters.helpify.DTO.ProyectoSoloConDatosDTO;
import com.webcrafters.helpify.servicios.WishlistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/{usuarioId}/agregar/{proyectoId}")
    public void agregarProyecto(@PathVariable Long usuarioId, @PathVariable Long proyectoId) {
        wishlistService.agregarProyectoAWishlist(usuarioId, proyectoId);
    }

    @DeleteMapping("/{usuarioId}/quitar/{proyectoId}")
    public void quitarProyecto(@PathVariable Long usuarioId, @PathVariable Long proyectoId) {
        wishlistService.quitarProyectoDeWishlist(usuarioId, proyectoId);
    }

    @GetMapping("/{usuarioId}")
    public List<ProyectoSoloConDatosDTO> obtenerWishlist(@PathVariable Long usuarioId) {
        return wishlistService.obtenerWishlist(usuarioId);
    }
}