package com.webcrafters.helpify.entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idwishlist", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "idusuario", unique = true)
    @JsonBackReference
    private Usuario usuario;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "wishlist_proyectos",
            joinColumns = @JoinColumn(name = "idwishlist"),
            inverseJoinColumns = @JoinColumn(name = "idproyecto")
    )
    private List<Proyecto>proyectos;
}