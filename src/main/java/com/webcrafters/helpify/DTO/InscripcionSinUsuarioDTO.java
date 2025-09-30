package com.webcrafters.helpify.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InscripcionSinUsuarioDTO {
    private Long id;
    private String fechaRegistro;
    private UniversitarioSinUsuarioDTO universitario;
    private ProyectoSoloConDatosDTO proyecto;//se puede cambiar a ProyectoSinUsuarioDTO si es necesario

}
