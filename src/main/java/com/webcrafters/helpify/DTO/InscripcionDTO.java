package com.webcrafters.helpify.DTO;

import com.webcrafters.helpify.entidades.Proyecto;
import com.webcrafters.helpify.entidades.Universitario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InscripcionDTO {
    private Long id;
    private String fechaRegistro;
    //private UniversitarioDTO idUniversitario;
    //private ProyectoDTO idProyecto;


    private Universitario universitario;

    private Proyecto proyecto;
}