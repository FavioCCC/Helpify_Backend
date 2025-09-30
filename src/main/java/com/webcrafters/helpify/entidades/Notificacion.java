package com.webcrafters.helpify.entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Notificacion")
public class Notificacion {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "idnotificacion", nullable = false)
    private Long idnotificacion;

    @Column(name = "mensaje", nullable = false, length = 250)
    private String mensaje;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    @Column(name = "fechaEnvio", nullable = false)
    private LocalDate fechaEnvio;

    @Column(name = "leido", nullable = false)
    private Boolean leido;

    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "idusuario")
    @JsonBackReference("usuario-notificacion")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "proyecto_id", referencedColumnName = "idproyecto")
    @JsonBackReference("proyecto-notificacion")
    private Proyecto proyecto;
}
