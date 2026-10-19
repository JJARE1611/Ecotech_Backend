package com.upc.appecotech.controladores;

import com.upc.appecotech.dtos.*;
import com.upc.appecotech.servicios.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class ReportesController {

    @Autowired
    private ReporteService reporteServicio;

    @GetMapping("/estadisticas-generales")
    public ResponseEntity<EstadisticasGeneralesDTO> obtenerEstadisticasGenerales() {
        return ResponseEntity.ok(reporteServicio.obtenerEstadisticasGenerales());
    }

    @GetMapping("/depositos-por-tipo")
    public ResponseEntity<List<DepositoPorTipoDTO>> obtenerDepositosPorTipo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin
    ) {
        return ResponseEntity.ok(reporteServicio.obtenerDepositosPorTipo(inicio, fin));
    }

    @GetMapping("/eventos-mas-participados")
    public ResponseEntity<List<EventoParticipacionDTO>> obtenerEventosMasParticipados(
            @RequestParam(defaultValue = "5") int limite
    ) {
        return ResponseEntity.ok(reporteServicio.obtenerEventosMasParticipados(limite));
    }

    @GetMapping("/usuarios-top")
    public ResponseEntity<List<UsuarioTopDTO>> obtenerUsuariosTop(
            @RequestParam(defaultValue = "10") int limite
    ) {
        return ResponseEntity.ok(reporteServicio.obtenerUsuariosTop(limite));
    }

    @GetMapping("/depositos-por-mes")
    public ResponseEntity<List<DepositoPorMesDTO>> obtenerDepositosPorMes(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin
    ) {
        return ResponseEntity.ok(reporteServicio.obtenerDepositosPorMes(inicio, fin));
    }
}