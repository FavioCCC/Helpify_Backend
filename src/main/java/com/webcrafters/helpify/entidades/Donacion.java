package com.webcrafters.helpify.entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.webcrafters.helpify.seguridad.entidades.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "donacion")
public class Donacion {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "iddonacion", nullable = false)
    private Long id;

    @Column(name = "fechadonacion", nullable = false)
    private LocalDate fechadonacion;

    @Column(name = "estado", nullable = false, length = 10) // la longitud 10 puede cambiarse si se ve conveniente
    private String estado;

    @ManyToOne
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario")
    @JsonBackReference("usuario-donacion")
    //@JsonIgnore
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idproyecto", nullable = false) //@JsonBackReference
    @JsonBackReference("proyecto-donacion")
    private Proyecto proyecto;
}
