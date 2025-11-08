package com.webcrafters.helpify.interfaces;

import com.webcrafters.helpify.DTO.NotificacionDTO;
import com.webcrafters.helpify.DTO.NotificacionSinUsuarioDTO;

import java.util.List;

public interface INotificacionService {
    NotificacionDTO crearNotificacion(NotificacionSinUsuarioDTO notificacionDTO);

    List<NotificacionDTO> listarTodas();

    List<NotificacionDTO> listarPorUsuario(Long idUsuario);

    NotificacionDTO marcarComoLeida(Long idNotificacion);
    void eliminarNotificacion(Long idNotificacion);
}