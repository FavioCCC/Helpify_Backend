package com.webcrafters.helpify.repositorios;

import com.webcrafters.helpify.entidades.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepositorio extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByUsuarioIdusuarioOrderByFechaenvioDesc(Long idUsuario);

}