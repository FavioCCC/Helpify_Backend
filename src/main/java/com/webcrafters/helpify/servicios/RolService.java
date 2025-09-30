package com.webcrafters.helpify.servicios;

import com.webcrafters.helpify.DTO.RolDTO;
import com.webcrafters.helpify.entidades.Rol;
import com.webcrafters.helpify.interfaces.IRolService;
import com.webcrafters.helpify.repositorios.RolRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolService implements IRolService {
    @Autowired
    private RolRepositorio rolRepositorio;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RolDTO insertarRol(RolDTO rolDTO) {
        Rol rolEntidad = modelMapper.map(rolDTO, Rol.class);
        Rol guardado = rolRepositorio.save(rolEntidad);
        return modelMapper.map(guardado, RolDTO.class);
    }

    @Override
    public RolDTO actualizarRol(RolDTO rolDTO) {
        return rolRepositorio.findById(rolDTO.getIdrol())
                .map(existing -> {
                    Rol rolEntidad = modelMapper.map(rolDTO, Rol.class);
                    Rol guardado = rolRepositorio.save(rolEntidad);
                    return modelMapper.map(guardado, RolDTO.class);
                })
                .orElseThrow(() -> new RuntimeException("Rol con ID " + rolDTO.getIdrol() +
                        " no encontrado"));
    }

    @Override
    public void eliminarRol(Long id) {
        if (!rolRepositorio.existsById(id)) {
            throw new RuntimeException("Rol no encontrado con ID: " + id);
        }
        rolRepositorio.deleteById(id);
    }

    @Override
    public RolDTO buscarRolPorId(Long idRol) {
        return rolRepositorio.findById(idRol).
                map((element) -> modelMapper.map(element, RolDTO.class))
                .orElseThrow(() -> new RuntimeException("Usuario con ID " + idRol + " no encontrado"));
    }

    @Override
    public List<RolDTO> listarTodosLosRoles() {
        return rolRepositorio.findAll().stream().map(rol -> modelMapper.map(rol, RolDTO.class))
                .collect(Collectors.toList());
    }
}
