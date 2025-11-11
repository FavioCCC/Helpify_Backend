package com.webcrafters.helpify.repositorios;

import com.webcrafters.helpify.entidades.Donacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DonacionRepositorio extends JpaRepository<Donacion, Long> {

    // antes: findByIdAndUsuario_Id(...)
    Optional<Donacion> findByIdAndUsuario_Idusuario(Long idDonacion, Long idUsuario);

    // antes: findByUsuario_Id(...)
    List<Donacion> findByUsuario_Idusuario(Long idUsuario);

    boolean existsByProyecto_Idproyecto(Long idproyecto);
}
