package com.upc.appecotech.controladores;


import com.upc.appecotech.dtos.FeedbackDTO;
import com.upc.appecotech.interfaces.IFeedbackService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

public class FeedbackController {
    @Autowired
    private IFeedbackService feedbackService;

    @PostMapping("/feedbacks")
    public ResponseEntity<?> crearFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        try {
            FeedbackDTO nuevoFeedback = feedbackService.crearFeedback(feedbackDTO);
            return ResponseEntity.ok(nuevoFeedback);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/feedbacks/{id}")
    public ResponseEntity<?> actualizarFeedback(@PathVariable Long id, @RequestBody FeedbackDTO feedbackDTO) {
        try {
            FeedbackDTO actualizado = feedbackService.actualizarFeedback(id, feedbackDTO);
            return ResponseEntity.ok(actualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/feedbacks/{id}")
    public ResponseEntity<?> eliminarFeedback(@PathVariable Long id) {
        try {
            feedbackService.eliminarFeedback(id);
            return ResponseEntity.ok("Feedback eliminado correctamente");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuarios/{idUsuario}/eventos/{idEvento}/validar-asistencia")
    public ResponseEntity<Boolean> validarAsistencia(@PathVariable Long idUsuario, @PathVariable Long idEvento) {
        boolean asistio = feedbackService.validarAsistenciaEvento(idUsuario, idEvento);
        return ResponseEntity.ok(asistio);
    }

    @GetMapping("/feedbacks")
    public ResponseEntity<List<FeedbackDTO>> listarTodos(){
        return ResponseEntity.ok(feedbackService.listarTodos());
    }

    @GetMapping("/feedbacks/{id}")
    public ResponseEntity<FeedbackDTO> buscarPorId(@PathVariable Long id){
        FeedbackDTO feedback = feedbackService.buscarPorId(id);
        if (feedback != null) {
            return ResponseEntity.ok(feedback);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/eventos/{idEvento}/feedbacks")
    public ResponseEntity<List<FeedbackDTO>> listarFeedbacksPorEvento(@PathVariable Long idEvento) {
        try {
            return ResponseEntity.ok(feedbackService.listarFeedbacksPorEvento(idEvento));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuarios/{idUsuario}/feedbacks")
    public ResponseEntity<List<FeedbackDTO>> listarFeedbacksPorUsuario(@PathVariable Long idUsuario) {
        try {
            return ResponseEntity.ok(feedbackService.listarFeedbacksPorUsuario(idUsuario));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/eventos/{idEvento}/promedio")
    public ResponseEntity<Double> calcularPromedioEvento(@PathVariable Long idEvento) {
        try {
            Double promedio = feedbackService.calcularPromedioEvento(idEvento);
            return ResponseEntity.ok(promedio);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
