package com.webcrafters.helpify.DTO;

import com.webcrafters.helpify.seguridad.DTO.UsuarioSoloConDatosDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UniversitarioConUsuarioDTO {
    private Long iduniversitario;
    private String codigoestudiante;
    private UsuarioSoloConDatosDTO usuario;
}
