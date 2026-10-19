package com.upc.appecotech.controladores;

import com.upc.appecotech.dtos.CanjeUsuarioDTO;
import com.upc.appecotech.interfaces.ICanjeusuarioService;
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
public class CanjeusuarioController {
    @Autowired
    private ICanjeusuarioService canjeusuarioService;

    @PostMapping("/canjes")
    public ResponseEntity<?> canjearProducto(@RequestBody CanjeUsuarioDTO canjeUsuarioDTO) {
        try {
            CanjeUsuarioDTO nuevoCanje = canjeusuarioService.canjearProducto(canjeUsuarioDTO);
            return ResponseEntity.ok(nuevoCanje);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usuarios/{idUsuario}/validar-puntos")
    public ResponseEntity<Boolean> validarPuntosUsuario(
            @PathVariable Long idUsuario,
            @RequestParam Long idProducto,
            @RequestParam Integer cantidad) {
        try {
            boolean tienePuntos = canjeusuarioService.validarPuntosUsuario(idUsuario, idProducto, cantidad);
            return ResponseEntity.ok(tienePuntos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/canjes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CanjeUsuarioDTO>> listarTodos() {
        return ResponseEntity.ok(canjeusuarioService.listarTodos());
    }

    @GetMapping("/canjes/{id}")
    public ResponseEntity<CanjeUsuarioDTO> buscarPorId(@PathVariable Long id) {
        CanjeUsuarioDTO canje = canjeusuarioService.buscarPorId(id);
        if (canje != null) {
            return ResponseEntity.ok(canje);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuarios/{idUsuario}/canjes")
    public ResponseEntity<List<CanjeUsuarioDTO>> listarCanjesPorUsuario(@PathVariable Long idUsuario) {
        try {
            return ResponseEntity.ok(canjeusuarioService.listarCanjesPorUsuario(idUsuario));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuarios/{idUsuario}/puntos")
    public ResponseEntity<Integer> obtenerPuntosDisponibles(@PathVariable Long idUsuario) {
        try {
            int puntosDisponibles = canjeusuarioService.obtenerPuntosDisponibles(idUsuario);
            return ResponseEntity.ok(puntosDisponibles);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ========== NUEVOS ENDPOINTS ==========

    @GetMapping("/canjes/estado/{estado}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listarCanjesPorEstado(@PathVariable String estado) {
        try {
            return ResponseEntity.ok(canjeusuarioService.listarCanjesPorEstado(estado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/canjes/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarEstadoCanje(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        try {
            String nuevoEstado = body.get("estadoCanje");
            if (nuevoEstado == null || nuevoEstado.isEmpty()) {
                return ResponseEntity.badRequest().body("El campo 'estadoCanje' es requerido");
            }
            CanjeUsuarioDTO actualizado = canjeusuarioService.actualizarEstadoCanje(id, nuevoEstado);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/canjes/{id}/metodo-entrega")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarMetodoEntrega(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        try {
            String nuevoMetodo = body.get("metodoEntrega");
            if (nuevoMetodo == null || nuevoMetodo.isEmpty()) {
                return ResponseEntity.badRequest().body("El campo 'metodoEntrega' es requerido");
            }
            CanjeUsuarioDTO actualizado = canjeusuarioService.actualizarMetodoEntrega(id, nuevoMetodo);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/canjes/{id}/observaciones")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> agregarObservaciones(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        try {
            String observaciones = body.get("observaciones");
            CanjeUsuarioDTO actualizado = canjeusuarioService.agregarObservaciones(id, observaciones);
            return ResponseEntity.ok(actualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/productos/{idProducto}/permite-entrega-digital")
    public ResponseEntity<Boolean> permiteEntregaDigital(@PathVariable Long idProducto) {
        try {
            boolean permite = canjeusuarioService.permiteEntregaDigital(idProducto);
            return ResponseEntity.ok(permite);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}