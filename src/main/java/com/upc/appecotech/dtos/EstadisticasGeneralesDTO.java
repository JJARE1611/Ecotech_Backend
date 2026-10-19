package com.upc.appecotech.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class EstadisticasGeneralesDTO {
    private Long totalUsuarios;
    private Long totalEventos;
    private Long totalDepositos;
    private Long totalProductos;
    private Long puntosOtorgadosTotal;
    private Long puntosCanjeadosTotal;
}
