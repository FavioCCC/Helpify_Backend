package com.webcrafters.helpify.interfaces;

import com.webcrafters.helpify.DTO.NotificacionDTO;
import com.webcrafters.helpify.DTO.NotificacionSinProyectoyUsuarioDTO;

import java.util.List;

public interface INotificacionService {
    public NotificacionDTO insertarNotificacion(NotificacionDTO notificacionDTO, Long idUsuario, Long idProyecto);
    public NotificacionDTO actualizarNotificacion(NotificacionDTO notificacionDTO, Long idUsuario, Long idProyecto);
    public void eliminarNotificacion(Long id, Long idUsuario, Long idProyecto);
    public List<NotificacionSinProyectoyUsuarioDTO> listarNotificacionesPorUsuario(Long idProyecto,Long idUsuario);
    public void marcarComoLeida(Long idNotificacion);
}
