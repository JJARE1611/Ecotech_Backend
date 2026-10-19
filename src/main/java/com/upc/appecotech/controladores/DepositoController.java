package com.upc.appecotech.controladores;

import com.upc.appecotech.dtos.DepositoDTO;
import com.upc.appecotech.interfaces.IDepositoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

public class DepositoController {
    @Autowired
    private IDepositoService depositoService;

    @PostMapping("/depositos")
    public ResponseEntity<?> registrarDeposito(@RequestBody DepositoDTO depositoDTO) {
        try {
            DepositoDTO nuevoDeposito = depositoService.registrarDeposito(depositoDTO);
            return ResponseEntity.ok(nuevoDeposito);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/depositos/{id}")
    public ResponseEntity<?> actualizarDeposito(@PathVariable Long id, @RequestBody DepositoDTO depositoDTO) {
        try {
            DepositoDTO actualizado = depositoService.actualizarDeposito(id, depositoDTO);
            return ResponseEntity.ok(actualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/depositos")
    public ResponseEntity<List<DepositoDTO>> findAll(){
        return ResponseEntity.ok(depositoService.findAll());
    }


    @GetMapping("/depositos/{id}")
    public ResponseEntity<DepositoDTO> buscarPorId(@PathVariable Long id){
        DepositoDTO deposito = depositoService.buscarPorId(id);
        if (deposito != null) {
            return ResponseEntity.ok(deposito);
        }
        return ResponseEntity.notFound().build();
    }



    @PutMapping("/depositos/{id}/validar")
    public ResponseEntity<?> validarDeposito(@PathVariable Long id, @RequestParam boolean aprobado){
        try {
            DepositoDTO validado = depositoService.validarDeposito(id, aprobado);
            return ResponseEntity.ok(validado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/usuarios/{idUsuario}/depositos")
    public ResponseEntity<List<DepositoDTO>> listarDepositosPorUsuario(@PathVariable Long idUsuario) {
        try {
            return ResponseEntity.ok(depositoService.listarDepositosPorUsuario(idUsuario));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
