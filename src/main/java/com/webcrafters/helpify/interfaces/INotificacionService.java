package com.webcrafters.helpify.interfaces;

import com.webcrafters.helpify.DTO.NotificacionDTO;
import com.webcrafters.helpify.DTO.NotificacionSinProyectoyUsuarioDTO;

import java.util.List;

public interface INotificacionService {
    public NotificacionSinProyectoyUsuarioDTO insertarNotificacion(NotificacionDTO notificacionDTO, Long idUsuario, Long idProyecto);
    public NotificacionSinProyectoyUsuarioDTO actualizarNotificacion(NotificacionDTO notificacionDTO, Long idUsuario, Long idProyecto);
    public NotificacionSinProyectoyUsuarioDTO eliminarNotificacion(Long id, Long idUsuario, Long idProyecto);
    public List<NotificacionSinProyectoyUsuarioDTO> listarNotificacionesPorUsuario(Long idProyecto,Long idUsuario);
    public NotificacionSinProyectoyUsuarioDTO marcarComoLeida(Long idNotificacion);
}
