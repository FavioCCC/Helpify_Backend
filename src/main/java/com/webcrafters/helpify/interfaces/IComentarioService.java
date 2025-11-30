package com.webcrafters.helpify.interfaces;

import com.webcrafters.helpify.DTO.ComentarioDTO;
import com.webcrafters.helpify.DTO.ComentarioSinProyectoyUsuarioDTO;

import java.util.List;

public interface IComentarioService {
    public ComentarioDTO insertarComentario(ComentarioDTO comentarioDTO, Long idUsuario);
    public ComentarioDTO actualizarComentario(ComentarioDTO comentarioDTO, Long idUsuario);
    public void eliminarComentario(Long id, Long idUsuario, boolean esAdmin);
    public List<ComentarioSinProyectoyUsuarioDTO> listarComentarioPorUsuario(Long idUsuario);
    public List<ComentarioSinProyectoyUsuarioDTO> buscarPorCalificacion(double estrella);
    public List<ComentarioSinProyectoyUsuarioDTO> listarTodosComentarios();
}
