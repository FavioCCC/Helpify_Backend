package com.webcrafters.helpify.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProyectoSoloConDatosDTO {
    private Long idproyecto;

    @NotBlank(message = "El nombre del proyecto no puede estar vacío")
    @Size(min = 10, max = 50, message = "El nombre del proyecto debe tener entre 10 y 50 caracteres")
    private String nombreproyecto;

    @NotBlank(message = "La descripción del proyecto no puede estar vacía")
    @Size(min = 50, max = 300, message = "La descripción del proyecto debe tener entre 50 y 300 caracteres")
    private String descripcion;

    @NotNull(message = "El monto objetivo del proyecto no puede estar vacío")
    @Positive(message = "El monto objetivo debe ser mayor que 0")
    private double montoobjetivo;

    @NotNull(message = "El monto recaudado del proyecto no puede estar vacío")
    @Positive(message = "El monto recaudado debe ser mayor que 0")
    private double montorecaudado;

    @NotNull(message = "La fecha de inicio del proyecto no puede ser nula")
    private LocalDate fechainicio;

    @NotNull(message = "La fecha de finalización del proyecto no puede ser nula")
    private LocalDate fechafin;

    @NotBlank(message = "El nombre de la organización no puede estar vacío")
    private String nombreorganizacion;

    @NotBlank(message = "El nombre de la escuela beneficiada no puede estar vacío")
    private String escuelabeneficiada;

    private int cupoMaximo;
    private int cupoRestante;

    private String imagen;
}
