package com.webcrafters.helpify.seguridad.servicios;

import com.webcrafters.helpify.seguridad.entidades.Rol;
import com.webcrafters.helpify.seguridad.repositorios.RolRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolServicio {
    @Autowired
    private RolRepositorio rolRepositorio;

    public Rol save(Rol rol) {
        return rolRepositorio.save(rol);
    }

    public List<Rol> listarTodos() {
        return rolRepositorio.findAll();
    }

    public Optional<Rol> buscarPorId(Long id) {
        return rolRepositorio.findById(id);
    }
}
