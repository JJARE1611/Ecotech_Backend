package com.upc.appecotech.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SuscripcionDTO {
    private Long id;
    private Long idusuario;
    private Long idmetodopago;
    private String tipoplan;
    private LocalDate fechainicio;
    private LocalDate fechafin;
    private String descripcion;
    private String estado;

    // Nuevos campos
    private BigDecimal monto;
    private String proximoPlan;
    private Integer descuentoCanje;
    private Boolean accesoEventosVIP;
    private Boolean prioridadEventos;
    private Boolean contenidoExclusivo;

    // Campos calculados (no en BD)
    private Integer multiplicadorPuntos;
    private Integer diasRestantes;
}