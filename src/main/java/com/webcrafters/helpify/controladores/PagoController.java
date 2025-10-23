package com.webcrafters.helpify.controladores;


import com.webcrafters.helpify.DTO.PagoDTO;
import com.webcrafters.helpify.DTO.RegistroPagoRespuestaDTO;
import com.webcrafters.helpify.interfaces.IPagoService;
import com.webcrafters.helpify.seguridad.entidades.Usuario;
import com.webcrafters.helpify.seguridad.repositorios.UsuarioRepositorio;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
        exposedHeaders = {"Authorization", "Mensaje"})
@RequestMapping("/api")
public class PagoController {
    @Autowired
    private IPagoService pagoService;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @PreAuthorize("hasRole('DONANTE')")
    @PostMapping("/pago")
    public ResponseEntity<RegistroPagoRespuestaDTO> insertarPago(@Valid @RequestBody PagoDTO pagoDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new BadCredentialsException("Usuario no autenticado");
        }

        String username = authentication.getName();
        Usuario usuario = usuarioRepositorio.findByNombre(username)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado: " + username));

        if (pagoDTO.getNombretitular() == null || !username.equalsIgnoreCase(pagoDTO.getNombretitular().trim())) {
            throw new BadCredentialsException("El nombre del titular no coincide con el usuario autenticado");
        }

        log.info("Registrando pago por usuario {} para donacionId {}", username,
                pagoDTO.getDonacion() != null ? pagoDTO.getDonacion().getId() : "null");

        // Llamar a la firma que espera 1 argumento (el servicio debe obtener el username desde SecurityContext si lo necesita)
        PagoDTO creado = pagoService.insertarPago(pagoDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Mensaje", "¡Gracias por tu contribución! Tu donación fue registrada correctamente.");

        RegistroPagoRespuestaDTO respuesta = new RegistroPagoRespuestaDTO(
                "¡Gracias por tu contribución! Tu donación fue registrada correctamente.",
                creado
        );

        return new ResponseEntity<>(respuesta, headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('DONANTE')")
    @PutMapping("/pago")
    public ResponseEntity<PagoDTO> actualizarPago(@Valid @RequestBody PagoDTO pagoDTO) {
        return ResponseEntity.ok(pagoService.actualizarPago(pagoDTO));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DONANTE')")
    @GetMapping("/pago")
    public ResponseEntity<List<PagoDTO>> listarPagos() {
        log.info("Lista de pagos");
        return ResponseEntity.ok(pagoService.listarTodos());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DONANTE')")
    @DeleteMapping("/pago/{idPago}")
    public void eliminarPago(@PathVariable Long idPago) {
        pagoService.eliminarPago(idPago);
    }
}
