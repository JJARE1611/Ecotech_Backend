package com.upc.appecotech.interfaces;

import com.upc.appecotech.dtos.CanjeUsuarioDTO;

import java.util.List;
import java.util.Map;

public interface ICanjeusuarioService {
    CanjeUsuarioDTO canjearProducto(CanjeUsuarioDTO canjeUsuarioDTO);
    boolean validarPuntosUsuario(Long idUsuario, Long idProducto, Integer cantidad);
    CanjeUsuarioDTO buscarPorId(Long id);
    List<CanjeUsuarioDTO> listarTodos();
    List<CanjeUsuarioDTO> listarCanjesPorUsuario(Long idUsuario);
    int obtenerPuntosDisponibles(Long idUsuario);
    CanjeUsuarioDTO actualizarEstadoCanje(Long id, String nuevoEstado);
    CanjeUsuarioDTO actualizarMetodoEntrega(Long id, String nuevoMetodo);
    CanjeUsuarioDTO agregarObservaciones(Long id, String observaciones);
    List<CanjeUsuarioDTO> listarCanjesPorEstado(String estado);
    boolean permiteEntregaDigital(Long idProducto);



}
