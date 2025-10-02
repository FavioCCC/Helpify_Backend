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
import org.springframework.stereotype.Service;

import java.util.List;
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
    public NotificacionDTO insertarNotificacion(NotificacionDTO notificacionDTO, Long idUsuario, Long idProyecto) {
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
        return modelMapper.map(guardada, NotificacionDTO.class);
    }

    @Override
    public NotificacionDTO actualizarNotificacion(NotificacionDTO notificacionDTO, Long idUsuario, Long idProyecto) {
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
                    return modelMapper.map(actualizado, NotificacionDTO.class);
                })
                .orElseThrow(() -> new RuntimeException(
                        "Notificación con ID " + notificacionDTO.getIdnotificacion() + " no encontrada"));
    }

    @Override
    public void eliminarNotificacion(Long id, Long idUsuario, Long idProyecto) {
        Notificacion notificacion = notificacionRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con ID: " + id));

        if (!notificacion.getUsuario().getIdusuario().equals(idUsuario) ||
                !notificacion.getProyecto().getIdproyecto().equals(idProyecto)) {
            throw new RuntimeException("La notificación no corresponde al usuario/proyecto indicado");
        }

        notificacionRepositorio.deleteById(id);
    }

    @Override
    public List<NotificacionSinProyectoyUsuarioDTO> listarNotificacionesPorUsuario(Long idProyecto, Long idUsuario) {
        return notificacionRepositorio.findByProyectoIdproyectoAndUsuarioIdusuario(idProyecto, idUsuario).stream()
                .map(c -> modelMapper.map(c, NotificacionSinProyectoyUsuarioDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void marcarComoLeida(Long idNotificacion) {
        Notificacion notificacion = notificacionRepositorio.findById(idNotificacion)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con ID: " + idNotificacion));
        notificacion.setLeido(true);
        notificacionRepositorio.save(notificacion);
    }
}