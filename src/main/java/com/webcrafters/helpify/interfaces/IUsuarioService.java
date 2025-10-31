package com.webcrafters.helpify.interfaces;

import com.webcrafters.helpify.DTO.RegistroDonanteRespuestaDTO;
import com.webcrafters.helpify.seguridad.DTO.UsuarioConComentariosDTO;
import com.webcrafters.helpify.seguridad.DTO.UsuarioConDonacionesDTO;
import com.webcrafters.helpify.seguridad.DTO.UsuarioDTO;

import java.util.List;

public interface IUsuarioService {
    public RegistroDonanteRespuestaDTO insertar(UsuarioDTO usuarioDTO);
    public UsuarioDTO actualizar(UsuarioDTO usuarioDTO);
    public void eliminar(Long id);
    public UsuarioDTO buscarPorId(Long idUsuario);
    public List<UsuarioDTO> listarTodos();
    public List<UsuarioConComentariosDTO> listarUsuariosConComentarios();
    public List<UsuarioConDonacionesDTO> listarUsuariosConDonaciones();

    Long obtenerIdPorUsername(String username);

}
