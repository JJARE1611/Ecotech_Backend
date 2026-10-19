package com.upc.appecotech.controladores;

import com.upc.appecotech.dtos.BeneficiosDTO;
import com.upc.appecotech.dtos.SuscripcionDTO;
import com.upc.appecotech.interfaces.ISuscripcionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class SuscripcionController {
    @Autowired
    private ISuscripcionService suscripcionService;


    @PostMapping("/suscripciones")
    public ResponseEntity<?> crearSuscripcion(@RequestBody SuscripcionDTO suscripcionDTO) {
        try {
            SuscripcionDTO nuevaSuscripcion = suscripcionService.crearSuscripcion(suscripcionDTO);
            return ResponseEntity.ok(nuevaSuscripcion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/suscripciones/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        try {
            String nuevoEstado = body.get("estado");
            if (nuevoEstado == null || nuevoEstado.isEmpty()) {
                return ResponseEntity.badRequest().body("El campo 'estado' es requerido");
            }
            SuscripcionDTO actualizada = suscripcionService.actualizarEstadoSuscripcion(id, nuevoEstado);
            return ResponseEntity.ok(actualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/suscripciones/{id}/renovar")
    public ResponseEntity<?> renovarSuscripcion(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        try {
            String nuevoPlan = body.get("nuevoPlan");
            if (nuevoPlan == null || nuevoPlan.isEmpty()) {
                return ResponseEntity.badRequest().body("El campo 'nuevoPlan' es requerido");
            }
            SuscripcionDTO renovada = suscripcionService.renovarSuscripcion(id, nuevoPlan);
            return ResponseEntity.ok(renovada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuarios/{idUsuario}/suscripcion-activa")
    public ResponseEntity<Boolean> validarSuscripcionActiva(@PathVariable Long idUsuario) {
        boolean tieneActiva = suscripcionService.validarSuscripcionActiva(idUsuario);
        return ResponseEntity.ok(tieneActiva);
    }

    @GetMapping("/suscripciones")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SuscripcionDTO>> listarTodas() {
        return ResponseEntity.ok(suscripcionService.listarTodas());
    }

    @GetMapping("/suscripciones/{id}")
    public ResponseEntity<SuscripcionDTO> buscarPorId(@PathVariable Long id) {
        SuscripcionDTO suscripcion = suscripcionService.buscarPorId(id);
        if (suscripcion != null) {
            return ResponseEntity.ok(suscripcion);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuarios/{idUsuario}/suscripciones")
    public ResponseEntity<List<SuscripcionDTO>> listarSuscripcionesPorUsuario(@PathVariable Long idUsuario) {
        try {
            return ResponseEntity.ok(suscripcionService.listarSuscripcionesPorUsuario(idUsuario));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuarios/{idUsuario}/suscripcion-actual")
    public ResponseEntity<SuscripcionDTO> obtenerSuscripcionActual(@PathVariable Long idUsuario) {
        SuscripcionDTO suscripcion = suscripcionService.obtenerSuscripcionActivaUsuario(idUsuario);
        if (suscripcion != null) {
            return ResponseEntity.ok(suscripcion);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuarios/{idUsuario}/multiplicador-puntos")
    public ResponseEntity<Integer> obtenerMultiplicadorPuntos(@PathVariable Long idUsuario) {
        Integer multiplicador = suscripcionService.obtenerMultiplicadorPuntos(idUsuario);
        return ResponseEntity.ok(multiplicador);
    }



    @PostMapping("/usuarios/{idUsuario}/cambiar-plan")
    public ResponseEntity<?> cambiarPlan(
            @PathVariable Long idUsuario,
            @RequestBody Map<String, Object> body) {
        try {
            String nuevoPlan = (String) body.get("nuevoPlan");
            Long idMetodoPago = body.get("idMetodoPago") != null
                    ? Long.valueOf(body.get("idMetodoPago").toString())
                    : null;

            if (nuevoPlan == null || nuevoPlan.isEmpty()) {
                return ResponseEntity.badRequest().body("El campo 'nuevoPlan' es requerido");
            }
            if (idMetodoPago == null) {
                return ResponseEntity.badRequest().body("El campo 'idMetodoPago' es requerido");
            }

            SuscripcionDTO suscripcion = suscripcionService.cambiarPlan(idUsuario, nuevoPlan, idMetodoPago);
            return ResponseEntity.ok(suscripcion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/suscripciones/{id}/cancelar")
    public ResponseEntity<?> cancelarSuscripcion(@PathVariable Long id) {
        try {
            SuscripcionDTO suscripcion = suscripcionService.cancelarSuscripcion(id);
            return ResponseEntity.ok(suscripcion);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/suscripciones/procesar-vencidas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> procesarVencidas() {
        try {
            suscripcionService.procesarSuscripcionesVencidas();
            return ResponseEntity.ok("Suscripciones procesadas correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al procesar: " + e.getMessage());
        }
    }


    @GetMapping("/planes/{tipoplan}/beneficios")
    public ResponseEntity<?> obtenerBeneficios(@PathVariable String tipoplan) {
        try {
            BeneficiosDTO beneficios = suscripcionService.obtenerBeneficios(tipoplan);
            return ResponseEntity.ok(beneficios);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener beneficios");
        }
    }


    @GetMapping("/planes")
    public ResponseEntity<?> listarPlanes() {
        try {
            List<BeneficiosDTO> planes = List.of(
                    suscripcionService.obtenerBeneficios("Basico"),
                    suscripcionService.obtenerBeneficios("Premium"),
                    suscripcionService.obtenerBeneficios("VIP")
            );
            return ResponseEntity.ok(planes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al listar planes");
        }
    }

    @PutMapping("/suscripciones/{id}/cancelar-cambio")
    public ResponseEntity<?> cancelarCambioProgramado(@PathVariable Long id) {
        try {
            SuscripcionDTO suscripcion = suscripcionService.cancelarCambioProgramado(id);
            return ResponseEntity.ok(suscripcion);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/suscripciones/{id}/cancelar-completo")
    public ResponseEntity<?> cancelarCompleto(@PathVariable Long id) {
        try {
            SuscripcionDTO suscripcion = suscripcionService.cancelarSuscripcion(id);
            return ResponseEntity.ok(suscripcion);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuarios/{idUsuario}/beneficios")
    public ResponseEntity<?> obtenerBeneficios(@PathVariable Long idUsuario) {
        try {
            SuscripcionDTO suscripcion = suscripcionService.obtenerSuscripcionActivaUsuario(idUsuario);

            Map<String, Object> beneficios = new HashMap<>();
            beneficios.put("plan", suscripcion != null ? suscripcion.getTipoplan() : "Basico");
            beneficios.put("multiplicadorPuntos", suscripcionService.obtenerMultiplicadorPuntos(idUsuario));
            beneficios.put("descuentoCanjes", suscripcion != null ?
                    (suscripcion.getTipoplan().equals("Premium") ? 15 :
                            suscripcion.getTipoplan().equals("VIP") ? 30 : 0) : 0);

            return ResponseEntity.ok(beneficios);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}