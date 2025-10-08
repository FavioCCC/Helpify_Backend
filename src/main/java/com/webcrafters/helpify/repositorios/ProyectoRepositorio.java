// src/main/java/com/upc/borradorhelpify/repositorios/ProyectoRepositorio.java
package com.webcrafters.helpify.repositorios;

import com.webcrafters.helpify.DTO.PorcentajeUniversitariosDTO;
import com.webcrafters.helpify.DTO.UniversitariosPorProyectoDTO;
import com.webcrafters.helpify.entidades.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProyectoRepositorio extends JpaRepository<Proyecto, Long> {
    List<Proyecto> findAllByNombreproyectoContainingIgnoreCase(String nombreproyecto);

    List<Proyecto> findAllByMontoobjetivo(double montoobjetivo);

    List<Proyecto> findByFechainicioBetween(LocalDate fechainicio, LocalDate fechafin);

    @Query("SELECT new com.webcrafters.helpify.DTO.UniversitariosPorProyectoDTO(p.nombreproyecto, COUNT(i.universitario)) " +
            "FROM Proyecto p JOIN Inscripcion i ON i.proyecto = p GROUP BY p.nombreproyecto")
    List<UniversitariosPorProyectoDTO> obtenerUniversitariosPorProyecto();

    @Query("SELECT COUNT(u) FROM Universitario u")
    long obtenerTotalUniversitarios();

    @Query("SELECT new com.webcrafters.helpify.DTO.PorcentajeUniversitariosDTO(p.nombreproyecto, " +
            "(COUNT(i.universitario) * 1.0 / :total) * 100) " +
            "FROM Proyecto p JOIN Inscripcion i ON i.proyecto = p GROUP BY p.nombreproyecto")
    List<PorcentajeUniversitariosDTO> obtenerPorcentajeUniversitariosPorProyecto(@Param("total") long total);
}
