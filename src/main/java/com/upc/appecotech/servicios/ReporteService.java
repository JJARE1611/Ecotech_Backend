package com.upc.appecotech.servicios;

import com.upc.appecotech.dtos.*;
import com.upc.appecotech.repositorios.*;
import com.upc.appecotech.security.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private DepositoRepositorio depositoRepositorio;

    @Autowired
    private EventoRepositorio eventoRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private HistorialPuntosRepository historialdepuntoRepositorio;

    @Autowired
    private ProductoRepositorio productoRepositorio;

    public EstadisticasGeneralesDTO obtenerEstadisticasGenerales() {
        Long totalUsuarios = usuarioRepositorio.count();
        Long totalEventos = eventoRepositorio.count();
        Long totalDepositos = depositoRepositorio.count();
        Long totalProductos = productoRepositorio.count();

        Long puntosGanados = historialdepuntoRepositorio.sumPuntosByTipo("ganancia");
        Long puntosCanjeados = historialdepuntoRepositorio.sumPuntosByTipo("Canjes");

        return new EstadisticasGeneralesDTO(
                totalUsuarios,
                totalEventos,
                totalDepositos,
                totalProductos,
                puntosGanados != null ? puntosGanados : 0L,
                puntosCanjeados != null ? puntosCanjeados : 0L
        );
    }

    public List<DepositoPorTipoDTO> obtenerDepositosPorTipo(LocalDate inicio, LocalDate fin) {
        List<Object[]> resultados = depositoRepositorio.obtenerEstadisticasPorTipo(inicio, fin);

        return resultados.stream()
                .map(r -> new DepositoPorTipoDTO(
                        (String) r[0],
                        ((Number) r[1]).longValue(),
                        (BigDecimal) r[2],
                        ((Number) r[3]).longValue()
                ))
                .collect(Collectors.toList());
    }

    public List<EventoParticipacionDTO> obtenerEventosMasParticipados(int limite) {
        List<Object[]> resultados = eventoRepositorio.obtenerEventosMasParticipados(
                PageRequest.of(0, limite)
        );

        return resultados.stream()
                .map(r -> new EventoParticipacionDTO(
                        (String) r[0],
                        ((Number) r[1]).longValue(),
                        (LocalDate) r[2]
                ))
                .collect(Collectors.toList());
    }

    public List<UsuarioTopDTO> obtenerUsuariosTop(int limite) {
        List<Object[]> resultados = historialdepuntoRepositorio.obtenerUsuariosConMasPuntos(
                PageRequest.of(0, limite)
        );

        return resultados.stream()
                .map(r -> new UsuarioTopDTO(
                        (String) r[0],
                        (String) r[1],
                        (String) r[2],
                        ((Number) r[3]).longValue()
                ))
                .collect(Collectors.toList());
    }

    public List<DepositoPorMesDTO> obtenerDepositosPorMes(LocalDate inicio, LocalDate fin) {
        List<Object[]> resultados = depositoRepositorio.obtenerDepositosPorMes(inicio, fin);

        return resultados.stream()
                .map(r -> new DepositoPorMesDTO(
                        (String) r[0],
                        ((Number) r[1]).longValue(),
                        (BigDecimal) r[2]
                ))
                .collect(Collectors.toList());
    }
}