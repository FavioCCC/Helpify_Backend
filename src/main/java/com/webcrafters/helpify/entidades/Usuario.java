package com.webcrafters.helpify.entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "idusuario", nullable = false)
    private Long idusuario;

    @Column(name = "numerodocumento", nullable = false, length = 12)
    private String numerodocumento;

    @Column(name = "nombredocumento", nullable = false)
    private String nombredocumento;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellidopaterno", nullable = false, length = 100)
    private String apellidopaterno;

    @Column(name = "apellidomaterno", nullable = false, length = 100)
    private String apellidomaterno;

    @Column(name = "celular", nullable = false, length = 9)
    private String celular;

    @Column(name = "correo", nullable = false, length = Integer.MAX_VALUE, unique = true)
    private String correo;

    @Column(name = "password", nullable = false, length = Integer.MAX_VALUE, unique = true)
    private String password;

    @Column(name= "fecharegistro", nullable = false, updatable = false)
    private LocalDateTime fecharegistro;

    @PrePersist
    protected void onCreate() {
        this.fecharegistro = LocalDateTime.now();
    }


    @ManyToOne
    @JoinColumn(name = "idrol", referencedColumnName = "idrol")
    private Rol rol;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "iduniversitario")
    @JsonBackReference("usuario-universitario")
    private Universitario universitario;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("usuario-comentario")
    private List<Comentario> comentarios = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("usuario-donacion")
    private List<Donacion> donaciones = new ArrayList<>(); // Me genera dudas
}
