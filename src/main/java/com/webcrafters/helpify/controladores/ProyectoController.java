package com.webcrafters.helpify.controladores;

import com.webcrafters.helpify.DTO.*;
import com.webcrafters.helpify.interfaces.IProyectoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200",
        allowCredentials = "true",
        exposedHeaders = "Authorization")
@RequestMapping("/api")
public class ProyectoController {
    @Autowired
    private IProyectoService proyectoService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/proyecto")
    public ResponseEntity<ProyectoSoloConDatosDTO> insertarProyecto(@Valid @RequestBody ProyectoDTO proyectoDTO) {
        // Si la validación falla, este código no se ejecutará.
        // Spring lanzará una excepción MethodArgumentNotValidException
        log.info("Registrando proyecto {}", proyectoDTO.getNombreproyecto());
        return ResponseEntity.ok(proyectoService.insertarProyecto(proyectoDTO));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO', 'DONANTE')")
    @GetMapping("/proyectos")
    public List<ProyectoSoloConDatosDTO> listarProyectos(){
        log.info("Lista de proyectos");
        return proyectoService.listarTodosLosProyectos();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/proyectos/comentarios")
    public List<ProyectoConComentariosDTO> listarProyectosConComentarios(){
        log.info("Lista de proyectos");
        return proyectoService.listarProyectosConComentarios();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/proyectos/donaciones")
    public List<ProyectoConDonacionesDTO> listarProyectosConDonaciones(){
        log.info("Lista de proyectos");
        return proyectoService.listarProyectosConDonaciones();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO', 'DONANTE')")
    @GetMapping("/proyectos/async")
    public CompletableFuture<List<ProyectoSoloConDatosDTO>> listarProyectosAsync() {
        return proyectoService.listarTodosLosProyectosAsync();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/proyecto")
    public ResponseEntity<ProyectoDTO> actualizarProyecto(@RequestBody ProyectoDTO proyectoDTO){
        return ResponseEntity.ok(proyectoService.actualizarProyecto(proyectoDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/proyecto/{id}")
    public void eliminarProyecto(@PathVariable Long id)
    {
        proyectoService.eliminarProyecto(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO', 'DONANTE')")
    @GetMapping("/buscar/nombre")
    public List<ProyectoSoloConDatosDTO> buscarPorNombre(@RequestParam String nombre) {
        return proyectoService.buscarPorNombreProyecto(nombre);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO', 'DONANTE')")
    @GetMapping("/buscar/monto")
    public List<ProyectoSoloConDatosDTO> buscarPorMonto(@RequestParam double monto) {
        return proyectoService.buscarPorMontoObjetivo(monto);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO', 'DONANTE')")
    @GetMapping("/buscar/fechas")
    public List<ProyectoSoloConDatosDTO> buscarPorFechas(@RequestParam LocalDate inicio, @RequestParam LocalDate fin) {
        return proyectoService.buscarPorFechaInicioEntreFechaFin(inicio, fin);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO', 'DONANTE')")
    @GetMapping("/buscar/anio-mes")
    public List<ProyectoSoloConDatosDTO> buscarPorAnioYMes(@RequestParam int anio, @RequestParam int mes) {
        return proyectoService.buscarPorAnioYMes(anio, mes);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/universitarios-por-proyecto")
    public List<UniversitariosPorProyectoDTO> obtenerUniversitariosPorProyecto() {
        return proyectoService.obtenerUniversitariosPorProyecto();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/porcentaje-universitarios")
    public List<PorcentajeUniversitariosDTO> obtenerPorcentajeUniversitariosPorProyecto() {
        long total = proyectoService.obtenerTotalUniversitarios();
        return proyectoService.obtenerPorcentajeUniversitariosPorProyecto(total);
    }
}

