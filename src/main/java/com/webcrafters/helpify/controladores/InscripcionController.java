package com.webcrafters.helpify.controladores;

import com.webcrafters.helpify.DTO.InscripcionRespuestaDTO;
import com.webcrafters.helpify.DTO.InscripcionSinUsuarioDTO;
import com.webcrafters.helpify.DTO.ReporteParticipacionDTO;
import com.webcrafters.helpify.servicios.InscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {
    @Autowired
    private InscripcionService inscripcionService;

    @PreAuthorize("hasRole('VOLUNTARIO')")
    @PostMapping("/inscribir")
    public ResponseEntity<InscripcionRespuestaDTO> inscribirEnProyecto(
            @RequestParam Long idUniversitario,
            @RequestParam Long idProyecto) {
        InscripcionRespuestaDTO respuesta = inscripcionService.inscribirEnProyecto(idUniversitario, idProyecto);
        return ResponseEntity.ok(respuesta);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO')")
    @DeleteMapping("/eliminar")
    public ResponseEntity<InscripcionRespuestaDTO> cancelar(
            @RequestParam Long idUniversitario,
            @RequestParam Long idProyecto) {
        InscripcionRespuestaDTO respuesta = inscripcionService.cancelarInscripcion(idUniversitario, idProyecto);
        return ResponseEntity.ok(respuesta);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO')")
    @GetMapping("/inscripciones")
    public List<InscripcionSinUsuarioDTO> listarInscripciones() {
        return inscripcionService.listarTodasLasInscripciones();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/reporte-participacion")
    public List<ReporteParticipacionDTO> reporteParticipacion() {
        return inscripcionService.generarReporteParticipacion();
    }

}