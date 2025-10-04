package com.webcrafters.helpify.controladores;

import com.webcrafters.helpify.DTO.DonacionDTO;
import com.webcrafters.helpify.DTO.DonacionSinUsuarioyProyectoDTO;
import com.webcrafters.helpify.interfaces.IDonacionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200",
        allowCredentials = "true",
        exposedHeaders = "Authorization")
@RequestMapping("/api")
public class DonacionController {
    @Autowired
    private IDonacionService donacionService;

    @PreAuthorize("hasRole('DONANTE')")
    @PostMapping("/donacion")
    public ResponseEntity<DonacionDTO> insertarDonacion(@Valid @RequestBody DonacionDTO donacionDTO) {
        // Si la validación falla, este código no se ejecutará.
        // Spring lanzará una excepción MethodArgumentNotValidException
        log.info("Registrando donacion {}", donacionDTO.getEstado());
        return ResponseEntity.ok(donacionService.insertarDonacion(donacionDTO));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DONANTE')")
    @GetMapping("/donaciones")
    public List<DonacionSinUsuarioyProyectoDTO> listarDonaciones(){
        log.info("Lista de donaciones");
        return donacionService.listarTodos();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DONANTE')")
    @GetMapping("/donacion/{idDonacion}")
    public ResponseEntity<DonacionSinUsuarioyProyectoDTO> buscarDonacionPorId(@PathVariable Long idDonacion){
        return ResponseEntity.ok(donacionService.buscarPorId(idDonacion));
    }

    @PreAuthorize("hasRole('DONANTE')")
    @PutMapping("/donacion")
    public ResponseEntity<DonacionDTO> actualizarDonacion(@RequestBody DonacionDTO donacionDTO){
        return ResponseEntity.ok(donacionService.actualizarDonacion(donacionDTO));
    }

    @PreAuthorize("hasRole('DONANTE')")
    @DeleteMapping("/donacion/{id}")
    public void eliminarDonacion(@PathVariable Long id)
    {
        donacionService.eliminarDonacion(id);
    }

}
