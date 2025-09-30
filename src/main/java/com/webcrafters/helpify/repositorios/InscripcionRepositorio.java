package com.webcrafters.helpify.repositorios;

import com.webcrafters.helpify.entidades.Inscripcion;
import com.webcrafters.helpify.entidades.Proyecto;
import com.webcrafters.helpify.entidades.Universitario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscripcionRepositorio extends JpaRepository<Inscripcion, Long> {
    public long countByProyecto(Proyecto proyecto);
    public Inscripcion findByUniversitarioAndProyecto(Universitario universitario, Proyecto proyecto);
}
