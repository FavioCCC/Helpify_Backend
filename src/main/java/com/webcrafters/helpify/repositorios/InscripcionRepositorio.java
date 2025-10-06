package com.webcrafters.helpify.repositorios;

import com.webcrafters.helpify.entidades.Inscripcion;
import com.webcrafters.helpify.entidades.Proyecto;
import com.webcrafters.helpify.entidades.Universitario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscripcionRepositorio extends JpaRepository<Inscripcion, Long> {
    public long countByProyecto(Proyecto proyecto);
    List<Inscripcion> findByUniversitarioAndProyecto(Universitario universitario, Proyecto proyecto);
    boolean existsByUniversitarioAndProyecto(Universitario universitario, Proyecto proyecto);

}
