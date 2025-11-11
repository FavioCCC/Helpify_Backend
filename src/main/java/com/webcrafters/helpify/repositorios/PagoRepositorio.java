package com.webcrafters.helpify.repositorios;

import com.webcrafters.helpify.entidades.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PagoRepositorio extends JpaRepository<Pago, Long> {

    boolean existsByDonacion_Id(Long idDonacion);

    // antes: findByDonacion_Usuario_Id(...)
    List<Pago> findByDonacion_Usuario_Idusuario(Long idUsuario);
}


