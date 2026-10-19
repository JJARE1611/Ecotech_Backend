package com.upc.appecotech.controladores;

import com.upc.appecotech.dtos.EventoDTO;
import com.upc.appecotech.interfaces.IEventoService;
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
public class EventoController {
    @Autowired
    private IEventoService eventoService;


    @PostMapping("/eventos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> crearEvento(@RequestBody EventoDTO eventoDTO) {
        try {
            EventoDTO nuevoEvento = eventoService.crearEvento(eventoDTO);
            return ResponseEntity.ok(nuevoEvento);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/eventos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarEvento(@PathVariable Long id, @RequestBody EventoDTO eventoDTO) {
        try {
            EventoDTO actualizado = eventoService.actualizarEvento(id, eventoDTO);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/eventos")
    public ResponseEntity<List<EventoDTO>> listarTodos() {
        return ResponseEntity.ok(eventoService.listarTodos());
    }

    @GetMapping("/eventos/{id}")
    public ResponseEntity<EventoDTO> buscarPorId(@PathVariable Long id) {
        EventoDTO evento = eventoService.buscarPorId(id);
        if (evento != null) {
            return ResponseEntity.ok(evento);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eventos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminarEvento(@PathVariable Long id) {
        try {
            eventoService.eliminarEvento(id);
            return ResponseEntity.ok("Evento eliminado correctamente");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @GetMapping("/eventos/proximos")
    public ResponseEntity<List<EventoDTO>> listarEventosProximos() {
        return ResponseEntity.ok(eventoService.listarEventosProximos());
    }

    @GetMapping("/eventos/pasados")
    public ResponseEntity<List<EventoDTO>> listarEventosPasados() {
        return ResponseEntity.ok(eventoService.listarEventosPasados());
    }



    @GetMapping("/eventos/categorias")
    public ResponseEntity<List<String>> listarCategorias() {
        return ResponseEntity.ok(eventoService.listarCategorias());
    }

    @GetMapping("/eventos/categoria/{categoria}")
    public ResponseEntity<List<EventoDTO>> listarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(eventoService.listarPorCategoria(categoria));
    }

    @GetMapping("/eventos/categoria/{categoria}/programados")
    public ResponseEntity<List<EventoDTO>> listarEventosProgramadosPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(eventoService.listarEventosProgramadosPorCategoria(categoria));
    }

    @GetMapping("/eventos/estado/{estadoEvento}")
    public ResponseEntity<?> listarPorEstado(@PathVariable String estadoEvento) {
        try {
            return ResponseEntity.ok(eventoService.listarPorEstado(estadoEvento));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/eventos/programados")
    public ResponseEntity<List<EventoDTO>> listarEventosProgramados() {
        return ResponseEntity.ok(eventoService.listarEventosProgramados());
    }

    @GetMapping("/eventos/en-curso")
    public ResponseEntity<List<EventoDTO>> listarEventosEnCurso() {
        return ResponseEntity.ok(eventoService.listarEventosEnCurso());
    }

    @GetMapping("/eventos/finalizados")
    public ResponseEntity<List<EventoDTO>> listarEventosFinalizados() {
        return ResponseEntity.ok(eventoService.listarEventosFinalizados());
    }

    @PutMapping("/eventos/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarEstadoEvento(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        try {
            String nuevoEstado = body.get("estadoEvento");
            if (nuevoEstado == null || nuevoEstado.isEmpty()) {
                return ResponseEntity.badRequest().body("El campo 'estadoEvento' es requerido");
            }
            EventoDTO actualizado = eventoService.actualizarEstadoEvento(id, nuevoEstado);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/eventos/con-capacidad")
    public ResponseEntity<List<EventoDTO>> listarEventosConCapacidadDisponible() {
        return ResponseEntity.ok(eventoService.listarEventosConCapacidadDisponible());
    }

    @PutMapping("/eventos/{id}/capacidad")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarCapacidad(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> body) {
        try {
            Integer nuevaCapacidad = body.get("capacidadMaxima");
            if (nuevaCapacidad == null) {
                return ResponseEntity.badRequest().body("El campo 'capacidadMaxima' es requerido");
            }
            EventoDTO actualizado = eventoService.actualizarCapacidad(id, nuevaCapacidad);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/eventos/{id}/capacidad-disponible")
    public ResponseEntity<?> obtenerCapacidadDisponible(@PathVariable Long id) {
        try {
            Integer capacidadDisponible = eventoService.obtenerCapacidadDisponible(id);
            return ResponseEntity.ok(Map.of(
                    "eventoId", id,
                    "capacidadDisponible", capacidadDisponible != null ? capacidadDisponible : "ilimitado"
            ));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/eventos/{id}/tiene-capacidad")
    public ResponseEntity<?> verificarCapacidadDisponible(@PathVariable Long id) {
        try {
            boolean tieneCapacidad = eventoService.tieneCapacidadDisponible(id);
            return ResponseEntity.ok(Map.of(
                    "eventoId", id,
                    "tieneCapacidad", tieneCapacidad
            ));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/eventos/filtrar")
    public ResponseEntity<?> filtrarEventos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String estadoEvento) {
        try {
            if (categoria != null && estadoEvento != null) {
                return ResponseEntity.ok(eventoService.listarPorCategoriaYEstado(categoria, estadoEvento));
            } else if (categoria != null) {
                return ResponseEntity.ok(eventoService.listarPorCategoria(categoria));
            } else if (estadoEvento != null) {
                return ResponseEntity.ok(eventoService.listarPorEstado(estadoEvento));
            } else {
                return ResponseEntity.ok(eventoService.listarTodos());
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}