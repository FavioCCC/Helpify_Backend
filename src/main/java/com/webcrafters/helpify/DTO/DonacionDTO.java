package com.webcrafters.helpify.DTO;

import com.webcrafters.helpify.seguridad.DTO.UsuarioSoloConDatosDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DonacionDTO {
    private Long id;
    private LocalDate fechadonacion;
    private String estado;

    private UsuarioSoloConDatosDTO usuario;
    private ProyectoSoloConDatosDTO proyecto;
    private PagoSoloConDatosDTO pago;
}
