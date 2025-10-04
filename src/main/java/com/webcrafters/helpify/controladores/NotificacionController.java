package com.webcrafters.helpify.controladores;



import com.webcrafters.helpify.DTO.NotificacionDTO;
import com.webcrafters.helpify.DTO.NotificacionSinProyectoyUsuarioDTO;
import com.webcrafters.helpify.interfaces.INotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificacion")
public class NotificacionController {
    @Autowired
    private INotificacionService notificacionService;

    // Crear Notificacion
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{usuarioId}/proyecto/{proyectoId}")
    public NotificacionDTO crearNotifificacion(@RequestBody NotificacionDTO notificacionDTO,
                                                  @PathVariable Long usuarioId,
                                                  @PathVariable Long proyectoId) {
        return notificacionService.insertarNotificacion(notificacionDTO, usuarioId, proyectoId);
    }

    // Actualizar Notificacion
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/notificacion/{idUsuario}/proyecto/{idProyecto}")
    public ResponseEntity<NotificacionDTO> actualizarNotificacion(
            @RequestBody NotificacionDTO notificacionDTO,
            @PathVariable Long idUsuario,
            @PathVariable Long idProyecto) {
        return ResponseEntity.ok(notificacionService.actualizarNotificacion(notificacionDTO, idUsuario, idProyecto));
    }


    // Eliminar Notificacion
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{notificacionId}/usuario/{usuarioId}/proyecto/{proyectoId}")
    public void eliminarNotificacionId(@PathVariable Long notificacionId,
                                   @PathVariable Long usuarioId,
                                   @PathVariable Long proyectoId) {
        notificacionService.eliminarNotificacion(notificacionId, usuarioId, proyectoId);
    }

    // Listar Notificacion por proyecto y usuario
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/proyecto/{proyectoId}/usuario/{usuarioId}")
    public List<NotificacionSinProyectoyUsuarioDTO> listarPorProyectoYUsuario(@PathVariable Long proyectoId,
                                                                              @PathVariable Long usuarioId) {
        return notificacionService.listarNotificacionesPorUsuario(proyectoId, usuarioId);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO', 'DONANTE')")
    @PutMapping("/marcar-leida/{idNotificacion}")
    public ResponseEntity<Void> marcarComoLeida(@PathVariable Long idNotificacion) {
        notificacionService.marcarComoLeida(idNotificacion);
        return ResponseEntity.ok().build();
    }
}
