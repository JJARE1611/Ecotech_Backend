package com.upc.appecotech.controladores;

import com.upc.appecotech.dtos.NotificacionDTO;
import com.upc.appecotech.servicios.NotificacionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping("/usuarios/{idUsuario}/notificaciones")
    public ResponseEntity<List<NotificacionDTO>> obtenerNotificaciones(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(notificacionService.obtenerNotificacionesUsuario(idUsuario));
    }

    @GetMapping("/usuarios/{idUsuario}/notificaciones/no-leidas")
    public ResponseEntity<List<NotificacionDTO>> obtenerNotificacionesNoLeidas(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(notificacionService.obtenerNotificacionesNoLeidas(idUsuario));
    }

    @GetMapping("/usuarios/{idUsuario}/notificaciones/contador")
    public ResponseEntity<Long> contarNotificacionesNoLeidas(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(notificacionService.contarNotificacionesNoLeidas(idUsuario));
    }

    @PutMapping("/notificaciones/{idNotificacion}/marcar-leida")
    public ResponseEntity<NotificacionDTO> marcarComoLeida(@PathVariable Long idNotificacion) {
        try {
            return ResponseEntity.ok(notificacionService.marcarComoLeida(idNotificacion));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/usuarios/{idUsuario}/notificaciones/marcar-todas-leidas")
    public ResponseEntity<Void> marcarTodasComoLeidas(@PathVariable Long idUsuario) {
        notificacionService.marcarTodasComoLeidas(idUsuario);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/notificaciones/{idNotificacion}")
    public ResponseEntity<Void> eliminarNotificacion(@PathVariable Long idNotificacion) {
        notificacionService.eliminarNotificacion(idNotificacion);
        return ResponseEntity.ok().build();
    }
}