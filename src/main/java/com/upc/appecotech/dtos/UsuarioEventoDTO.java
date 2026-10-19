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

public class UsuarioEventoDTO {

    private Long id;
    private Long idusuario;
    private Long idevento;
    private LocalDate fechainscripcion;
    private Boolean asistio = false;
    private Integer puntosotorgados;
    private EventoDTO evento;
    private UsuarioDTO usuario;
}
