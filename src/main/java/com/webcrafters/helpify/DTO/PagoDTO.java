package com.webcrafters.helpify.DTO;

import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull(message = "El idPago es obligatorio")
    private Long idPago;

    @NotNull(message = "El monto es obligatorio")
    private BigDecimal monto;

    @NotNull(message = "La fecha de pago es obligatoria")
    private LocalDate fechapago;

    @NotBlank(message = "El número de tarjeta es obligatorio")
    private String numerotarjeta;

    @NotBlank(message = "El nombre del titular es obligatorio")
    private String nombretitular;

    @NotNull(message = "La fecha de expiración es obligatoria")
    private LocalDate fechaexpiracion;

    @NotBlank(message = "El CVV es obligatorio")
    @Size(min = 3, max = 3, message = "Datos de tarjeta inválidos. Verifique la información e intente nuevamente")
    private String cvv;
    private DonacionSoloDatosDTO donacion;

}
