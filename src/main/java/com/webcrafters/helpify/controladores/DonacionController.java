package com.webcrafters.helpify.controladores;

import com.webcrafters.helpify.DTO.DonacionDTO;
import com.webcrafters.helpify.DTO.DonacionSinUsuarioyProyectoDTO;
import com.webcrafters.helpify.interfaces.IDonacionService;
import com.webcrafters.helpify.seguridad.entidades.Usuario;
import com.webcrafters.helpify.seguridad.repositorios.UsuarioRepositorio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        origins = "${ip.frontend}",
        allowCredentials = "true",
        exposedHeaders = {"Authorization"}
)
@RequestMapping("/api")
public class DonacionController {

    private final IDonacionService donacionService;
    private final UsuarioRepositorio usuarioRepositorio;

    // ===== Helpers =====
    private Authentication getAuthOrThrow() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new BadCredentialsException("Usuario no autenticado");
        }
        return auth;
    }

    private Usuario getUsuarioActualOrThrow() {
        Authentication auth = getAuthOrThrow();
        String username = auth.getName();
        return usuarioRepositorio.findByNombre(username)
                .orElseThrow(() -> new BadCredentialsException("Usuario autenticado no encontrado: " + username));
    }

    // ===== Endpoints =====

    /** Crear donación: solo DONANTE, se asocia SIEMPRE al usuario autenticado. */
    @PreAuthorize("hasRole('DONANTE')")
    @PostMapping("/donacion")
    public ResponseEntity<DonacionDTO> insertarDonacion(@Valid @RequestBody DonacionDTO donacionDTO) {
        Usuario actual = getUsuarioActualOrThrow();

        // Forzar el usuario autenticado en la donación (ignorar lo que venga del cliente)
        DonacionDTO dtoForzado = new DonacionDTO();
        dtoForzado.setId(donacionDTO.getId());
        dtoForzado.setProyecto(donacionDTO.getProyecto());
        dtoForzado.setFechadonacion(donacionDTO.getFechadonacion());
        dtoForzado.setEstado(donacionDTO.getEstado());

        // Asignación de usuario en el service usando SecurityContext (mejor práctica)
        // -> No exponer el usuario aquí para evitar spoofing

        log.info("Creando donación por usuario id={} para proyecto id={}",
                actual.getIdusuario(),
                donacionDTO.getProyecto() != null ? donacionDTO.getProyecto().getIdproyecto() : "null");

        DonacionDTO creada = donacionService.insertarDonacionComoUsuarioActual(dtoForzado);
        return ResponseEntity.ok(creada);
    }

    /** Mis donaciones (solo DONANTE) */
    @PreAuthorize("hasRole('DONANTE')")
    @GetMapping("/donacion/mias")
    public ResponseEntity<List<DonacionSinUsuarioyProyectoDTO>> listarDonacionesDelUsuarioActual() {
        List<DonacionSinUsuarioyProyectoDTO> lista = donacionService.listarDonacionesDelUsuarioActual();
        return ResponseEntity.ok(lista);
    }

    /** Lista global de donaciones (solo ADMIN) */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/donaciones")
    public ResponseEntity<List<DonacionSinUsuarioyProyectoDTO>> listarDonaciones() {
        log.info("Lista de donaciones (ADMIN)");
        return ResponseEntity.ok(donacionService.listarTodos());
    }

    /**
     * Buscar donación por id:
     * - ADMIN: cualquier donación
     * - DONANTE: solo si es dueñ@ (ownership validado en el service)
     */
    @PreAuthorize("hasAnyRole('ADMIN','DONANTE')")
    @GetMapping("/donacion/{idDonacion}")
    public ResponseEntity<DonacionSinUsuarioyProyectoDTO> buscarDonacionPorId(@PathVariable Long idDonacion) {
        DonacionSinUsuarioyProyectoDTO dto = donacionService.buscarPorIdRestringiendoAUsuarioActualSiCorresponde(idDonacion);
        return ResponseEntity.ok(dto);
    }

    /** Actualizar donación: DONANTE solo sobre sus donaciones (ownership en service). */
    @PreAuthorize("hasRole('DONANTE')")
    @PutMapping("/donacion")
    public ResponseEntity<DonacionDTO> actualizarDonacion(@Valid @RequestBody DonacionDTO donacionDTO) {
        DonacionDTO actualizada = donacionService.actualizarDonacionComoUsuarioActual(donacionDTO);
        return ResponseEntity.ok(actualizada);
    }

    /** Eliminar donación: DONANTE solo si es dueñ@ (ownership en service). */
    @PreAuthorize("hasRole('DONANTE')")
    @DeleteMapping("/donacion/{id}")
    public ResponseEntity<Void> eliminarDonacion(@PathVariable Long id) {
        donacionService.eliminarDonacionComoUsuarioActual(id);
        return ResponseEntity.noContent().build();
    }
}
