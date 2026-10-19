package com.upc.appecotech.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionDTO {
    private Long id;
    private Long idUsuario;
    private String titulo;
    private String mensaje;
    private String tipo;
    private Long idReferencia;
    private Boolean leida;
    private LocalDateTime fecha;
    private String icono;
}