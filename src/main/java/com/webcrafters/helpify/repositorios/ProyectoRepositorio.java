// src/main/java/com/upc/borradorhelpify/repositorios/ProyectoRepositorio.java
package com.webcrafters.helpify.repositorios;

import com.webcrafters.helpify.entidades.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProyectoRepositorio extends JpaRepository<Proyecto, Long> {
    List<Proyecto> findAllByNombreproyectoContainingIgnoreCase(String nombreproyecto);
    List<Proyecto> findAllByMontoobjetivo(double montoobjetivo);
    List<Proyecto> findByFechainicioBetween(LocalDate fechainicio, LocalDate fechafin);
}
