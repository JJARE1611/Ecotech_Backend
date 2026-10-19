package com.upc.appecotech.servicios;

import com.upc.appecotech.dtos.NotificacionDTO;
import com.upc.appecotech.entidades.Notificacion;
import com.upc.appecotech.security.entidades.Usuario;
import com.upc.appecotech.repositorios.NotificacionRepositorio;
import com.upc.appecotech.security.repositorios.UsuarioRepositorio;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepositorio notificacionRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ModelMapper modelMapper;


    @Transactional
    public NotificacionDTO crearNotificacionCanje(Long idUsuario, String tipo, String titulo, String mensaje, Long idCanje) {
        Usuario usuario = usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Notificacion notificacion = new Notificacion();
        notificacion.setIdusuario(usuario);
        notificacion.setTitulo(titulo);
        notificacion.setMensaje(mensaje);
        notificacion.setTipo(tipo);
        notificacion.setIdReferencia(idCanje);
        notificacion.setLeida(false);
        notificacion.setFecha(LocalDateTime.now());
        notificacion.setIcono(obtenerIconoPorTipo(tipo));

        Notificacion guardada = notificacionRepositorio.save(notificacion);
        return modelMapper.map(guardada, NotificacionDTO.class);
    }


    @Transactional
    public List<NotificacionDTO> obtenerNotificacionesUsuario(Long idUsuario) {
        List<Notificacion> notificaciones = notificacionRepositorio.findByUsuarioIdOrderByFechaDesc(idUsuario);
        return notificaciones.stream()
                .map(n -> modelMapper.map(n, NotificacionDTO.class))
                .toList();
    }


    @Transactional
    public List<NotificacionDTO> obtenerNotificacionesNoLeidas(Long idUsuario) {
        List<Notificacion> notificaciones = notificacionRepositorio.findNoLeidasByUsuarioId(idUsuario);
        return notificaciones.stream()
                .map(n -> modelMapper.map(n, NotificacionDTO.class))
                .toList();
    }


    @Transactional
    public Long contarNotificacionesNoLeidas(Long idUsuario) {
        return notificacionRepositorio.contarNoLeidasByUsuarioId(idUsuario);
    }


    @Transactional
    public NotificacionDTO marcarComoLeida(Long idNotificacion) {
        return notificacionRepositorio.findById(idNotificacion)
                .map(notificacion -> {
                    notificacion.setLeida(true);
                    Notificacion actualizada = notificacionRepositorio.save(notificacion);
                    return modelMapper.map(actualizada, NotificacionDTO.class);
                })
                .orElseThrow(() -> new EntityNotFoundException("Notificación no encontrada"));
    }


    @Transactional
    public void marcarTodasComoLeidas(Long idUsuario) {
        List<Notificacion> notificaciones = notificacionRepositorio.findNoLeidasByUsuarioId(idUsuario);
        notificaciones.forEach(n -> n.setLeida(true));
        notificacionRepositorio.saveAll(notificaciones);
    }

    @Transactional
    public void eliminarNotificacion(Long idNotificacion) {
        notificacionRepositorio.deleteById(idNotificacion);
    }

    private String obtenerIconoPorTipo(String tipo) {
        return switch (tipo) {
            case "canje_pendiente" -> "⏳";
            case "canje_aprobado" -> "✅";
            case "canje_entregado" -> "📦";
            case "canje_rechazado" -> "❌";
            default -> "🔔";
        };
    }
}