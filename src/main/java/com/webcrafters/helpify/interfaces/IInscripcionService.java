package com.webcrafters.helpify.interfaces;

import com.webcrafters.helpify.DTO.InscripcionRespuestaDTO;
import com.webcrafters.helpify.DTO.InscripcionSinUsuarioDTO;

import java.util.List;

public interface IInscripcionService {
    public InscripcionRespuestaDTO inscribirEnProyecto(Long idUniversitario, Long idProyecto);
    public InscripcionRespuestaDTO cancelarInscripcion(Long idUniversitario, Long idProyecto);
    public List<String> generarReporteInscripciones();
    public List<InscripcionSinUsuarioDTO> listarTodasLasInscripciones();
}
