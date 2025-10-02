package com.webcrafters.helpify.seguridad.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rol")
public class Rol {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "idrol", nullable = false)
    private Long idrol;

    @Column(name = "nombre", nullable = false, length = 15)
    private String nombre;
}
