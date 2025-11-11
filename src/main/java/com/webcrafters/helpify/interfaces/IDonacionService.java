package com.webcrafters.helpify.interfaces;

import com.webcrafters.helpify.DTO.DonacionDTO;
import com.webcrafters.helpify.DTO.DonacionSinUsuarioyProyectoDTO;

import java.util.List;

public interface IDonacionService {
    public DonacionDTO insertarDonacion(DonacionDTO donacionDTO);
    public DonacionDTO actualizarDonacion(DonacionDTO donacionDTO);
    public void eliminarDonacion(Long id);
    public DonacionSinUsuarioyProyectoDTO buscarPorId(Long idDonacion);
    public List<DonacionSinUsuarioyProyectoDTO> listarTodos();
    DonacionDTO insertarDonacionComoUsuarioActual(DonacionDTO dto);
    List<DonacionSinUsuarioyProyectoDTO> listarDonacionesDelUsuarioActual();
    DonacionSinUsuarioyProyectoDTO buscarPorIdRestringiendoAUsuarioActualSiCorresponde(Long idDonacion);
    DonacionDTO actualizarDonacionComoUsuarioActual(DonacionDTO dto);
    void eliminarDonacionComoUsuarioActual(Long idDonacion);


}
