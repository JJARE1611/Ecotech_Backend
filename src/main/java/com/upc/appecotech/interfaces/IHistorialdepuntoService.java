package com.upc.appecotech.interfaces;

import com.upc.appecotech.dtos.HistorialPuntosDTO;

import java.time.LocalDate;
import java.util.List;

public interface IHistorialdepuntoService {
    HistorialPuntosDTO registrarMovimiento(HistorialPuntosDTO historialDTO);
    List<HistorialPuntosDTO> obtenerHistorialUsuario(Long idUsuario);
    List<HistorialPuntosDTO> obtenerHistorialPorTipo(Long idUsuario, String tipoMovimiento);
    Integer calcularPuntosDisponibles(Long idUsuario);
    List<HistorialPuntosDTO> obtenerHistorialPorFechas(Long idUsuario, LocalDate inicio, LocalDate fin);

}
