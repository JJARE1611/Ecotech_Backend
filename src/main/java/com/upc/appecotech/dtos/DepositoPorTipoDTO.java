package com.upc.appecotech.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class DepositoPorTipoDTO {
    private String tipo;
    private Long cantidad;
    private BigDecimal peso;
    private Long puntos;
}
