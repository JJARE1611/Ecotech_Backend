package com.upc.appecotech.controladores;


import com.upc.appecotech.dtos.ContactoDTO;
import com.upc.appecotech.interfaces.IContactoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

public class ContactoController {
    @Autowired
    private IContactoService contactoService;

    @PostMapping("/contactos")
    public ResponseEntity<?> crearContacto(@RequestBody ContactoDTO contactoDTO) {
        try {
            ContactoDTO nuevoContacto = contactoService.crearContacto(contactoDTO);
            return ResponseEntity.ok(nuevoContacto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/contactos/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {
        try {
            ContactoDTO actualizado = contactoService.actualizarEstadoContacto(id, nuevoEstado);
            return ResponseEntity.ok(actualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/contactos")
    public ResponseEntity<List<ContactoDTO>> listarTodos(){
        return ResponseEntity.ok(contactoService.listarTodos());
    }

    @GetMapping("/contactos/{id}")
    public ResponseEntity<ContactoDTO> buscarPorId(@PathVariable Long id){
        ContactoDTO contacto = contactoService.buscarPorId(id);
        if (contacto != null) {
            return ResponseEntity.ok(contacto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuarios/{idUsuario}/contactos")
    public ResponseEntity<List<ContactoDTO>> listarContactosPorUsuario(@PathVariable Long idUsuario) {
        try {
            return ResponseEntity.ok(contactoService.listarContactosPorUsuario(idUsuario));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/contactos/estado/{estado}")
    public ResponseEntity<List<ContactoDTO>> listarContactosPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(contactoService.listarContactosPorEstado(estado));
    }
}
