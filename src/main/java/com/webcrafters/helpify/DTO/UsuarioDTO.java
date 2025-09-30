package com.webcrafters.helpify.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private Long idusuario;

    @NotBlank(message = "El número de documento no puede estar vacío")
    @Pattern(regexp = "\\d{8,12}", message = "El número de documento debe contener entre 8 y 12 dígitos")
    private String numerodocumento;

    @NotBlank(message = "El nombre del documento de identidad no puede estar vacío")
    @Pattern(regexp = "DNI|RUC|CE", message = "El documento debe ser DNI, RUC o CE")
    private String nombredocumento;

    @NotNull(message = "El nombre no puede ser nulo")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @NotNull(message = "El nombre no puede ser nulo")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String apellidopaterno;

    @NotNull(message = "El nombre no puede ser nulo")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String apellidomaterno;

    @Pattern(regexp = "\\d{9}", message = "El celular debe tener 9 dígitos")
    private String celular;

    @Email(message = "El email debe ser válido")
    private String correo;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, max = 20, message = "La contraseña debe tener entre 8 y 20 caracteres")
    private String password;

    private LocalDateTime fecharegistro;

    private Long idRol; //solo el id del rol
}
