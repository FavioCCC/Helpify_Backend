package com.webcrafters.helpify.servicios;

import com.webcrafters.helpify.DTO.NotificacionDTO;
import com.webcrafters.helpify.DTO.NotificacionSinProyectoyUsuarioDTO;
import com.webcrafters.helpify.entidades.Notificacion;
import com.webcrafters.helpify.entidades.Proyecto;
import com.webcrafters.helpify.seguridad.entidades.Usuario;
import com.webcrafters.helpify.interfaces.INotificacionService;
import com.webcrafters.helpify.repositorios.NotificacionRepositorio;
import com.webcrafters.helpify.repositorios.ProyectoRepositorio;
import com.webcrafters.helpify.seguridad.repositorios.UsuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class NotificacionService implements INotificacionService {

    @Autowired
    private NotificacionRepositorio notificacionRepositorio;
    @Autowired
    private ProyectoRepositorio proyectoRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public NotificacionSinProyectoyUsuarioDTO insertarNotificacion(NotificacionDTO notificacionDTO, Long idUsuario, Long idProyecto) {
        Usuario usuario = usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));
        Proyecto proyecto = proyectoRepositorio.findById(idProyecto)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + idProyecto));

        Notificacion notificacionEntidad = modelMapper.map(notificacionDTO, Notificacion.class);
        notificacionEntidad.setUsuario(usuario);
        notificacionEntidad.setProyecto(proyecto);
        notificacionEntidad.setFechaEnvio(java.time.LocalDate.now());
        notificacionEntidad.setLeido(false);

        Notificacion guardada = notificacionRepositorio.save(notificacionEntidad);
        return modelMapper.map(guardada, NotificacionSinProyectoyUsuarioDTO.class);
    }

    @Override
    public NotificacionSinProyectoyUsuarioDTO actualizarNotificacion(NotificacionDTO notificacionDTO, Long idUsuario, Long idProyecto) {
        return notificacionRepositorio.findById(notificacionDTO.getIdnotificacion())
                .map(existing -> {
                    Usuario usuario = usuarioRepositorio.findById(idUsuario)
                            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));
                    Proyecto proyecto = proyectoRepositorio.findById(idProyecto)
                            .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + idProyecto));

                    existing.setMensaje(notificacionDTO.getMensaje());
                    existing.setTipo(notificacionDTO.getTipo());
                    existing.setLeido(notificacionDTO.getLeido());
                    existing.setUsuario(usuario);
                    existing.setProyecto(proyecto);

                    Notificacion actualizado = notificacionRepositorio.save(existing);
                    return modelMapper.map(actualizado, NotificacionSinProyectoyUsuarioDTO.class);
                })
                .orElseThrow(() -> new RuntimeException(
                        "Notificación con ID " + notificacionDTO.getIdnotificacion() + " no encontrada"));
    }

    @Override
    public NotificacionSinProyectoyUsuarioDTO eliminarNotificacion(Long id, Long idUsuario, Long idProyecto) {
        Notificacion notificacion = notificacionRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con ID: " + id));

        if (!notificacion.getUsuario().getIdusuario().equals(idUsuario) ||
                !notificacion.getProyecto().getIdproyecto().equals(idProyecto)) {
            throw new RuntimeException("La notificación no corresponde al usuario/proyecto indicado");
        }

        // Mapear antes de eliminar para devolver representación
        NotificacionSinProyectoyUsuarioDTO eliminadoDto = modelMapper.map(notificacion, NotificacionSinProyectoyUsuarioDTO.class);
        notificacionRepositorio.deleteById(id);
        return eliminadoDto;
    }

    @Override
    public List<NotificacionSinProyectoyUsuarioDTO> listarNotificacionesPorUsuario(Long idProyecto, Long idUsuario) {
        // Verificar autenticación
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            // Simula HTTP 401
            throw new BadCredentialsException("Autenticación requerida");
        }

        // Validar existencia del usuario
        Usuario usuario = usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));

        // Validar existencia del proyecto
        Proyecto proyecto = proyectoRepositorio.findById(idProyecto)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + idProyecto));

        try {
            // Buscar notificaciones
            List<Notificacion> notificaciones = notificacionRepositorio
                    .findByProyectoIdproyectoAndUsuarioIdusuario(idProyecto, idUsuario);

            if (notificaciones.isEmpty()) {
                // Caso 1: Sin notificaciones registradas
                throw new NoSuchElementException("No tienes notificaciones por el momento");
            }

            // Mapear resultados
            return notificaciones.stream()
                    .map(n -> modelMapper.map(n, NotificacionSinProyectoyUsuarioDTO.class))
                    .collect(Collectors.toList());

        } catch (DataAccessException ex) {
            // Caso 2: Falla en la carga (error de conexión, DB caída, etc.)
            throw new RuntimeException("No se pudieron cargar las notificaciones, intenta nuevamente más tarde.");
        }
    }

    @Override
    public NotificacionSinProyectoyUsuarioDTO marcarComoLeida(Long idNotificacion) {
        Notificacion notificacion = notificacionRepositorio.findById(idNotificacion)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con ID: " + idNotificacion));
        notificacion.setLeido(true);
        Notificacion actualizado = notificacionRepositorio.save(notificacion);
        return modelMapper.map(actualizado, NotificacionSinProyectoyUsuarioDTO.class);
    }
}