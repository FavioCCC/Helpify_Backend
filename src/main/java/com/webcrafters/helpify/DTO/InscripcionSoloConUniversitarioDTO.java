package com.webcrafters.helpify.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InscripcionSoloConUniversitarioDTO {
    private Long id;
    private LocalDateTime fecharegistro;
    private UniversitarioConUsuarioDTO universitario;
}
