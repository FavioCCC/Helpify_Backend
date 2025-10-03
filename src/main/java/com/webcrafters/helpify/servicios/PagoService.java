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
import org.springframework.stereotype.Service;

import java.util.List;
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
        if (pagoDTO.getDonacion() == null || pagoDTO.getDonacion().getId() == null) {
            throw new RuntimeException("Debe especificar la donación para el pago");
        }
        Donacion donacion = donacionRepositorio.findById(pagoDTO.getDonacion().getId())
                .orElseThrow(() -> new RuntimeException("Donación no encontrada"));

        Pago pagoEntidad = modelMapper.map(pagoDTO, Pago.class);
        pagoEntidad.setDonacion(donacion);

        Pago guardado = pagoRepositorio.save(pagoEntidad);

        // Actualizar el monto recaudado del proyecto
        var proyecto = donacion.getProyecto();
        proyecto.setMontorecaudado(proyecto.getMontorecaudado() + guardado.getMonto().doubleValue());
        proyectoRepositorio.save(proyecto); // <-- Guarda el proyecto actualizado

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
