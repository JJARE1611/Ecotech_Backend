package com.upc.appecotech.interfaces;

import com.upc.appecotech.dtos.ContactoDTO;

import java.util.List;

public interface IContactoService {
    ContactoDTO crearContacto(ContactoDTO contactoDTO);
    ContactoDTO actualizarEstadoContacto(Long idContacto, String nuevoEstado);
    ContactoDTO buscarPorId(Long id);
    List<ContactoDTO> listarTodos();
    List<ContactoDTO> listarContactosPorUsuario(Long idUsuario);
    List<ContactoDTO> listarContactosPorEstado(String estado);
}
