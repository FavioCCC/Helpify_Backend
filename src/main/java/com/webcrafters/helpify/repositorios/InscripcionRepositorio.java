package com.webcrafters.helpify.repositorios;

import com.webcrafters.helpify.entidades.Inscripcion;
import com.webcrafters.helpify.entidades.Proyecto;
import com.webcrafters.helpify.entidades.Universitario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface InscripcionRepositorio extends JpaRepository<Inscripcion, Long> {
    public long countByProyecto(Proyecto proyecto);
    List<Inscripcion> findByUniversitarioAndProyecto(Universitario universitario, Proyecto proyecto);
    boolean existsByUniversitarioAndProyecto(Universitario universitario, Proyecto proyecto);
    @Query("SELECT COUNT(i) > 0 FROM Inscripcion i " +
            "WHERE i.universitario.iduniversitario = :universitarioId " +
            "AND :fechaActual <= i.proyecto.fechafin " +
            "AND i.proyecto.fechainicio <= :fechaActual")
    boolean existsActiveProjectInscription(
            @Param("universitarioId") Long universitarioId,
            @Param("fechaActual") LocalDate fechaActual
    );

}
