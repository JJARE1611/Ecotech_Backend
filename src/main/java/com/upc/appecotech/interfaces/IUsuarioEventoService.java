package com.upc.appecotech.interfaces;

import com.upc.appecotech.dtos.EventoDTO;
import com.upc.appecotech.dtos.UsuarioEventoDTO;

import java.util.List;

public interface IUsuarioEventoService {
    UsuarioEventoDTO registrarUsuarioEvento(UsuarioEventoDTO usuarioEventoDTO);
    UsuarioEventoDTO marcarAsistencia(Long idUsuarioEvento, boolean asistio);
    List<UsuarioEventoDTO> listarInscritosPorEvento(Long idEvento);
    UsuarioEventoDTO buscarPorId(Long id);
    List<UsuarioEventoDTO> listarTodos();
    List<UsuarioEventoDTO> listarEventosPorUsuario(Long idUsuario);
    List<EventoDTO> listarEventosDisponiblesParaUsuario(Long idUsuario);
    void cancelarInscripcion(Long idInscripcion);
}
