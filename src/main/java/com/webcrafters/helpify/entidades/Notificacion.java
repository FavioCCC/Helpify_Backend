package com.webcrafters.helpify.entidades;

import com.webcrafters.helpify.seguridad.entidades.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "idnotificacion", nullable = false)
    private Long idnotificacion;

    @Column(name = "mensaje", nullable = false, length = 250)
    private String mensaje;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    @Column(name = "fechaenvio", nullable = false)
    private LocalDateTime fechaenvio;

    @Column(name = "leido", nullable = false)
    private Boolean leido = false;

    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "idusuario")
    @JsonBackReference("usuario-notificacion")
    private Usuario usuario;
}