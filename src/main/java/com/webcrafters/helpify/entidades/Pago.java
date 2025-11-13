package com.webcrafters.helpify.entidades;

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
@Table(name = "pago")
public class Pago {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "idpago", nullable = false)
    private Long idPago;


    @Column(name = "monto", nullable = false, precision = 5, scale = 2)
    private BigDecimal monto;

    @Column(name = "fechapago", nullable = false)
    private LocalDate fechapago;

    @Column(name = "numerotarjeta", nullable = false)
    private String numerotarjeta;

    @Column(name = "nombretitular", nullable = false, length = 30)
    private String nombretitular;

    @Column(name = "fechaexpiracion", nullable = false)
    private LocalDate fechaexpiracion;

    @Column(nullable = false, name = "cvv")
    private String cvv;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_donacion", nullable = false)
    private Donacion donacion;

}