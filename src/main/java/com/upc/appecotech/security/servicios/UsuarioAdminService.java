package com.upc.appecotech.security.servicios;

import com.upc.appecotech.security.entidades.Usuario;
import com.upc.appecotech.security.entidades.Rol;
import com.upc.appecotech.security.repositorios.UsuarioRepositorio;
import com.upc.appecotech.security.repositorios.RolRepositorio;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioAdminService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private RolRepositorio rolRepositorio;

    public void registrarUsuario(Usuario usuario) {
        if (usuarioRepositorio.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        usuarioRepositorio.save(usuario);
    }

    public Rol crearRol(Rol rol) {
        if (rolRepositorio.existsByNombrerol(rol.getNombrerol())) {
            throw new RuntimeException("El rol ya existe");
        }
        return rolRepositorio.save(rol);
    }

    public void asignarRol(Long idUsuario, Long idRol) {
        Usuario usuario = usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        Rol rol = rolRepositorio.findById(idRol)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado"));

        usuario.getRoles().add(rol);
        usuarioRepositorio.save(usuario);
    }
}
