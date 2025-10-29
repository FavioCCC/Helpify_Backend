package com.webcrafters.helpify.controladores;

import com.webcrafters.helpify.seguridad.entidades.Usuario;
import com.webcrafters.helpify.seguridad.repositorios.UsuarioRepositorio;
import org.springframework.http.ResponseEntity;

import com.webcrafters.helpify.DTO.ComentarioDTO;
import com.webcrafters.helpify.DTO.ComentarioSinProyectoyUsuarioDTO;
import com.webcrafters.helpify.interfaces.IComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ComentarioController {
    @Autowired
    private IComentarioService comentarioService;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    private Usuario obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new BadCredentialsException("Usuario no autenticado");
        }
        String username = authentication.getName();
        return usuarioRepositorio.findByNombre(username)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado: " + username));
    }

    // Crear comentario
    @PreAuthorize("hasAnyRole('VOLUNTARIO', 'DONANTE')")
    @PostMapping("/comentario/proyecto/{proyectoId}")
    public ComentarioDTO insertarComentario(@RequestBody ComentarioDTO comentarioDTO,
                                            @PathVariable Long proyectoId) {
        Usuario usuario = obtenerUsuarioAutenticado();
        return comentarioService.insertarComentario(comentarioDTO, usuario.getIdusuario(), proyectoId);
    }

    // Actualizar comentario
    @PreAuthorize("hasAnyRole('VOLUNTARIO', 'DONANTE')")
    @PutMapping("/comentario/proyecto/{proyectoId}")
    public ResponseEntity<ComentarioDTO> actualizarComentario(
            @RequestBody ComentarioDTO comentarioDTO,
            @PathVariable Long proyectoId) {
        if (comentarioDTO.getIdcomentario() == null) {
            throw new IllegalArgumentException("El id del comentario debe estar presente en el JSON para actualizar");
        }
        Usuario usuario = obtenerUsuarioAutenticado();
        return ResponseEntity.ok(comentarioService.actualizarComentario(comentarioDTO, usuario.getIdusuario(), proyectoId));
    }


    // Eliminar comentario
    @PreAuthorize("hasAnyRole('VOLUNTARIO', 'DONANTE')")
    @DeleteMapping("/comentario/proyecto/{proyectoId}")
    public void eliminarComentario(@RequestBody ComentarioDTO comentarioDTO,
                                   @PathVariable Long proyectoId) {
        if (comentarioDTO.getIdcomentario() == null) {
            throw new IllegalArgumentException("El id del comentario debe estar presente en el JSON para eliminar");
        }
        Usuario usuario = obtenerUsuarioAutenticado();
        comentarioService.eliminarComentario(comentarioDTO.getIdcomentario(), usuario.getIdusuario(), proyectoId);
    }

    // Listar comentarios por proyecto y usuario
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO', 'DONANTE')")
    @GetMapping("/comentario/proyecto/{proyectoId}")
    public List<ComentarioSinProyectoyUsuarioDTO> listarPorProyectoYUsuario(@PathVariable Long proyectoId) {
        Usuario usuario = obtenerUsuarioAutenticado();
        return comentarioService.listarComentarioPorProyectoyUsuario(proyectoId, usuario.getIdusuario());
    }



    // Buscar comentarios por calificación
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO', 'DONANTE')")
    @GetMapping("/comentario/calificacion/{estrella}")
    public List<ComentarioSinProyectoyUsuarioDTO> buscarPorCalificacion(@PathVariable double estrella) {
        return comentarioService.buscarPorCalificacion(estrella);
    }
}
