package com.webcrafters.helpify.controladores;

import com.webcrafters.helpify.DTO.PagoDTO;
import com.webcrafters.helpify.DTO.RegistroPagoRespuestaDTO;
import com.webcrafters.helpify.interfaces.IPagoService;
import com.webcrafters.helpify.seguridad.repositorios.UsuarioRepositorio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@CrossOrigin(
        origins = "http://localhost:4200",
        allowCredentials = "true",
        exposedHeaders = {"Authorization", "Mensaje"}
)
@RequestMapping("/api")
public class PagoController {

    private final IPagoService pagoService;
    private final UsuarioRepositorio usuarioRepositorio;

    // ===== Helpers =====
    private Authentication getAuthOrThrow() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new BadCredentialsException("Usuario no autenticado");
        }
        return auth;
    }

    private void sanitizePagoDTO(PagoDTO dto) {
        if (dto == null) return;
        // Nunca devolver CVV
        dto.setCvv(null);
        // Enmascarar número de tarjeta
        String num = dto.getNumerotarjeta();
        if (num != null) {
            String digits = num.replaceAll("\\s+", "");
            if (digits.length() > 4) {
                dto.setNumerotarjeta("**** **** **** " + digits.substring(digits.length() - 4));
            } else {
                dto.setNumerotarjeta("****");
            }
        }
    }

    // ===== Endpoints =====

    /** Crear pago (solo DONANTE). Ownership se valida en el service. */
    @PreAuthorize("hasRole('DONANTE')")
    @PostMapping("/pago")
    public ResponseEntity<RegistroPagoRespuestaDTO> insertarPago(@Valid @RequestBody PagoDTO pagoDTO) {
        Authentication auth = getAuthOrThrow();
        String username = auth.getName();

        log.info("Registrando pago por usuario {} para donacionId {}",
                username,
                pagoDTO.getDonacion() != null ? pagoDTO.getDonacion().getId() : "null");

        // Importante: NO validar que el titular = username (no siempre coincide).
        // El service debe validar que la donación pertenece al usuario autenticado.

        PagoDTO creado = pagoService.insertarPago(pagoDTO);

        // Sanitizar datos sensibles en la respuesta
        sanitizePagoDTO(creado);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Mensaje", "¡Gracias por tu contribución! Tu donación fue registrada correctamente.");

        RegistroPagoRespuestaDTO respuesta = new RegistroPagoRespuestaDTO(
                "¡Gracias por tu contribución! Tu donación fue registrada correctamente.",
                creado
        );
        return new ResponseEntity<>(respuesta, headers, HttpStatus.CREATED);
    }

    /** Actualizar pago (si mantienes este flujo): DONANTE puede actualizar SOLO si es dueño (service debe validar). */
    @PreAuthorize("hasRole('DONANTE')")
    @PutMapping("/pago")
    public ResponseEntity<PagoDTO> actualizarPago(@Valid @RequestBody PagoDTO pagoDTO) {
        PagoDTO actualizado = pagoService.actualizarPago(pagoDTO);
        sanitizePagoDTO(actualizado);
        return ResponseEntity.ok(actualizado);
    }

    /** Mis pagos (solo DONANTE): devuelve únicamente los pagos del usuario autenticado. */
    @PreAuthorize("hasRole('DONANTE')")
    @GetMapping("/pago/mis")
    public ResponseEntity<List<PagoDTO>> listarPagosDelUsuarioActual() {
        List<PagoDTO> list = pagoService.listarPagosDelUsuarioActual();
        list.forEach(this::sanitizePagoDTO);
        return ResponseEntity.ok(list);
    }

    /** Lista global (solo ADMIN). */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pago")
    public ResponseEntity<List<PagoDTO>> listarPagos() {
        log.info("Lista de pagos (ADMIN)");
        List<PagoDTO> list = pagoService.listarTodos();
        list.forEach(this::sanitizePagoDTO);
        return ResponseEntity.ok(list);
    }

    /** Eliminar pago: restringido a ADMIN. Si quieres permitir anulación del donante, crea otro endpoint con ownership check. */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/pago/{idPago}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long idPago) {
        pagoService.eliminarPago(idPago);
        return ResponseEntity.noContent().build();
    }
}
