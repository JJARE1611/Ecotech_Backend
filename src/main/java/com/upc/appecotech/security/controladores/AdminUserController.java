package com.upc.appecotech.security.controladores;

import com.upc.appecotech.security.entidades.Usuario;
import com.upc.appecotech.security.entidades.Rol;
import com.upc.appecotech.security.servicios.UsuarioAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    @Autowired
    private UsuarioAdminService usuarioAdminService;

    @Autowired
    private PasswordEncoder bcrypt;

    @PostMapping("/usuario")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {
        usuario.setContrasena(bcrypt.encode(usuario.getContrasena()));
        usuarioAdminService.registrarUsuario(usuario);
        return ResponseEntity.ok("Usuario registrado correctamente");
    }

    @PostMapping("/rol")
    public ResponseEntity<?> crearRol(@RequestBody Rol rol) {
        Rol nuevo = usuarioAdminService.crearRol(rol);
        return ResponseEntity.ok(nuevo);
    }

    @PostMapping("/usuario/{idUsuario}/rol/{idRol}")
    public ResponseEntity<?> asignarRol(@PathVariable Long idUsuario, @PathVariable Long idRol) {
        usuarioAdminService.asignarRol(idUsuario, idRol);
        return ResponseEntity.ok("Rol asignado correctamente");
    }
}
