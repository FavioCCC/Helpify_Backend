// src/main/java/com/upc/borradorhelpify/repositorios/UniversitarioRepositorio.java
package com.webcrafters.helpify.repositorios;

import com.webcrafters.helpify.entidades.Universitario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UniversitarioRepositorio extends JpaRepository<Universitario, Long> {
    Optional<Universitario> findByCodigoestudiante(String codigoestudiante);
}
