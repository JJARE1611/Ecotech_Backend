package com.upc.appecotech.interfaces;


import com.upc.appecotech.dtos.SuscripcionDTO;
import com.upc.appecotech.dtos.UsuarioDTO;

import java.util.List;

public interface IUsuarioService {
    UsuarioDTO registrarUsuario(UsuarioDTO usuarioDTO);
    UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO);
    UsuarioDTO buscarPorId(Long id);
    List<UsuarioDTO> listarUsuarios();
    void eliminarUsuario(Long id);
    UsuarioDTO buscarPorEmail(String email);
    void cambiarContrasena(Long id, String contrasenaActual, String contrasenaNueva);
}
