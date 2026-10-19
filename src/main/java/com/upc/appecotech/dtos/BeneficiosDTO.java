package com.upc.appecotech.dtos;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BeneficiosDTO {
    private String tipoplan;
    private Integer multiplicadorPuntos;
    private Integer descuentoCanje;
    private Boolean accesoEventosVIP;
    private Boolean prioridadEventos;
    private Boolean contenidoExclusivo;
    private String descripcionBeneficios;
}
