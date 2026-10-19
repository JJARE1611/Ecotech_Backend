package com.upc.appecotech.controladores;

import com.upc.appecotech.dtos.HistorialPuntosDTO;
import com.upc.appecotech.interfaces.IHistorialdepuntoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

public class HistorialdepuntoController {
    @Autowired
    private IHistorialdepuntoService historialPuntosService;

    @GetMapping("/usuarios/{idUsuario}/historial-puntos")
    public ResponseEntity<List<HistorialPuntosDTO>> obtenerHistorial(@PathVariable Long idUsuario) {
        try {
            return ResponseEntity.ok(historialPuntosService.obtenerHistorialUsuario(idUsuario));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuarios/{idUsuario}/historial-puntos/tipo/{tipo}")
    public ResponseEntity<List<HistorialPuntosDTO>> obtenerHistorialPorTipo(
            @PathVariable Long idUsuario,
            @PathVariable String tipo) {
        try {
            return ResponseEntity.ok(historialPuntosService.obtenerHistorialPorTipo(idUsuario, tipo));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuarios/{idUsuario}/puntos-disponibles")
    public ResponseEntity<Integer> obtenerPuntosDisponibles(@PathVariable Long idUsuario) {
        try {
            Integer puntos = historialPuntosService.calcularPuntosDisponibles(idUsuario);
            return ResponseEntity.ok(puntos);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuarios/{idUsuario}/historial-puntos/rango")
    public ResponseEntity<List<HistorialPuntosDTO>> obtenerHistorialPorFechas(
            @PathVariable Long idUsuario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        try {
            return ResponseEntity.ok(historialPuntosService.obtenerHistorialPorFechas(idUsuario, inicio, fin));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
