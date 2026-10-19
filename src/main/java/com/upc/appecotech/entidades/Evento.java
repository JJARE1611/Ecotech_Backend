package com.upc.appecotech.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "evento")
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idevento", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false, length = Integer.MAX_VALUE)
    private String nombre;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Column(name = "lugar", nullable = false, length = Integer.MAX_VALUE)
    private String lugar;

    @NotNull
    @Column(name = "descripcion", nullable = false, length = Integer.MAX_VALUE)
    private String descripcion;

    @NotNull
    @Column(name = "puntos", nullable = false)
    private Integer puntos;

    @Column(name = "horaInicio")
    private LocalTime horaInicio;

    @Column(name = "horaFin")
    private LocalTime horaFin;

    @Column(name = "capacidadMaxima")
    private Integer capacidadMaxima;

    @Column(name = "estadoEvento")
    private String estadoEvento; // programado, en curso, finalizado

    @Column(name = "categoria")
    private String categoria;

    @Column(columnDefinition = "TEXT")
    private String imagenBanner;

    @Column(name = "tipo_evento", nullable = false, length = 20)
    private String tipoEvento = "publico"; // publico, premium, vip


}