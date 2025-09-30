package com.webcrafters.helpify.interfaces;

import com.webcrafters.helpify.DTO.UniversitarioConUsuarioDTO;
import com.webcrafters.helpify.DTO.UniversitarioDTO;

import java.util.List;

public interface IUniversitarioService {
    public UniversitarioDTO insertarUniversitario(UniversitarioDTO universitarioDTO);
    public UniversitarioDTO actualizarUniversitario(UniversitarioDTO universitarioDTO);
    public void eliminarUniversitario(Long id);
    public UniversitarioDTO buscarPorIdUniversitario(Long idUniversitario);
    public List<UniversitarioConUsuarioDTO> listarUniversitariosConUsuario();


}
