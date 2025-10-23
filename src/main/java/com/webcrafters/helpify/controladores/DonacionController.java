package com.webcrafters.helpify.controladores;

import com.webcrafters.helpify.DTO.DonacionDTO;
import com.webcrafters.helpify.DTO.DonacionSinUsuarioyProyectoDTO;
import com.webcrafters.helpify.interfaces.IDonacionService;
import com.webcrafters.helpify.seguridad.DTO.UsuarioSoloConDatosDTO;
import com.webcrafters.helpify.seguridad.entidades.Usuario;
import com.webcrafters.helpify.seguridad.repositorios.UsuarioRepositorio;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @PreAuthorize("hasRole('DONANTE')")
    @PostMapping("/donacion")
    public ResponseEntity<DonacionDTO> insertarDonacion(@Valid @RequestBody DonacionDTO donacionDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new BadCredentialsException("Usuario no autenticado");
        }

        String username = authentication.getName();
        Usuario usuario = usuarioRepositorio.findByNombre(username)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado: " + username));

        // Asociar el id del usuario autenticado en la DTO para que el servicio lo use
        if (donacionDTO.getUsuario() == null) {
            UsuarioSoloConDatosDTO usuarioDto = new UsuarioSoloConDatosDTO();
            usuarioDto.setIdusuario(usuario.getIdusuario());
            donacionDTO.setUsuario(usuarioDto);
        } else {
            donacionDTO.getUsuario().setIdusuario(usuario.getIdusuario());
        }

        log.info("Registrando donacion por usuario id={} para proyecto id={}", usuario.getIdusuario(),
                donacionDTO.getProyecto() != null ? donacionDTO.getProyecto().getIdproyecto() : "null");

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
