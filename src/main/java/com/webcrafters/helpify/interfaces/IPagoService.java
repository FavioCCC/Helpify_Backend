package com.webcrafters.helpify.interfaces;

import com.webcrafters.helpify.DTO.PagoDTO;

import java.util.List;

public interface IPagoService {
    public PagoDTO insertarPago(PagoDTO pagoDTO);
    public PagoDTO actualizarPago(PagoDTO pagoDTO);
    public void eliminarPago(Long idPago);
    public List<PagoDTO> listarTodos();
}
