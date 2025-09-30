package com.webcrafters.helpify.interfaces;

import com.webcrafters.helpify.DTO.ProyectoConComentariosDTO;
import com.webcrafters.helpify.DTO.ProyectoConDonacionesDTO;
import com.webcrafters.helpify.DTO.ProyectoDTO;
import com.webcrafters.helpify.DTO.ProyectoSoloConDatosDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IProyectoService {
    public ProyectoSoloConDatosDTO insertarProyecto(ProyectoDTO proyectoDTO);
    public ProyectoDTO actualizarProyecto(ProyectoDTO proyectoDTO);
    public void eliminarProyecto(Long id);
    public List<ProyectoSoloConDatosDTO> listarTodosLosProyectos();
    public List<ProyectoConComentariosDTO> listarProyectosConComentarios();
    public List<ProyectoConDonacionesDTO> listarProyectosConDonaciones();

    public CompletableFuture<List<ProyectoSoloConDatosDTO>> listarTodosLosProyectosAsync();
    public List<ProyectoSoloConDatosDTO> buscarPorNombreProyecto(String nombreProyecto);
    public List<ProyectoSoloConDatosDTO> buscarPorMontoObjetivo(double monto);
    public List<ProyectoSoloConDatosDTO> buscarPorFechaInicioEntreFechaFin(LocalDate fechaInicio, LocalDate fechaFin);
    public List<ProyectoSoloConDatosDTO> buscarPorAnioYMes(int anio, int mes);
}
