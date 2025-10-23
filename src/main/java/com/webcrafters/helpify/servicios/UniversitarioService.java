package com.webcrafters.helpify.servicios;

import com.webcrafters.helpify.DTO.UniversitarioConUsuarioDTO;
import com.webcrafters.helpify.DTO.UniversitarioDTO;
import com.webcrafters.helpify.seguridad.DTO.UsuarioSoloConDatosDTO;
import com.webcrafters.helpify.entidades.Universitario;
import com.webcrafters.helpify.seguridad.entidades.Usuario;
import com.webcrafters.helpify.interfaces.IUniversitarioService;
import com.webcrafters.helpify.repositorios.UniversitarioRepositorio;
import com.webcrafters.helpify.seguridad.repositorios.UsuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class
UniversitarioService implements IUniversitarioService {
    @Autowired
    private UniversitarioRepositorio universitarioRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UniversitarioDTO insertarUniversitario(UniversitarioDTO universitarioDTO){
        if (universitarioRepositorio.findByCodigoestudiante(universitarioDTO.getCodigoestudiante()).isPresent()) {
            throw new RuntimeException("El código de estudiante ya está registrado: " + universitarioDTO.getCodigoestudiante());
        }

        Usuario usuario = usuarioRepositorio.findById(universitarioDTO.getIdusuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + universitarioDTO.getIdusuario()));

        if (usuario.getUniversitario() != null) {
            throw new RuntimeException("El usuario ya tiene un perfil universitario asociado");
        }

        Universitario universitario = new Universitario();
        universitario.setCodigoestudiante(universitarioDTO.getCodigoestudiante());
        universitario.setUsuario(usuario);

        Universitario guardado = universitarioRepositorio.save(universitario);

        // asegurar la asociación desde la entidad Usuario (FK en usuario.iduniversitario)
        usuario.setUniversitario(guardado);
        usuarioRepositorio.save(usuario);

        UniversitarioDTO dto = new UniversitarioDTO();
        dto.setIduniversitario(guardado.getIduniversitario());
        dto.setCodigoestudiante(guardado.getCodigoestudiante());
        dto.setIdusuario(guardado.getUsuario().getIdusuario());
        return dto;
    }


    @Override
    public UniversitarioDTO actualizarUniversitario(UniversitarioDTO universitarioDTO) {
        Universitario existente = universitarioRepositorio.findById(universitarioDTO.getIduniversitario())
                .orElseThrow(() -> new RuntimeException("Universitario no encontrado"));
        existente.setCodigoestudiante(universitarioDTO.getCodigoestudiante());

        Universitario guardado = universitarioRepositorio.save(existente);
        return modelMapper.map(guardado, UniversitarioDTO.class);
    }

    @Override
    public void eliminarUniversitario(Long id) {
        if (!universitarioRepositorio.existsById(id)) {
            throw new RuntimeException("Universitario no encontrado con ID: " + id);
        }
        universitarioRepositorio.deleteById(id);

    }

    @Override
    public UniversitarioDTO buscarPorIdUniversitario(Long idUniversitario) {
        return universitarioRepositorio.findById(idUniversitario).
                map((element) -> modelMapper.map(element, UniversitarioDTO.class))
                .orElseThrow(() -> new RuntimeException("Universitario con ID " + idUniversitario + " no encontrado"));
    }

    @Override
    public List<UniversitarioConUsuarioDTO> listarUniversitariosConUsuario() {
        return universitarioRepositorio.findAll().stream()
                .map(u -> {
                    UniversitarioConUsuarioDTO dto = new UniversitarioConUsuarioDTO();
                    dto.setIduniversitario(u.getIduniversitario());
                    dto.setCodigoestudiante(u.getCodigoestudiante());
                    dto.setUsuario(modelMapper.map(u.getUsuario(), UsuarioSoloConDatosDTO.class));
                    return dto;
                })
                .collect(Collectors.toList());
    }


}
