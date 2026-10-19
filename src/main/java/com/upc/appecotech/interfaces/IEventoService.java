package com.upc.appecotech.interfaces;

import com.upc.appecotech.dtos.EventoDTO;


import java.util.List;

public interface IEventoService {

    EventoDTO crearEvento(EventoDTO eventoDTO);
    EventoDTO actualizarEvento(Long id, EventoDTO eventoDTO);
    EventoDTO buscarPorId(Long id);
    List<EventoDTO> listarTodos();
    void eliminarEvento(Long id);

    List<EventoDTO> listarEventosProximos();
    List<EventoDTO> listarEventosPasados();

    List<EventoDTO> listarPorEstado(String estadoEvento);
    List<EventoDTO> listarPorCategoria(String categoria);
    List<EventoDTO> listarEventosProgramados();
    List<EventoDTO> listarEventosEnCurso();
    List<EventoDTO> listarEventosFinalizados();
    List<EventoDTO> listarPorCategoriaYEstado(String categoria, String estadoEvento);
    List<EventoDTO> listarEventosProgramadosPorCategoria(String categoria);
    List<EventoDTO> listarEventosConCapacidadDisponible();
    List<String> listarCategorias();

    EventoDTO actualizarEstadoEvento(Long id, String nuevoEstado);
    EventoDTO actualizarCapacidad(Long id, Integer nuevaCapacidad);

    boolean tieneCapacidadDisponible(Long id);
    Integer obtenerCapacidadDisponible(Long id);
}
