package com.upc.appecotech.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ProductoDTO {
    private Long id;
    private String nombre;
    private LocalDate fecha;
    private String descripcion;
    private Integer puntosrequerido;
    private Integer stock;
    private BigDecimal precioReferencia;
    private String categoria;
    private String imagen;
    private String estado; // activo, agotado, inactivo
    private Boolean permiteEntregaDigital;
}
