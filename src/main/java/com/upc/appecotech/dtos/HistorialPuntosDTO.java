package com.upc.appecotech.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class HistorialPuntosDTO {
    private Long id;
    private Long idusuario;
    private Integer puntosobtenidos;
    private String tipomovimiento;
    private String descripcion;
    private Integer puntoscanjeados;
    private LocalDate fecha;
}
