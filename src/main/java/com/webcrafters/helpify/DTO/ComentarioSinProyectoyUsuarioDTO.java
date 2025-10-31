package com.webcrafters.helpify.DTO;

import com.webcrafters.helpify.seguridad.DTO.UsuarioSoloConDatosDTO;
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
public class ComentarioSinProyectoyUsuarioDTO {
    private Long idcomentario;

    @NotBlank(message = "El comentario del proyecto no puede estar vacío")
    @Size(min = 50, max = 500, message = "El comentario del proyecto debe tener entre 50 y 500 caracteres")
    private String comentario;

    @NotNull(message = "El puntaje en estrellas del proyecto no puede estar vacío")
    private double estrella;

    private UsuarioSoloConDatosDTO usuario;
}
