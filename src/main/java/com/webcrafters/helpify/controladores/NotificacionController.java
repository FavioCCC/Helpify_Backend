package com.webcrafters.helpify.controladores;

import com.webcrafters.helpify.DTO.NotificacionDTO;
import com.webcrafters.helpify.DTO.NotificacionSinUsuarioDTO;
import com.webcrafters.helpify.interfaces.INotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200",
        allowCredentials = "true",
        exposedHeaders = "Authorization")
@RequestMapping("/api")
public class NotificacionController {
    @Autowired
    private INotificacionService notificacionService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/notificacion/crear")
    public ResponseEntity<NotificacionDTO> crearNotificacion(
            @Valid @RequestBody NotificacionSinUsuarioDTO notificacionDTO) {

        NotificacionDTO nuevaNotificacion = notificacionService.crearNotificacion(notificacionDTO);
        return new ResponseEntity<>(nuevaNotificacion, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/notificaciones/listartodo")
    public ResponseEntity<List<NotificacionDTO>> listarTodas() {
        List<NotificacionDTO> notificaciones = notificacionService.listarTodas();
        return ResponseEntity.ok(notificaciones);
    }

    @PreAuthorize("hasAnyRole('ADMIN','VOLUNTARIO', 'DONANTE')")
    @DeleteMapping("/notificacion/{id}")
    public ResponseEntity<Void> eliminarNotificacion(@PathVariable Long id) {
        notificacionService.eliminarNotificacion(id);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasAnyRole( 'VOLUNTARIO', 'DONANTE')")
    @GetMapping("/notificaciones/usuario/{idUsuario}")
    public ResponseEntity<List<NotificacionDTO>> listarPorUsuario(@PathVariable Long idUsuario) {
        List<NotificacionDTO> notificaciones = notificacionService.listarPorUsuario(idUsuario);
        return ResponseEntity.ok(notificaciones);
    }

    @PreAuthorize("hasAnyRole('VOLUNTARIO', 'DONANTE')")
    @PutMapping("notificacion/leer/{id}")
    public ResponseEntity<NotificacionDTO> marcarComoLeida(@PathVariable Long id) {
        NotificacionDTO notificacionLeida = notificacionService.marcarComoLeida(id);
        return ResponseEntity.ok(notificacionLeida);
    }
}