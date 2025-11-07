package com.webcrafters.helpify.interfaces;

import com.webcrafters.helpify.DTO.InscripcionRespuestaDTO;
import com.webcrafters.helpify.DTO.InscripcionSinUsuarioDTO;
import com.webcrafters.helpify.DTO.ReporteParticipacionDTO;

import java.util.List;

public interface IInscripcionService {
    public InscripcionRespuestaDTO inscribirEnProyecto(Long proyectoId, String username);
    public InscripcionRespuestaDTO cancelarInscripcion(Long idUniversitario, Long idProyecto);
    public List<InscripcionSinUsuarioDTO> listarTodasLasInscripciones();
    public List<ReporteParticipacionDTO> generarReporteParticipacion();
}