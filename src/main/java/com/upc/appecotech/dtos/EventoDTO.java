package com.upc.appecotech.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class EventoDTO {
    private Long id;
    private String nombre;
    private LocalDate fecha;
    private String lugar;
    private String descripcion;
    private Integer puntos;
    private Integer capacidadMaxima;
    private String estadoEvento; // programado, en curso, finalizado
    private String categoria;
    private String imagenBanner;
    private String tipoEvento;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}
