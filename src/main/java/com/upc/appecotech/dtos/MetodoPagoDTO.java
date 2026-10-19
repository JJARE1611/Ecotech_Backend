package com.upc.appecotech.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class MetodoPagoDTO {
    private Long id;
    private String nombremetodo;
}
