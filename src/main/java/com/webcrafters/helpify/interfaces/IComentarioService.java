package com.webcrafters.helpify.interfaces;

import com.webcrafters.helpify.DTO.ComentarioDTO;
import com.webcrafters.helpify.DTO.ComentarioSinProyectoyUsuarioDTO;

import java.util.List;

public interface IComentarioService {
    public ComentarioDTO insertarComentario(ComentarioDTO comentarioDTO, Long idUsuario, Long idProyecto);
    public ComentarioDTO actualizarComentario(ComentarioDTO comentarioDTO, Long idUsuario, Long idProyecto);
    public void eliminarComentario(Long id, Long idUsuario, Long idProyecto);
    public List<ComentarioSinProyectoyUsuarioDTO> listarComentarioPorProyectoyUsuario(Long idProyecto, Long idUsuario);
    public List<ComentarioSinProyectoyUsuarioDTO> buscarPorCalificacion(double estrella);
}
