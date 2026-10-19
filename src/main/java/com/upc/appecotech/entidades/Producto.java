package com.upc.appecotech.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idproducto", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false, length = Integer.MAX_VALUE)
    private String nombre;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Column(name = "descripcion", nullable = false, length = Integer.MAX_VALUE)
    private String descripcion;

    @NotNull
    @Column(name = "puntosrequerido", nullable = false)
    private Integer puntosrequerido;

    @NotNull
    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "precioReferencia")
    private BigDecimal precioReferencia;

    @Column(name = "categoria")
    private String categoria; // Tecnologia , Hogar, Sostenible, Accesorios

    @Column(columnDefinition = "TEXT")
    private String imagen;

    @NotNull
    @Column(name = "estado", nullable = false)
    private String estado; // activo, agotado, inactivo

    @Column(name = "permite_entrega_digital", nullable = false)
    private Boolean permiteEntregaDigital = false;

}