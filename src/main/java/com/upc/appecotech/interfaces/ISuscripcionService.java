package com.upc.appecotech.interfaces;

import com.upc.appecotech.dtos.BeneficiosDTO;
import com.upc.appecotech.dtos.SuscripcionDTO;

import java.util.List;

public interface ISuscripcionService {
    SuscripcionDTO crearSuscripcion(SuscripcionDTO suscripcionDTO);
    boolean validarSuscripcionActiva(Long idUsuario);
    SuscripcionDTO actualizarEstadoSuscripcion(Long idSuscripcion, String nuevoEstado);
    SuscripcionDTO renovarSuscripcion(Long idSuscripcion, String nuevoPlan);
    SuscripcionDTO buscarPorId(Long id);
    List<SuscripcionDTO> listarTodas();
    List<SuscripcionDTO> listarSuscripcionesPorUsuario(Long idUsuario);
    SuscripcionDTO obtenerSuscripcionActivaUsuario(Long idUsuario);
    Integer obtenerMultiplicadorPuntos(Long idUsuario);
    SuscripcionDTO cancelarCambioProgramado(Long idSuscripcion);


    SuscripcionDTO cambiarPlan(Long idUsuario, String nuevoPlan, Long idMetodoPago);
    SuscripcionDTO cancelarSuscripcion(Long idSuscripcion);
    void procesarSuscripcionesVencidas();
    BeneficiosDTO obtenerBeneficios(String tipoplan);
}
