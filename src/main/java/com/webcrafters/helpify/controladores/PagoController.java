package com.webcrafters.helpify.controladores;


import com.webcrafters.helpify.DTO.PagoDTO;
import com.webcrafters.helpify.interfaces.IPagoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200",
        allowCredentials = "true",
        exposedHeaders = "Authorization")
@RequestMapping("/api")
public class PagoController {
    @Autowired
    private IPagoService pagoService;
    @PostMapping("/pago")
    public ResponseEntity<PagoDTO> insertarPago(@Valid @RequestBody PagoDTO pagoDTO) {
        log.info("Registrando pago");
        return ResponseEntity.ok(pagoService.insertarPago(pagoDTO));
    }

    @PutMapping("/pago")
    public ResponseEntity<PagoDTO> actualizarPago(@Valid @RequestBody PagoDTO pagoDTO) {
        return ResponseEntity.ok(pagoService.actualizarPago(pagoDTO));
    }

    @GetMapping("/pago")
    public ResponseEntity<List<PagoDTO>> listarPagos() {
        log.info("Lista de pagos");
        return ResponseEntity.ok(pagoService.listarTodos());
    }

    @DeleteMapping("/pago/{idPago}")
    public void eliminarPago(@PathVariable Long idPago) {
        pagoService.eliminarPago(idPago);
    }
}
