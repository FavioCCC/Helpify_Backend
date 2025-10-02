package com.webcrafters.helpify.entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.webcrafters.helpify.seguridad.entidades.Usuario;
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
@Table(name = "comentario")
public class Comentario {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "idcomentario", nullable = false)
    private Long idcomentario;

    @Column(name = "comentario", nullable = false, length = Integer.MAX_VALUE)
    private String comentario;

    @Column(name = "estrella", nullable = false)
    private double estrella;

    @ManyToOne
    @JoinColumn(name = "idproyecto")
    @JsonBackReference("proyecto-comentario")
    private Proyecto proyecto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idusuario", nullable = false)
    @JsonBackReference("usuario-comentario")
    private Usuario usuario;
}
