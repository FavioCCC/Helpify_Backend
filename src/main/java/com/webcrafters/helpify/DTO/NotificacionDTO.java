package com.webcrafters.helpify.DTO;

import com.webcrafters.helpify.seguridad.DTO.UsuarioSoloConDatosDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionDTO {
    private Long idnotificacion;
    @NotBlank(message = "La notificación del proyecto no puede estar vacío")
    @NotNull
    private String mensaje;
    private String tipo;
    private LocalDateTime fechaEnvio; // O LocalDate, dependiendo de tu Entidad final
    private Boolean leido;
    private UsuarioSoloConDatosDTO usuario;
}