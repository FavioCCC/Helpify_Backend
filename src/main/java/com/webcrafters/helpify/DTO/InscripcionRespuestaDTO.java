package com.webcrafters.helpify.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InscripcionRespuestaDTO {
    private boolean exito;
    private String mensaje;
}
