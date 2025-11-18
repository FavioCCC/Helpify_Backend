package com.webcrafters.helpify.interfaces;

import com.webcrafters.helpify.DTO.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.math.BigDecimal;

public interface IProyectoService {
    public ProyectoSoloConDatosDTO insertarProyecto(ProyectoDTO proyectoDTO);
    public ProyectoDTO actualizarProyecto(ProyectoDTO proyectoDTO);
    public void eliminarProyecto(Long id);
    public List<ProyectoSoloConDatosDTO> listarTodosLosProyectos();
    public List<ProyectoConDonacionesDTO> listarProyectosConDonaciones();
    public List<ProyectoConInscripcionesDTO> listarProyectosConInscripciones();
    public CompletableFuture<List<ProyectoSoloConDatosDTO>> listarTodosLosProyectosAsync();
    public List<ProyectoSoloConDatosDTO> buscarPorNombreProyecto(String nombreProyecto);

    public List<ProyectoSoloConDatosDTO> buscarPorFechaInicioEntreFechaFin(LocalDate fechaInicio, LocalDate fechaFin);
    public List<ProyectoSoloConDatosDTO> buscarPorAnioYMes(int anio, int mes);

    List<ProyectoSoloConDatosDTO> buscarPorMontoObjetivo(BigDecimal min, BigDecimal max);


    List<UniversitariosPorProyectoDTO> obtenerUniversitariosPorProyecto();

    long obtenerTotalUniversitarios();
    ProyectoSoloConDatosDTO obtenerProyectoPorId(Long id);

    List<PorcentajeUniversitariosDTO> obtenerPorcentajeUniversitariosPorProyecto(@Param("total") long total);
}
