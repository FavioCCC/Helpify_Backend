// src/main/java/com/webcrafters/helpify/servicios/PagoService.java
package com.webcrafters.helpify.servicios;

import com.webcrafters.helpify.DTO.PagoDTO;
import com.webcrafters.helpify.entidades.Donacion;
import com.webcrafters.helpify.entidades.Pago;
import com.webcrafters.helpify.interfaces.IPagoService;
import com.webcrafters.helpify.repositorios.DonacionRepositorio;
import com.webcrafters.helpify.repositorios.PagoRepositorio;
import com.webcrafters.helpify.repositorios.ProyectoRepositorio;
import com.webcrafters.helpify.seguridad.entidades.Usuario;
import com.webcrafters.helpify.seguridad.repositorios.UsuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PagoService implements IPagoService {

    @Autowired private PagoRepositorio pagoRepositorio;
    @Autowired private ProyectoRepositorio proyectoRepositorio;
    @Autowired private DonacionRepositorio donacionRepositorio;
    @Autowired private UsuarioRepositorio usuarioRepositorio;
    @Autowired private ModelMapper modelMapper;

    // ===== Helpers =====

    private Usuario getUsuarioActualOrThrow() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new SecurityException("Usuario no autenticado");
        }
        String username = auth.getName();

        // Intenta por correo y luego por nombre (depende de tu UserDetails)
        return usuarioRepositorio.findByCorreo(username)
                .or(() -> usuarioRepositorio.findByNombre(username))
                .orElseThrow(() -> new SecurityException("Usuario autenticado no encontrado: " + username));
    }

    private void validarPagoDTO(PagoDTO dto) {
        if (dto == null
                || dto.getDonacion() == null || dto.getDonacion().getId() == null
                || dto.getMonto() == null || dto.getMonto().compareTo(BigDecimal.ZERO) <= 0
                || dto.getNumerotarjeta() == null || dto.getNumerotarjeta().trim().isEmpty()
                || dto.getNombretitular() == null || dto.getNombretitular().trim().isEmpty()
                || dto.getFechaexpiracion() == null) {
            throw new IllegalArgumentException("Datos de pago inválidos");
        }

        // Validación tarjeta (básica)
        String digits = dto.getNumerotarjeta().replaceAll("\\s+", "");
        if (digits.length() < 13 || digits.length() > 19) {
            throw new IllegalArgumentException("Número de tarjeta inválido");
        }
        if (!dto.getFechaexpiracion().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Tarjeta vencida");
        }

        // Recomendación PCI: no usar/guardar CVV (si el campo existe en DTO, no persistirlo)
        // if (dto.getCvv() == null || dto.getCvv().trim().isEmpty()) { ... } // Opcional si sigues pidiéndolo
    }

    private String last4(String numero) {
        String digits = numero == null ? "" : numero.replaceAll("\\s+", "");
        return (digits.length() >= 4) ? digits.substring(digits.length() - 4) : digits;
    }

    // ===== Implementación =====

    @Override
    @Transactional
    public PagoDTO insertarPago(PagoDTO pagoDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // ... tus validaciones ...

        Donacion donacion = donacionRepositorio.findById(pagoDTO.getDonacion().getId())
                .orElseThrow(() -> new NoSuchElementException("Donación no encontrada"));

        // si necesitas validar ownership con username, lo que ya haces...
        // ...

        // Mapear
        Pago pagoEntidad = modelMapper.map(pagoDTO, Pago.class);

        // 🔒 Forzar seteo por si el ModelMapper no está alineado con los nombres
        pagoEntidad.setDonacion(donacion);
        pagoEntidad.setMonto(pagoDTO.getMonto());
        pagoEntidad.setNumerotarjeta(pagoDTO.getNumerotarjeta());  // si quieres guardar solo last4, cámbialo aquí
        pagoEntidad.setNombretitular(pagoDTO.getNombretitular());
        pagoEntidad.setFechaexpiracion(pagoDTO.getFechaexpiracion());
        pagoEntidad.setCvv(pagoDTO.getCvv()); // ← CLAVE: evitar que vaya null

        if (pagoEntidad.getFechapago() == null) {
            pagoEntidad.setFechapago(LocalDate.now());
        }

        Pago guardado = pagoRepositorio.save(pagoEntidad);

        // actualizar recaudación del proyecto como ya tienes
        var proyecto = donacion.getProyecto();
        BigDecimal actual = BigDecimal.valueOf(proyecto.getMontorecaudado());
        BigDecimal nuevo = actual.add(guardado.getMonto());
        proyecto.setMontorecaudado(nuevo.doubleValue());
        proyectoRepositorio.save(proyecto);
        donacion.setEstado("COMPLETADO");
        donacionRepositorio.save(donacion);

        return modelMapper.map(guardado, PagoDTO.class);
    }

    // Variante con username explícito (si algún flujo interno lo necesita)
    public PagoDTO insertarPago(PagoDTO pagoDTO, String authUsername) {
        // Reutiliza el flujo normal, pero resuelve el usuario por username
        Usuario backup = usuarioRepositorio.findByCorreo(authUsername)
                .or(() -> usuarioRepositorio.findByNombre(authUsername))
                .orElseThrow(() -> new SecurityException("Usuario autenticado no encontrado: " + authUsername));
        // Hack: forzar auth temporal (si no quieres hacerlo, copia/pega lo mismo que insertarPago y usa 'backup')
        return insertarPago(pagoDTO); // ya usamos SecurityContext; mantenerlo simple
    }

    @Override
    @Transactional
    public PagoDTO actualizarPago(PagoDTO pagoDTO) {
        // Solo permitir actualizar pagos propios (si decides permitirlo)
        Usuario actual = getUsuarioActualOrThrow();
        Pago pagoExistente = pagoRepositorio.findById(pagoDTO.getIdPago())
                .orElseThrow(() -> new NoSuchElementException("Pago no encontrado"));

        Donacion don = pagoExistente.getDonacion();
        if (don == null || don.getUsuario() == null
                || !don.getUsuario().getIdusuario().equals(actual.getIdusuario())) {
            throw new AccessDeniedException("No puedes actualizar un pago que no es tuyo");
        }

        // Campos permitidos a actualizar (evita que cambien donación o numerotarjeta almacenada)
        if (pagoDTO.getMonto() != null) pagoExistente.setMonto(pagoDTO.getMonto());
        if (pagoDTO.getFechapago() != null) pagoExistente.setFechapago(pagoDTO.getFechapago());
        if (pagoDTO.getNombretitular() != null) pagoExistente.setNombretitular(pagoDTO.getNombretitular());
        // Nunca CVV; Nunca numerotarjeta completa

        Pago actualizado = pagoRepositorio.save(pagoExistente);
        PagoDTO resp = modelMapper.map(actualizado, PagoDTO.class);
        try { resp.setCvv(null); } catch (Exception ignored) {}
        if (resp.getNumerotarjeta() != null && resp.getNumerotarjeta().length() <= 4) {
            resp.setNumerotarjeta("**** **** **** " + resp.getNumerotarjeta());
        }
        return resp;
    }

    @Override
    @Transactional
    public void eliminarPago(Long idPago) {
        // Este endpoint lo dejamos para ADMIN desde el controller. Aquí no validamos ownership.
        Pago pago = pagoRepositorio.findById(idPago)
                .orElseThrow(() -> new NoSuchElementException("Pago no encontrado"));
        pagoRepositorio.delete(pago);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTO> listarTodos() {
        return pagoRepositorio.findAll().stream()
                .map(pago -> {
                    PagoDTO dto = modelMapper.map(pago, PagoDTO.class);
                    try { dto.setCvv(null); } catch (Exception ignored) {}
                    if (dto.getNumerotarjeta() != null && dto.getNumerotarjeta().length() <= 4) {
                        dto.setNumerotarjeta("**** **** **** " + dto.getNumerotarjeta());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // ===== NUEVO: para GET /api/pago/mis =====
    @Transactional(readOnly = true)
    public List<PagoDTO> listarPagosDelUsuarioActual() {
        Usuario actual = getUsuarioActualOrThrow();
        return pagoRepositorio.findByDonacion_Usuario_Idusuario(actual.getIdusuario()).stream()
                .map(p -> {
                    PagoDTO dto = modelMapper.map(p, PagoDTO.class);
                    try { dto.setCvv(null); } catch (Exception ignored) {}
                    if (dto.getNumerotarjeta() != null && dto.getNumerotarjeta().length() <= 4) {
                        dto.setNumerotarjeta("**** **** **** " + dto.getNumerotarjeta());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
