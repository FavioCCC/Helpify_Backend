package com.webcrafters.helpify.servicios;

import com.webcrafters.helpify.DTO.NotificacionDTO;
import com.webcrafters.helpify.DTO.NotificacionSinUsuarioDTO;
import com.webcrafters.helpify.entidades.Notificacion;
import com.webcrafters.helpify.seguridad.entidades.Usuario;
import com.webcrafters.helpify.interfaces.INotificacionService;
import com.webcrafters.helpify.repositorios.NotificacionRepositorio;
import com.webcrafters.helpify.seguridad.repositorios.UsuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionService implements INotificacionService {

    @Autowired
    private NotificacionRepositorio notificacionRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ModelMapper modelMapper;

    private NotificacionDTO convertirADTO(Notificacion notificacion) {
        return modelMapper.map(notificacion, NotificacionDTO.class);
    }


    @Override
    public NotificacionDTO crearNotificacion(NotificacionSinUsuarioDTO notificacionDTO) {

        Usuario usuario = usuarioRepositorio.findById(notificacionDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario destinatario no encontrado con ID: " + notificacionDTO.getUsuarioId()));


        Notificacion notificacionEntidad = modelMapper.map(notificacionDTO, Notificacion.class);

        notificacionEntidad.setUsuario(usuario);
        notificacionEntidad.setFechaenvio(LocalDateTime.now());
        notificacionEntidad.setLeido(false);

        Notificacion guardada = notificacionRepositorio.save(notificacionEntidad);
        return convertirADTO(guardada);
    }

    @Override
    public List<NotificacionDTO> listarTodas() {
        return notificacionRepositorio.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /** 3. Listar notificaciones por usuario (Uso exclusivo del Usuario) **/
    @Override
    public List<NotificacionDTO> listarPorUsuario(Long idUsuario) {

        List<Notificacion> notificaciones = notificacionRepositorio
                .findByUsuarioIdusuarioOrderByFechaenvioDesc(idUsuario);

        if (notificaciones.isEmpty()) {
            throw new RuntimeException("No se encontraron notificaciones para el usuario con ID: " + idUsuario);
        }

        return notificaciones.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /** 4. Marcar como leído (Método de utilidad) **/
    @Override
    public NotificacionDTO marcarComoLeida(Long idNotificacion) {
        Notificacion notificacion = notificacionRepositorio.findById(idNotificacion)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con ID: " + idNotificacion));

        notificacion.setLeido(true);
        Notificacion actualizado = notificacionRepositorio.save(notificacion);
        return convertirADTO(actualizado);
    }

    @Override
    public void eliminarNotificacion(Long idNotificacion) {
        Notificacion notificacion = notificacionRepositorio.findById(idNotificacion)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con ID: " + idNotificacion));

        notificacionRepositorio.delete(notificacion);

    }

}