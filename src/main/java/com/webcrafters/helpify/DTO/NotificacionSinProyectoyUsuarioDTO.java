package com.webcrafters.helpify.DTO;

import com.webcrafters.helpify.seguridad.DTO.UsuarioSoloConDatosDTO;
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
public class NotificacionSinProyectoyUsuarioDTO {
    private Long idnotificacion;
    @Size(min = 50, max = 250, message = "El notificación del proyecto debe tener entre 50 y 250 caracteres")
    private String mensaje;
    @Size(max = 50, message = "La notificación del proyecto debe tener menos de 50 caracteres")
    private String tipo;
    private LocalDate fechaEnvio;
    private Boolean leido;
    private ProyectoSoloConDatosDTO proyecto;

    private UsuarioSoloConDatosDTO usuario;
}
