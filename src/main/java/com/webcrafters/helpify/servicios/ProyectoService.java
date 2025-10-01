package com.webcrafters.helpify.servicios;

import com.webcrafters.helpify.DTO.*;
import com.webcrafters.helpify.entidades.Proyecto;
import com.webcrafters.helpify.interfaces.IProyectoService;
import com.webcrafters.helpify.repositorios.ProyectoRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ProyectoService implements IProyectoService {

    @Autowired
    private ProyectoRepositorio proyectoRepositorio;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProyectoSoloConDatosDTO insertarProyecto(ProyectoDTO proyectoDTO) {
        Proyecto proyectoEntidad = modelMapper.map(proyectoDTO, Proyecto.class);
        Proyecto guardado = proyectoRepositorio.save(proyectoEntidad);
        return modelMapper.map(guardado, ProyectoSoloConDatosDTO.class);
    }

    @Override
    public ProyectoDTO actualizarProyecto(ProyectoDTO proyectoDTO) {
        return proyectoRepositorio.findById(proyectoDTO.getIdproyecto())
                .map(existing -> {
                    Proyecto proyectoEntidad = modelMapper.map(proyectoDTO, Proyecto.class);
                    Proyecto guardado = proyectoRepositorio.save(proyectoEntidad);
                    return modelMapper.map(guardado, ProyectoDTO.class);
                })
                .orElseThrow(() -> new RuntimeException("Proyecto con ID " + proyectoDTO.getIdproyecto() +
                        " no encontrado"));
    }

    @Override
    public void eliminarProyecto(Long id) {
        if (!proyectoRepositorio.existsById(id)) {
            throw new RuntimeException("Proyecto no encontrado con ID: " + id);
        }
        proyectoRepositorio.deleteById(id);
    }

    @Override
    public List<ProyectoSoloConDatosDTO> listarTodosLosProyectos() {
        return proyectoRepositorio.findAll().stream().map(proyecto -> modelMapper.map(proyecto, ProyectoSoloConDatosDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProyectoConComentariosDTO> listarProyectosConComentarios() {
        return proyectoRepositorio.findAll().stream()
                .map(proyecto -> {
                    ProyectoConComentariosDTO dto = modelMapper.map(proyecto, ProyectoConComentariosDTO.class);
                    dto.setComentarios(
                            proyecto.getComentarios().stream()
                                    .map(c -> modelMapper.map(c, ComentarioSoloDatosDTO.class))
                                    .collect(Collectors.toList())
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ProyectoConDonacionesDTO> listarProyectosConDonaciones() {
        return proyectoRepositorio.findAll().stream()
                .map(proyecto -> {
                    ProyectoConDonacionesDTO dto = modelMapper.map(proyecto, ProyectoConDonacionesDTO.class);
                    dto.setDonaciones(
                            proyecto.getDonaciones().stream()
                                    .map(d -> modelMapper.map(d, DonacionSoloDatosDTO.class))
                                    .collect(Collectors.toList())
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Async
    @Override
    public CompletableFuture<List<ProyectoSoloConDatosDTO>> listarTodosLosProyectosAsync() {
        List<ProyectoSoloConDatosDTO> proyectos = proyectoRepositorio.findAll()
                .stream()
                .map(proyecto -> modelMapper.map(proyecto, ProyectoSoloConDatosDTO.class))
                .collect(Collectors.toList());

        return CompletableFuture.completedFuture(proyectos);
    }

    @Override
    public List<ProyectoSoloConDatosDTO> buscarPorNombreProyecto(String nombreproyecto) {
        return proyectoRepositorio.findAllByNombreproyectoContainingIgnoreCase(nombreproyecto)
                .stream()
                .map(proyecto -> modelMapper.map(proyecto, ProyectoSoloConDatosDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProyectoSoloConDatosDTO> buscarPorMontoObjetivo(double montoobjetivo) {
        return proyectoRepositorio.findAllByMontoobjetivo(montoobjetivo)
                .stream()
                .map(proyecto -> modelMapper.map(proyecto, ProyectoSoloConDatosDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProyectoSoloConDatosDTO> buscarPorFechaInicioEntreFechaFin(LocalDate fechainicio, LocalDate fechafin) {
        return proyectoRepositorio.findByFechainicioBetween(fechainicio, fechafin)
                .stream()
                .map(proyecto -> modelMapper.map(proyecto, ProyectoSoloConDatosDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProyectoSoloConDatosDTO> buscarPorAnioYMes(int anio, int mes) {
        return proyectoRepositorio.findAll().stream()
                .filter(proyecto -> proyecto.getFechainicio().getYear() == anio
                        && proyecto.getFechafin().getMonthValue() == mes)
                .map(proyecto -> modelMapper.map(proyecto, ProyectoSoloConDatosDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UniversitariosPorProyectoDTO> obtenerUniversitariosPorProyecto() {
        return proyectoRepositorio.obtenerUniversitariosPorProyecto();
    }

    @Override
    public long obtenerTotalUniversitarios() {
        return proyectoRepositorio.obtenerTotalUniversitarios();
    }

    @Override
    public List<PorcentajeUniversitariosDTO> obtenerPorcentajeUniversitariosPorProyecto(long total) {
        return proyectoRepositorio.obtenerPorcentajeUniversitariosPorProyecto(total);
    }
}

