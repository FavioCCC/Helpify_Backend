package com.webcrafters.helpify.repositorios;

import com.webcrafters.helpify.entidades.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioRepositorio extends JpaRepository<Comentario, Long> {
    // Buscar comentarios con estrellas mayores o iguales
    List<Comentario>findByProyectoIdproyectoAndUsuarioIdusuario(Long idProyecto, Long idUsuario);

    List<Comentario> findByEstrella(double estrella);

    // Buscar comentarios de un proyecto específico
    List<Comentario> findByProyectoIdproyecto(Long idproyecto);

    // Buscar comentarios de un usuario específico
    List<Comentario> findByUsuarioIdusuario(Long idusuario);
}
