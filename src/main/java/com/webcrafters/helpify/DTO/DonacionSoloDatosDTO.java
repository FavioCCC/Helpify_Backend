package com.webcrafters.helpify.DTO;

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
public class DonacionSoloDatosDTO {
    private Long id;
    private LocalDate fechadonacion;
    private String estado;

    private PagoSoloConDatosDTO pago;
}
