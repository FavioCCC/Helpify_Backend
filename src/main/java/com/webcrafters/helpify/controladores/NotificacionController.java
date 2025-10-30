package com.webcrafters.helpify.controladores;



import com.webcrafters.helpify.DTO.NotificacionDTO;
import com.webcrafters.helpify.DTO.NotificacionSinProyectoyUsuarioDTO;
import com.webcrafters.helpify.DTO.RegistroNotificacionRespuestaDTO;
import com.webcrafters.helpify.interfaces.INotificacionService;
import com.webcrafters.helpify.seguridad.entidades.Usuario;
import com.webcrafters.helpify.seguridad.repositorios.UsuarioRepositorio;
import com.webcrafters.helpify.servicios.NotificacionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class NotificacionController {
    private final NotificacionService notificacionService;
    private final UsuarioRepositorio usuarioRepositorio;

    public NotificacionController(NotificacionService notificacionService, UsuarioRepositorio usuarioRepositorio) {
        this.notificacionService = notificacionService;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    private String obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new BadCredentialsException("Usuario no autenticado");
        }
        return authentication.getName();
    }

    // Crear Notificacion
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/notificacion/usuario/{usuarioId}/proyecto/{proyectoId}")
    public ResponseEntity<RegistroNotificacionRespuestaDTO> crearNotifificacion(@RequestBody NotificacionDTO notificacionDTO,
                                                                                @PathVariable Long usuarioId,
                                                                                @PathVariable Long proyectoId) {
        NotificacionSinProyectoyUsuarioDTO creado = notificacionService.insertarNotificacion(notificacionDTO, usuarioId, proyectoId);
        RegistroNotificacionRespuestaDTO respuesta = new RegistroNotificacionRespuestaDTO(
                "Notificación registrada correctamente",
                creado
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    // Actualizar Notificacion
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/notificacion/usuario/{idUsuario}/proyecto/{idProyecto}")
    public ResponseEntity<RegistroNotificacionRespuestaDTO> actualizarNotificacion(
            @RequestBody NotificacionDTO notificacionDTO,
            @PathVariable Long idUsuario,
            @PathVariable Long idProyecto) {
        NotificacionSinProyectoyUsuarioDTO actualizado = notificacionService.actualizarNotificacion(notificacionDTO, idUsuario, idProyecto);
        RegistroNotificacionRespuestaDTO respuesta = new RegistroNotificacionRespuestaDTO(
                "Notificación actualizada correctamente",
                actualizado
        );
        return ResponseEntity.ok(respuesta);
    }


    // Eliminar Notificacion
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/notificacion/{notificacionId}/usuario/{usuarioId}/proyecto/{proyectoId}")
    public ResponseEntity<RegistroNotificacionRespuestaDTO> eliminarNotificacionId(@PathVariable Long notificacionId,
                                                                                   @PathVariable Long usuarioId,
                                                                                   @PathVariable Long proyectoId) {
        NotificacionSinProyectoyUsuarioDTO eliminado = notificacionService.eliminarNotificacion(notificacionId, usuarioId, proyectoId);
        RegistroNotificacionRespuestaDTO respuesta = new RegistroNotificacionRespuestaDTO(
                "La notificación ha sido eliminada correctamente.",
                eliminado
        );
        return ResponseEntity.ok(respuesta);
    }

    // Listar Notificacion por proyecto y usuario
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO')")
    @GetMapping("/notificacion/proyecto/{proyectoId}")
    public ResponseEntity<RegistroNotificacionRespuestaDTO> listarPorProyectoYUsuario(@PathVariable Long proyectoId) {
        String username = obtenerUsuarioAutenticado();

        Usuario usuario = usuarioRepositorio.findByNombre(username)
                .orElseThrow(() -> new BadCredentialsException("Usuario no encontrado: " + username));

        List<NotificacionSinProyectoyUsuarioDTO> lista = notificacionService.listarNotificacionesPorUsuario(proyectoId, usuario.getIdusuario());
        String msg = "Listado de notificaciones obtenido correctamente. Total: " + lista.size();
        RegistroNotificacionRespuestaDTO respuesta = new RegistroNotificacionRespuestaDTO(
                msg,
                lista.isEmpty() ? null : lista.get(0)
        );
        return ResponseEntity.ok(respuesta);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/notificacion/marcar-leida/{idNotificacion}")
    public ResponseEntity<RegistroNotificacionRespuestaDTO> marcarComoLeida(@PathVariable Long idNotificacion) {
        NotificacionSinProyectoyUsuarioDTO actualizado = notificacionService.marcarComoLeida(idNotificacion);
        RegistroNotificacionRespuestaDTO respuesta = new RegistroNotificacionRespuestaDTO(
                "Notificación marcada como leída.",
                actualizado
        );
        return ResponseEntity.ok(respuesta);
    }
}
