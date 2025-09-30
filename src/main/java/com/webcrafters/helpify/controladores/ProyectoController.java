package com.webcrafters.helpify.controladores;

import com.webcrafters.helpify.DTO.ProyectoConComentariosDTO;
import com.webcrafters.helpify.DTO.ProyectoConDonacionesDTO;
import com.webcrafters.helpify.DTO.ProyectoDTO;
import com.webcrafters.helpify.DTO.ProyectoSoloConDatosDTO;
import com.webcrafters.helpify.interfaces.IProyectoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/proyecto")
    public ResponseEntity<ProyectoSoloConDatosDTO> insertarProyecto(@Valid @RequestBody ProyectoDTO proyectoDTO) {
        // Si la validación falla, este código no se ejecutará.
        // Spring lanzará una excepción MethodArgumentNotValidException
        log.info("Registrando proyecto {}", proyectoDTO.getNombreproyecto());
        return ResponseEntity.ok(proyectoService.insertarProyecto(proyectoDTO));
    }

    @GetMapping("/proyectos")
    public List<ProyectoSoloConDatosDTO> listarProyectos(){
        log.info("Lista de proyectos");
        return proyectoService.listarTodosLosProyectos();
    }

    @GetMapping("/proyectos/comentarios")
    public List<ProyectoConComentariosDTO> listarProyectosConComentarios(){
        log.info("Lista de proyectos");
        return proyectoService.listarProyectosConComentarios();
    }

    @GetMapping("/proyectos/donaciones")
    public List<ProyectoConDonacionesDTO> listarProyectosConDonaciones(){
        log.info("Lista de proyectos");
        return proyectoService.listarProyectosConDonaciones();
    }

    @GetMapping("/proyectos/async")
    public CompletableFuture<List<ProyectoSoloConDatosDTO>> listarProyectosAsync() {
        return proyectoService.listarTodosLosProyectosAsync();
    }

    @PutMapping("/proyecto")
    public ResponseEntity<ProyectoDTO> actualizarProyecto(@RequestBody ProyectoDTO proyectoDTO){
        return ResponseEntity.ok(proyectoService.actualizarProyecto(proyectoDTO));
    }

    @DeleteMapping("/proyecto/{id}")
    public void eliminarProyecto(@PathVariable Long id)
    {
        proyectoService.eliminarProyecto(id);
    }

    @GetMapping("/buscar/nombre")
    public List<ProyectoSoloConDatosDTO> buscarPorNombre(@RequestParam String nombre) {
        return proyectoService.buscarPorNombreProyecto(nombre);
    }

    @GetMapping("/buscar/monto")
    public List<ProyectoSoloConDatosDTO> buscarPorMonto(@RequestParam double monto) {
        return proyectoService.buscarPorMontoObjetivo(monto);
    }

    @GetMapping("/buscar/fechas")
    public List<ProyectoSoloConDatosDTO> buscarPorFechas(@RequestParam LocalDate inicio, @RequestParam LocalDate fin) {
        return proyectoService.buscarPorFechaInicioEntreFechaFin(inicio, fin);
    }

    @GetMapping("/buscar/anio-mes")
    public List<ProyectoSoloConDatosDTO> buscarPorAnioYMes(@RequestParam int anio, @RequestParam int mes) {
        return proyectoService.buscarPorAnioYMes(anio, mes);
    }
}
