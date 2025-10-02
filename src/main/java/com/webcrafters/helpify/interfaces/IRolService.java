package com.webcrafters.helpify.interfaces;

import com.webcrafters.helpify.seguridad.DTO.RolDTO;

import java.util.List;

public interface IRolService {
    public RolDTO insertarRol(RolDTO rolDTO);
    public RolDTO actualizarRol(RolDTO rolDTO);
    public void eliminarRol(Long id);
    public RolDTO buscarRolPorId(Long idRol);
    public List<RolDTO> listarTodosLosRoles();
}