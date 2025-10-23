package com.webcrafters.helpify.repositorios;

import com.webcrafters.helpify.entidades.Donacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonacionRepositorio extends JpaRepository<Donacion, Long> {
    // Navegación a la propiedad real del entity Proyecto (idproyecto)
    boolean existsByProyecto_Idproyecto(Long idproyecto);
}
