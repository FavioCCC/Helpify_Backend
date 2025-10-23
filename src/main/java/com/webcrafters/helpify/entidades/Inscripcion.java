package com.webcrafters.helpify.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inscripcion",
uniqueConstraints = @UniqueConstraint(columnNames = {"iduniversitario","idproyecto"})
)
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idinscripcion", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "iduniversitario")
    private Universitario universitario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idproyecto")
    private Proyecto proyecto;

    @Column(name= "fecharegistro", nullable = false, updatable = false)
    private LocalDateTime fecharegistro;

}