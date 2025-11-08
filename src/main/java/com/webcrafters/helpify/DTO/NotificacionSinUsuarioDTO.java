package com.webcrafters.helpify.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionSinUsuarioDTO {
    @NotNull(message = "El ID de usuario destinatario es obligatorio.")
    private Long usuarioId;

    @NotBlank(message = "El mensaje no puede estar vacío.")
    @Size(max = 250, message = "El mensaje no puede exceder los 250 caracteres.")
    private String mensaje;

    @NotBlank(message = "El tipo no puede estar vacío.")
    @Size(max = 50, message = "El tipo no puede exceder los 50 caracteres.")
    private String tipo;

}