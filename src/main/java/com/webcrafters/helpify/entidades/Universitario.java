package com.webcrafters.helpify.entidades;

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
@Table(name = "universitario")
public class Universitario {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "iduniversitario", nullable = false)
    private Long iduniversitario;

    @Column(name = "codigoestudiante", length = 20, unique = true)
    private String codigoestudiante;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario", nullable = false, unique = true)
    private Usuario usuario; // cambio de idusuario a usuario
}
