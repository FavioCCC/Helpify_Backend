package com.webcrafters.helpify.servicios;

import com.webcrafters.helpify.DTO.ProyectoSoloConDatosDTO;
import com.webcrafters.helpify.entidades.*;
import com.webcrafters.helpify.repositorios.*;
import com.webcrafters.helpify.seguridad.entidades.Usuario;
import com.webcrafters.helpify.seguridad.repositorios.UsuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistService {
    private final WishlistRepositorio wishlistRepositorio;
    private final ProyectoRepositorio proyectoRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    public WishlistService(WishlistRepositorio wishlistRepositorio, ProyectoRepositorio proyectoRepositorio, UsuarioRepositorio usuarioRepositorio) {
        this.wishlistRepositorio = wishlistRepositorio;
        this.proyectoRepositorio = proyectoRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public Wishlist agregarProyectoAWishlist(Long usuarioId, Long proyectoId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId).orElseThrow();
        Proyecto proyecto = proyectoRepositorio.findById(proyectoId).orElseThrow();
        Wishlist wishlist = wishlistRepositorio.findByUsuario(usuario).orElse(new Wishlist());
        wishlist.setUsuario(usuario);
        if (wishlist.getProyectos() == null) wishlist.setProyectos(new ArrayList<>());
        if (!wishlist.getProyectos().contains(proyecto)) wishlist.getProyectos().add(proyecto);
        return wishlistRepositorio.save(wishlist);
    }

    public void quitarProyectoDeWishlist(Long usuarioId, Long proyectoId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Proyecto proyecto = proyectoRepositorio.findById(proyectoId).orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        Wishlist wishlist = wishlistRepositorio.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Wishlist no encontrada para el usuario"));
        boolean removed = wishlist.getProyectos().removeIf(p -> p.getIdproyecto().equals(proyecto.getIdproyecto()));
        if (!removed) {
            throw new RuntimeException("El proyecto no está en la wishlist");
        }
        wishlistRepositorio.save(wishlist);
    }

    public List<ProyectoSoloConDatosDTO> obtenerWishlist(Long usuarioId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId).orElseThrow();
        List<Proyecto> proyectos = wishlistRepositorio.findByUsuario(usuario)
                .map(Wishlist::getProyectos)
                .orElse(Collections.emptyList());
        return proyectos.stream()
                .map(proyecto -> modelMapper.map(proyecto, ProyectoSoloConDatosDTO.class))
                .collect(Collectors.toList());
    }
}