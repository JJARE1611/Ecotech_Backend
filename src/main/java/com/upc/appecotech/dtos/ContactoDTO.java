package com.upc.appecotech.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class ContactoDTO {
    private Long id;
    private Long idUsuario;
    private LocalDate fecha;
    private String descripcionProblema;
    private String tipoReclamo;
    private String estado;
}
