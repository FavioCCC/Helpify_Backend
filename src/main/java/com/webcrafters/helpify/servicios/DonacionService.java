// borradorHelpify/src/main/java/com/upc/borradorhelpify/servicios/DonacionService.java
package com.webcrafters.helpify.servicios;

import com.webcrafters.helpify.DTO.DonacionDTO;
import com.webcrafters.helpify.DTO.DonacionSinUsuarioyProyectoDTO;
import com.webcrafters.helpify.entidades.Donacion;
import com.webcrafters.helpify.entidades.Proyecto;
import com.webcrafters.helpify.seguridad.entidades.Usuario;
import com.webcrafters.helpify.interfaces.IDonacionService;
import com.webcrafters.helpify.repositorios.DonacionRepositorio;
import com.webcrafters.helpify.repositorios.ProyectoRepositorio;
import com.webcrafters.helpify.seguridad.repositorios.UsuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonacionService implements IDonacionService {
    @Autowired
    private DonacionRepositorio donacionRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ProyectoRepositorio proyectoRepositorio;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public DonacionDTO insertarDonacion(DonacionDTO donacionDTO) {
        Donacion donacionEntidad = modelMapper.map(donacionDTO, Donacion.class);

        if (donacionDTO.getUsuario() != null && donacionDTO.getUsuario().getIdusuario() != null) {
            Usuario usuario = usuarioRepositorio.findById(donacionDTO.getUsuario().getIdusuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            donacionEntidad.setUsuario(usuario);
        }

        if (donacionDTO.getProyecto() != null && donacionDTO.getProyecto().getIdproyecto() != null) {
            Proyecto proyecto = proyectoRepositorio.findById(donacionDTO.getProyecto().getIdproyecto())
                    .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
            donacionEntidad.setProyecto(proyecto);
        }

        Donacion guardado = donacionRepositorio.save(donacionEntidad);
        return modelMapper.map(guardado, DonacionDTO.class);
    }

    @Override
    public DonacionDTO actualizarDonacion(DonacionDTO donacionDTO) {
        return donacionRepositorio.findById(donacionDTO.getId())
                .map(existing -> {
                    Donacion donacionEntidad = modelMapper.map(donacionDTO, Donacion.class);
                    Donacion guardado = donacionRepositorio.save(donacionEntidad);
                    return modelMapper.map(guardado, DonacionDTO.class);
                })
                .orElseThrow(() -> new RuntimeException("Donación con ID " + donacionDTO.getId() + " no encontrada"));
    }

    @Override
    public void eliminarDonacion(Long id) {
        if (!donacionRepositorio.existsById(id)) {
            throw new RuntimeException("Donación no encontrada con ID: " + id);
        }
        donacionRepositorio.deleteById(id);
    }

    @Override
    public DonacionSinUsuarioyProyectoDTO buscarPorId(Long idDonacion) {
        return donacionRepositorio.findById(idDonacion)
                .map(donacion -> modelMapper.map(donacion, DonacionSinUsuarioyProyectoDTO.class))
                .orElseThrow(() -> new RuntimeException("Donación con ID " + idDonacion + " no encontrada"));
    }

    @Override
    public List<DonacionSinUsuarioyProyectoDTO> listarTodos() {
        return donacionRepositorio.findAll().stream()
                .map(donacion -> modelMapper.map(donacion, DonacionSinUsuarioyProyectoDTO.class))
                .collect(Collectors.toList());
    }
}