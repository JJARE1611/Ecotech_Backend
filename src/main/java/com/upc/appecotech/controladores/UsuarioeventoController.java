package com.upc.appecotech.controladores;

import com.upc.appecotech.dtos.EventoDTO;
import com.upc.appecotech.dtos.UsuarioEventoDTO;
import com.upc.appecotech.entidades.Evento;
import com.upc.appecotech.interfaces.IUsuarioEventoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

public class UsuarioeventoController {

    @Autowired
    private IUsuarioEventoService usuarioEventoService;

    @PostMapping("/usuario-eventos")
    public ResponseEntity<?> registrarUsuarioEvento(@RequestBody UsuarioEventoDTO usuarioEventoDTO) {
        try {
            UsuarioEventoDTO nuevoUsuarioEvento = usuarioEventoService.registrarUsuarioEvento(usuarioEventoDTO);
            return ResponseEntity.ok(nuevoUsuarioEvento);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/usuario-eventos/{id}")
    public ResponseEntity<?> cancelarInscripcion(@PathVariable Long id) {
        try {
            usuarioEventoService.cancelarInscripcion(id);
            return ResponseEntity.ok(Map.of("mensaje", "Inscripción cancelada exitosamente"));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/usuario-eventos/{idUsuarioEvento}/asistencia")
    public ResponseEntity<?> marcarAsistencia(@PathVariable Long idUsuarioEvento, @RequestParam boolean asistio) {
        try {
            UsuarioEventoDTO actualizado = usuarioEventoService.marcarAsistencia(idUsuarioEvento, asistio);
            return ResponseEntity.ok(actualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario-eventos")
    public ResponseEntity<List<UsuarioEventoDTO>> listarTodos(){
        return ResponseEntity.ok(usuarioEventoService.listarTodos());
    }

    @GetMapping("/usuario-eventos/{id}")
    public ResponseEntity<UsuarioEventoDTO> buscarPorId(@PathVariable Long id){
        UsuarioEventoDTO usuarioEvento = usuarioEventoService.buscarPorId(id);
        if (usuarioEvento != null) {
            return ResponseEntity.ok(usuarioEvento);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/eventos/{idEvento}/inscritos")
    public ResponseEntity<List<UsuarioEventoDTO>> listarInscritosPorEvento(@PathVariable Long idEvento) {
        try {
            return ResponseEntity.ok(usuarioEventoService.listarInscritosPorEvento(idEvento));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/usuarios/{idUsuario}/eventos")
    public ResponseEntity<List<UsuarioEventoDTO>> listarEventosPorUsuario(@PathVariable Long idUsuario) {
        try {
            return ResponseEntity.ok(usuarioEventoService.listarEventosPorUsuario(idUsuario));
        }catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // En UsuarioeventoController.java
    @GetMapping("/usuarios/{idUsuario}/eventos-disponibles")
    public ResponseEntity<?> listarEventosDisponibles(@PathVariable Long idUsuario) {
        try {
            List<EventoDTO> eventosDisponibles = usuarioEventoService.listarEventosDisponiblesParaUsuario(idUsuario);
            return ResponseEntity.ok(eventosDisponibles);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }




}
