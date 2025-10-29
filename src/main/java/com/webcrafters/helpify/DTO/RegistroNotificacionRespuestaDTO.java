package com.webcrafters.helpify.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistroNotificacionRespuestaDTO {
    private String mensaje;
    private NotificacionSinProyectoyUsuarioDTO notificacion;
}
