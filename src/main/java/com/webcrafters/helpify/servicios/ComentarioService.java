package com.webcrafters.helpify.servicios;

import com.webcrafters.helpify.DTO.ComentarioDTO;
import com.webcrafters.helpify.DTO.ComentarioSinProyectoyUsuarioDTO;
import com.webcrafters.helpify.entidades.Comentario;
import com.webcrafters.helpify.entidades.Proyecto;
import com.webcrafters.helpify.seguridad.entidades.Usuario;
import com.webcrafters.helpify.interfaces.IComentarioService;
import com.webcrafters.helpify.repositorios.ComentarioRepositorio;
import com.webcrafters.helpify.repositorios.ProyectoRepositorio;
import com.webcrafters.helpify.seguridad.repositorios.UsuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComentarioService implements IComentarioService {

    @Autowired
    private ComentarioRepositorio comentarioRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ComentarioDTO insertarComentario(ComentarioDTO comentarioDTO, Long idUsuario) {
        Usuario usuario = usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));

        Comentario comentarioEntidad = modelMapper.map(comentarioDTO, Comentario.class);
        comentarioEntidad.setUsuario(usuario);

        Comentario guardado = comentarioRepositorio.save(comentarioEntidad);
        return modelMapper.map(guardado, ComentarioDTO.class);
    }

    @Override
    public ComentarioDTO actualizarComentario(ComentarioDTO comentarioDTO, Long idUsuario) {
        Comentario existing = comentarioRepositorio.findById(comentarioDTO.getIdcomentario())
                .orElseThrow(() -> new RuntimeException("Comentario con ID " + comentarioDTO.getIdcomentario() + " no encontrado"));

        if (!existing.getUsuario().getIdusuario().equals(idUsuario)) {
            throw new RuntimeException("No autorizado para actualizar este comentario");
        }

        existing.setComentario(comentarioDTO.getComentario());
        existing.setEstrella(comentarioDTO.getEstrella());

        Comentario actualizado = comentarioRepositorio.save(existing);
        return modelMapper.map(actualizado, ComentarioDTO.class);
    }

    @Override
    public void eliminarComentario(Long id, Long idUsuario) {
        Comentario comentario = comentarioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado con ID: " + id));

        if (!comentario.getUsuario().getIdusuario().equals(idUsuario)) {
            throw new RuntimeException("No autorizado para eliminar este comentario");
        }

        comentarioRepositorio.deleteById(id);
    }

    @Override
    public List<ComentarioSinProyectoyUsuarioDTO> listarComentarioPorUsuario(Long idUsuario) {
        return comentarioRepositorio.findByUsuarioIdusuario(idUsuario).stream()
                .map(c -> modelMapper.map(c, ComentarioSinProyectoyUsuarioDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ComentarioSinProyectoyUsuarioDTO> buscarPorCalificacion(double estrella) {
        return comentarioRepositorio.findByEstrella(estrella).stream()
                .map(c -> modelMapper.map(c, ComentarioSinProyectoyUsuarioDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ComentarioSinProyectoyUsuarioDTO> listarTodosComentarios() {
        return comentarioRepositorio.findAll().stream()
                .map(c -> modelMapper.map(c, ComentarioSinProyectoyUsuarioDTO.class))
                .collect(Collectors.toList());
    }

}
