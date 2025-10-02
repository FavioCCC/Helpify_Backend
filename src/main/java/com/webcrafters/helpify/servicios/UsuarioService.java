package com.webcrafters.helpify.servicios;

import com.webcrafters.helpify.DTO.*;
import com.webcrafters.helpify.seguridad.DTO.UsuarioConComentariosDTO;
import com.webcrafters.helpify.seguridad.DTO.UsuarioConDonacionesDTO;
import com.webcrafters.helpify.seguridad.DTO.UsuarioDTO;
import com.webcrafters.helpify.seguridad.entidades.Rol;
import com.webcrafters.helpify.seguridad.entidades.Usuario;
import com.webcrafters.helpify.interfaces.IUsuarioService;
import com.webcrafters.helpify.seguridad.repositorios.RolRepositorio;
import com.webcrafters.helpify.seguridad.repositorios.UsuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private RolRepositorio rolRepositorio;
    @Autowired
    private ModelMapper modelMapper;



    @Override
    public UsuarioDTO insertar(UsuarioDTO usuarioDTO) {
        Usuario usuarioEntidad = modelMapper.map(usuarioDTO, Usuario.class);

        if (usuarioDTO.getIdRol() != null) {
            Rol rol = rolRepositorio.findById(usuarioDTO.getIdRol())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + usuarioDTO.getIdRol()));
            usuarioEntidad.setRol(rol);
        }

        Usuario guardado = usuarioRepositorio.save(usuarioEntidad);
        UsuarioDTO dtoSalida = modelMapper.map(guardado, UsuarioDTO.class);
        // Asignar manualmente el idRol si el rol existe
        if (guardado.getRol() != null) {
            dtoSalida.setIdRol(guardado.getRol().getIdrol());
        }
        return dtoSalida;
    }

    @Override
    public UsuarioDTO actualizar(UsuarioDTO usuarioDTO) {
        return usuarioRepositorio.findById(usuarioDTO.getIdusuario())
                .map(existing -> {
                    Usuario usuarioEntidad = modelMapper.map(usuarioDTO, Usuario.class);
                    Usuario guardado = usuarioRepositorio.save(usuarioEntidad);
                    return modelMapper.map(guardado, UsuarioDTO.class);
                })
                .orElseThrow(() -> new RuntimeException("Usuario con ID " + usuarioDTO.getIdusuario() +
                        " no encontrado"));
    }

    @Override
    public void eliminar(Long id) {
        if (!usuarioRepositorio.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepositorio.deleteById(id);
    }

    @Override
    public UsuarioDTO buscarPorId(Long idUsuario) {
        return usuarioRepositorio.findById(idUsuario)
                .map(usuario -> modelMapper.map(usuario, UsuarioDTO.class))
                .orElseThrow(() -> new RuntimeException("Usuario con ID " + idUsuario + " no encontrado"));
    }

    @Override
    public List<UsuarioDTO> listarTodos() {
        return usuarioRepositorio.findAll().stream()
                .map(usuario -> {
                    UsuarioDTO dto = modelMapper.map(usuario, UsuarioDTO.class);
                    if (usuario.getRol() != null) {
                        dto.setIdRol(usuario.getRol().getIdrol());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UsuarioConComentariosDTO> listarUsuariosConComentarios() {
        return usuarioRepositorio.findAll().stream()
                .map(usuario -> {
                    UsuarioConComentariosDTO dto = modelMapper.map(usuario, UsuarioConComentariosDTO.class);
                    // convertir comentarios a DTO
                    dto.setComentarios(
                            usuario.getComentarios().stream()
                                    .map(c -> modelMapper.map(c, ComentarioSoloDatosDTO.class))
                                    .collect(Collectors.toList())
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UsuarioConDonacionesDTO> listarUsuariosConDonaciones() {
        return usuarioRepositorio.findAll().stream()
                .map(usuario -> {
                    UsuarioConDonacionesDTO dto = modelMapper.map(usuario, UsuarioConDonacionesDTO.class);
                    // convertir donaciones a DTO
                    dto.setDonaciones(
                            usuario.getDonaciones().stream()
                                    .map(d -> modelMapper.map(d, DonacionSoloDatosDTO.class))
                                    .collect(Collectors.toList())
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
