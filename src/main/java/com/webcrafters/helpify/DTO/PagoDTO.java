package com.webcrafters.helpify.DTO;

import jakarta.persistence.Table;
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
@Table(name = "pago")
public class PagoDTO {
    private Long idPago;
    private BigDecimal monto;
    private LocalDate fechapago;
    private String numerotarjeta;
    private String nombretitular;
    private LocalDate fechaexpiracion;
    private String cvv;
    private DonacionSoloDatosDTO donacion;

}
