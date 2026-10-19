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
public class CanjeUsuarioDTO {
    private Long id;
    private Long idUsuario;
    private Long idProducto;
    private LocalDate fechacanje;
    private Integer cantidad;
    private String estadoCanje;
    private String metodoEntrega;
    private String observaciones;
    private String direccionEntrega;
    private String ciudad;
    private String codigoPostal;
    private String telefono;
    private String referencia;

    private UsuarioDTO usuario;
    private ProductoDTO producto;
}