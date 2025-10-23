// borradorHelpify/src/main/java/com/upc/borradorhelpify/servicios/PagoService.java
package com.webcrafters.helpify.servicios;

import com.webcrafters.helpify.DTO.PagoDTO;
import com.webcrafters.helpify.entidades.Donacion;
import com.webcrafters.helpify.entidades.Pago;
import com.webcrafters.helpify.interfaces.IPagoService;
import com.webcrafters.helpify.repositorios.DonacionRepositorio;
import com.webcrafters.helpify.repositorios.PagoRepositorio;
import com.webcrafters.helpify.repositorios.ProyectoRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PagoService implements IPagoService {
    @Autowired
    private PagoRepositorio pagoRepositorio;

    @Autowired
    private ProyectoRepositorio proyectoRepositorio;

    @Autowired
    private DonacionRepositorio donacionRepositorio;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PagoDTO insertarPago(PagoDTO pagoDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new SecurityException("Usuario no autenticado");
        }
        String username = authentication.getName();
        return insertarPago(pagoDTO, username);
    }

    // Método auxiliar
    public PagoDTO insertarPago(PagoDTO pagoDTO, String authUsername) {
        // Validaciones básicas
        if (pagoDTO == null
                || pagoDTO.getDonacion() == null || pagoDTO.getDonacion().getId() == null
                || pagoDTO.getMonto() == null || pagoDTO.getMonto().compareTo(BigDecimal.ZERO) <= 0
                || pagoDTO.getNumerotarjeta() == null || pagoDTO.getNumerotarjeta().trim().isEmpty()
                || pagoDTO.getNombretitular() == null || pagoDTO.getNombretitular().trim().isEmpty()
                || pagoDTO.getFechaexpiracion() == null
                || pagoDTO.getCvv() == null || pagoDTO.getCvv().trim().isEmpty()) {
            throw new IllegalArgumentException("Datos de pago inválidos");
        }

        // Buscar donación
        Donacion donacion = donacionRepositorio.findById(pagoDTO.getDonacion().getId())
                .orElseThrow(() -> new NoSuchElementException("Donación no encontrada"));

        // Verificar que la donación pertenezca al usuario autenticado
        if (donacion.getUsuario() == null || donacion.getUsuario().getNombre() == null
                || !donacion.getUsuario().getNombre().equalsIgnoreCase(authUsername)) {
            throw new SecurityException("El usuario autenticado no coincide con el titular de la donación");
        }

        // Mapear y preparar entidad Pago
        Pago pagoEntidad = modelMapper.map(pagoDTO, Pago.class);
        pagoEntidad.setDonacion(donacion);
        // si el DTO no trae fechapago, usar la fecha actual
        if (pagoEntidad.getFechapago() == null) {
            pagoEntidad.setFechapago(LocalDate.now());
        }

        Pago guardado = pagoRepositorio.save(pagoEntidad);

        var proyecto = donacion.getProyecto();
        // proyecto.getMontorecaudado() es un double primitivo: no compararlo con null
        BigDecimal actual = BigDecimal.valueOf(proyecto.getMontorecaudado());
        BigDecimal nuevo = actual.add(guardado.getMonto());
        proyecto.setMontorecaudado(nuevo.doubleValue());
        proyectoRepositorio.save(proyecto);

        return modelMapper.map(guardado, PagoDTO.class);
    }

    @Override
    public PagoDTO actualizarPago(PagoDTO pagoDTO) {
        Pago pagoExistente = pagoRepositorio.findById(pagoDTO.getIdPago())
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        modelMapper.map(pagoDTO, pagoExistente);
        Pago actualizado = pagoRepositorio.save(pagoExistente);
        return modelMapper.map(actualizado, PagoDTO.class);
    }

    @Override
    public void eliminarPago(Long idPago) {
        Pago pago = pagoRepositorio.findById(idPago)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        pagoRepositorio.delete(pago);
    }

    @Override
    public List<PagoDTO> listarTodos() {
        return pagoRepositorio.findAll().stream()
                .map(pago -> modelMapper.map(pago, PagoDTO.class))
                .collect(Collectors.toList());
    }
}
