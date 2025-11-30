package com.webcrafters.helpify.controladores;

import com.webcrafters.helpify.seguridad.entidades.Usuario;
import com.webcrafters.helpify.seguridad.repositorios.UsuarioRepositorio;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200",
        allowCredentials = "true",
        exposedHeaders = "Authorization")
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

        String principal = authentication.getName();

        return usuarioRepositorio.findByNombre(principal)
                .or(() -> usuarioRepositorio.findByCorreo(principal))
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado: " + principal));
    }


    // Crear comentario
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO', 'DONANTE')")
    @PostMapping("/comentario")
    public ResponseEntity<ComentarioDTO> insertarComentario(@RequestBody ComentarioDTO comentarioDTO) {
        Usuario usuario = obtenerUsuarioAutenticado();
        ComentarioDTO creado = comentarioService.insertarComentario(comentarioDTO, usuario.getIdusuario());
        return ResponseEntity.ok(creado);
    }

    // Actualizar comentario
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO', 'DONANTE')")
    @PutMapping("/comentarionuevo")
    public ResponseEntity<ComentarioDTO> actualizarComentario(@RequestBody ComentarioDTO comentarioDTO) {
        if (comentarioDTO.getIdcomentario() == null) {
            throw new IllegalArgumentException("El id del comentario debe estar presente en el JSON para actualizar");
        }
        Usuario usuario = obtenerUsuarioAutenticado();
        ComentarioDTO actualizado = comentarioService.actualizarComentario(comentarioDTO, usuario.getIdusuario());
        return ResponseEntity.ok(actualizado);
    }


    // Eliminar comentario
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO', 'DONANTE')")
    @DeleteMapping("/comentario/{id}")
    public ResponseEntity<Void> eliminarComentario(@PathVariable Long id) {
        Usuario usuario = obtenerUsuarioAutenticado();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean esAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        comentarioService.eliminarComentario(id, usuario.getIdusuario(), esAdmin);
        return ResponseEntity.noContent().build();
    }

    // Listar comentarios por usuario
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO', 'DONANTE')")
    @GetMapping("/comentario/usuario")
    public ResponseEntity<List<ComentarioSinProyectoyUsuarioDTO>> listarPorUsuario() {
        Usuario usuario = obtenerUsuarioAutenticado();
        List<ComentarioSinProyectoyUsuarioDTO> lista = comentarioService.listarComentarioPorUsuario(usuario.getIdusuario());
        return ResponseEntity.ok(lista);
    }

    // Buscar comentarios por calificación
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO', 'DONANTE')")
    @GetMapping("/comentario/calificacion/{estrella}")
    public List<ComentarioSinProyectoyUsuarioDTO> buscarPorCalificacion(@PathVariable double estrella) {
        return comentarioService.buscarPorCalificacion(estrella);
    }

    @GetMapping("/comentarios")
    public ResponseEntity<List<ComentarioSinProyectoyUsuarioDTO>> listarTodos() {
        List<ComentarioSinProyectoyUsuarioDTO> lista = comentarioService.listarTodosComentarios();
        return ResponseEntity.ok(lista);
    }
}
