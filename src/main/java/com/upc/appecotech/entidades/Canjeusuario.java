package com.upc.appecotech.entidades;

import com.upc.appecotech.security.entidades.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "canjeusuario")
public class Canjeusuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcanjeusuario", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idproducto", nullable = false)
    private Producto idproducto;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idusuario", nullable = false)
    private Usuario idusuario;

    @NotNull
    @Column(name = "fechacanje", nullable = false)
    private LocalDate fechacanje;

    @NotNull
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "estadoCanje")
    private String estadoCanje; // pendiente, aprobado, entregado, rechazado

    @Column(name = "metodoEntrega")
    private String metodoEntrega; // digital, físico, retiro en tienda

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "direccionEntrega", length = 300)
    private String direccionEntrega;

    @Column(name = "ciudad", length = 100)
    private String ciudad;

    @Column(name = "codigoPostal", length = 20)
    private String codigoPostal;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "referencia", length = 200)
    private String referencia;

}