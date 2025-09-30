package com.webcrafters.helpify.repositorios;

import com.webcrafters.helpify.entidades.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepositorio extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByProyectoIdproyectoAndUsuarioIdusuario(Long idProyecto, Long idUsuario);
}
