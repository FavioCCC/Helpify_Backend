package com.webcrafters.helpify.seguridad.controladores;

import com.webcrafters.helpify.seguridad.entidades.Rol;
import com.webcrafters.helpify.seguridad.servicios.RolServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
public class RolControlador {
    @Autowired
    private RolServicio rolServicio;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Rol> registrarRol(@RequestBody Rol rol) {
        return ResponseEntity.ok(rolServicio.save(rol));
    }
}
