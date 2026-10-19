package com.upc.appecotech.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class DepositoDTO {
    private Long id;
    private Long idUsuario;
    private String ubicacion;
    private LocalDate fechaenvio;
    private String descripcion;
    private String tiporesiduo;
    private String ubicaciondeposito;
    private String pruebas;
    private BigDecimal cantidad;
    private Integer puntosotorgados;
    private String estado;
    private UsuarioDTO usuario;
}
