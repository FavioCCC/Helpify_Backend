package com.webcrafters.helpify.entidades;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "proyectos")
public class Proyecto {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "idproyecto", nullable = false)
    private Long idproyecto;

    @Column(name = "nombreproyecto", nullable = false, length = 50)
    private String nombreproyecto;

    @Column(name = "descripcion", nullable = false, length = 300)
    private String descripcion;

    @Column(name = "montoobjetivo", nullable = false)
    private double montoobjetivo;

    @Column(name = "montorecaudado", nullable = false)
    private double montorecaudado;

    @Column(name = "fechainicio", nullable = false)
    private LocalDate fechainicio;

    @Column(name = "fechafin", nullable = false)
    private LocalDate fechafin;

    @Column(name = "nombreorganizacion", length = Integer.MAX_VALUE)
    private String nombreorganizacion;

    @Column(name = "escuelabeneficiada", length = Integer.MAX_VALUE)
    private String escuelabeneficiada;

    @Column(name = "cupoMaximo")
    private Integer cupoMaximo;

    @Column(name = "cupoRestante")
    private Integer cupoRestante;

    @Column(name = "imagen", columnDefinition = "TEXT", nullable = false)
    private String imagen;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("proyecto-donacion")
    private List<Donacion> donaciones = new ArrayList<>();

}
