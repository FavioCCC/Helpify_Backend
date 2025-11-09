package com.webcrafters.helpify.servicios;

import com.webcrafters.helpify.DTO.InscripcionRespuestaDTO;
import com.webcrafters.helpify.DTO.InscripcionSinUsuarioDTO;
import com.webcrafters.helpify.DTO.ReporteParticipacionDTO;
import com.webcrafters.helpify.entidades.Inscripcion;
import com.webcrafters.helpify.entidades.Proyecto;
import com.webcrafters.helpify.entidades.Universitario;
import com.webcrafters.helpify.interfaces.IInscripcionService;
import com.webcrafters.helpify.repositorios.InscripcionRepositorio;
import com.webcrafters.helpify.repositorios.ProyectoRepositorio;
import com.webcrafters.helpify.repositorios.UniversitarioRepositorio;
import com.webcrafters.helpify.seguridad.entidades.Usuario;
import com.webcrafters.helpify.seguridad.repositorios.UsuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InscripcionService implements IInscripcionService {
    @Autowired
    private InscripcionRepositorio inscripcionRepositorio;
    @Autowired
    private ProyectoRepositorio proyectoRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private UniversitarioRepositorio universitarioRepositorio;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public InscripcionRespuestaDTO inscribirEnProyecto(Long proyectoId, String username) {
        Usuario usuario = usuarioRepositorio.findByNombre(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Usuario no autenticado"));

        Universitario universitario = universitarioRepositorio.findByUsuarioIdusuario(usuario.getIdusuario())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "Debes registrarte como Universitario para inscribirte"));

        Proyecto proyecto = proyectoRepositorio.findById(proyectoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Proyecto no encontrado: " + proyectoId));

        Integer cupoRestante = proyecto.getCupoRestante();
        if (cupoRestante == null || cupoRestante <= 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No hay cupos disponibles en este proyecto");
        }

        boolean existe = inscripcionRepositorio
                .existsByUniversitario_IduniversitarioAndProyecto_Idproyecto(
                        universitario.getIduniversitario(),
                        proyecto.getIdproyecto()
                );

        if (existe) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Ya estás inscrito en este proyecto");
        }

        proyecto.setCupoRestante(cupoRestante - 1);
        proyectoRepositorio.save(proyecto);

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setUniversitario(universitario);
        inscripcion.setProyecto(proyecto);
        inscripcion.setFecharegistro(LocalDateTime.now());
        inscripcionRepositorio.save(inscripcion);

        return new InscripcionRespuestaDTO(true, "Inscripción registrada con éxito");
    }


    public InscripcionRespuestaDTO cancelarInscripcion(Long idUniversitario, Long idProyecto) {
        Universitario universitario = universitarioRepositorio.findById(idUniversitario).get();
        Proyecto proyecto = proyectoRepositorio.findById(idProyecto).get();
        List<Inscripcion> inscripciones = inscripcionRepositorio.findByUniversitarioAndProyecto(universitario, proyecto);

        if (inscripciones.isEmpty()) {
            return new InscripcionRespuestaDTO(false, "No tienes inscripción en este proyecto");
        }
        if (inscripciones.size() > 1) {
            return new InscripcionRespuestaDTO(false, "Error: Inscripciones duplicadas para este universitario y proyecto");
        }
        inscripcionRepositorio.delete(inscripciones.get(0));

        Integer restante = proyecto.getCupoRestante();
        proyecto.setCupoRestante((restante == null ? 0 : restante) + 1);
        proyectoRepositorio.save(proyecto);

        return new InscripcionRespuestaDTO(true, "Inscripción eliminada correctamente");
    }



    public List<InscripcionSinUsuarioDTO> listarTodasLasInscripciones() {
        return inscripcionRepositorio.findAll().stream()
                .map(inscripcion -> modelMapper.map(inscripcion, InscripcionSinUsuarioDTO.class))
                .toList();
    }

    public List<ReporteParticipacionDTO> generarReporteParticipacion() {
        return proyectoRepositorio.findAll().stream().map(proyecto -> {
            ReporteParticipacionDTO dto = new ReporteParticipacionDTO();
            dto.setNombreproyecto(proyecto.getNombreproyecto());
            dto.setDescripcion(proyecto.getDescripcion());
            dto.setMontoobjetivo(proyecto.getMontoobjetivo());
            dto.setMontorecaudado(proyecto.getMontorecaudado());
            dto.setFechainicio(proyecto.getFechainicio());
            dto.setFechafin(proyecto.getFechafin());
            dto.setNombreorganizacion(proyecto.getNombreorganizacion());
            dto.setEscuelabeneficiada(proyecto.getEscuelabeneficiada());
            dto.setCupoMaximo(proyecto.getCupoMaximo());
            dto.setCupoRestante(proyecto.getCupoRestante());
            List<String> universitarios = inscripcionRepositorio.findAll().stream()
                    .filter(i -> i.getProyecto().getIdproyecto().equals(proyecto.getIdproyecto()))
                    .map(i -> i.getUniversitario().getCodigoestudiante())
                    .toList();
            dto.setUniversitarios(universitarios);
            return dto;
        }).toList();
    }

    /*
    public List<ReporteParticipacionDTO> generarReporteParticipacion() {
        return proyectoRepositorio.findAll().stream().map(proyecto -> {
            ReporteParticipacionDTO dto = modelMapper.map(proyecto, ReporteParticipacionDTO.class);

            List<String> universitarios = inscripcionRepositorio.findByProyecto(proyecto).stream()
                    .map(Inscripcion::getUniversitario)
                    .map(Universitario::getCodigoestudiante)
                    .toList();

            dto.setUniversitarios(universitarios);
            return dto;
        }).toList();
    }
     */

}
