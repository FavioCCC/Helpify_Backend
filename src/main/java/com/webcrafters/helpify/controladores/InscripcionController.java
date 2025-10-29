package com.webcrafters.helpify.controladores;

import com.webcrafters.helpify.DTO.InscripcionRespuestaDTO;
import com.webcrafters.helpify.DTO.InscripcionSinUsuarioDTO;
import com.webcrafters.helpify.DTO.ReporteParticipacionDTO;
import com.webcrafters.helpify.servicios.InscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class InscripcionController {
    @Autowired
    private InscripcionService inscripcionService;

    private String obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new BadCredentialsException("Usuario no autenticado");
        }
        return authentication.getName();
    }

    @PostMapping("/inscripcion/{proyectoId}")
    public ResponseEntity<?> inscribirEnProyecto(@PathVariable Long proyectoId) {
        String username = obtenerUsuarioAutenticado();
        inscripcionService.inscribirEnProyecto(proyectoId, username);
        return ResponseEntity.ok(Map.of(
                "message", "Inscripción registrada con éxito",
                "proyectoId", proyectoId
        ));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO')")
    @DeleteMapping("/inscripcion/eliminar")
    public ResponseEntity<InscripcionRespuestaDTO> cancelar(
            @RequestParam Long idUniversitario,
            @RequestParam Long idProyecto) {
        InscripcionRespuestaDTO respuesta = inscripcionService.cancelarInscripcion(idUniversitario, idProyecto);
        return ResponseEntity.ok(respuesta);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO')")
    @GetMapping("/inscripcion/inscripciones")
    public List<InscripcionSinUsuarioDTO> listarInscripciones() {
        return inscripcionService.listarTodasLasInscripciones();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/inscripcion/reporte-participacion")
    public List<ReporteParticipacionDTO> reporteParticipacion() {
        return inscripcionService.generarReporteParticipacion();
    }

}