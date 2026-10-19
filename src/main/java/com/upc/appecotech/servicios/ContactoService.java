package com.upc.appecotech.servicios;

import com.upc.appecotech.dtos.ContactoDTO;
import com.upc.appecotech.entidades.Contacto;
import com.upc.appecotech.security.entidades.Usuario;
import com.upc.appecotech.interfaces.IContactoService;
import com.upc.appecotech.repositorios.ContactoRepositorio;
import com.upc.appecotech.security.repositorios.UsuarioRepositorio;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContactoService implements IContactoService {
    @Autowired
    private ContactoRepositorio contactoRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public ContactoDTO crearContacto(ContactoDTO contactoDTO) {
        try {
            Usuario usuario = usuarioRepositorio.findById(contactoDTO.getIdUsuario())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + contactoDTO.getIdUsuario()));

            if (contactoDTO.getDescripcionProblema() == null || contactoDTO.getDescripcionProblema().trim().isEmpty()) {
                throw new RuntimeException("La descripción del problema no puede estar vacía");
            }

            Contacto contacto = new Contacto();
            contacto.setIdusuario(usuario);
            contacto.setDescripcionproblema(contactoDTO.getDescripcionProblema());
            contacto.setTiporeclamo(contactoDTO.getTipoReclamo());
            contacto.setFecha(LocalDate.now());
            contacto.setEstado("Pendiente");

            Contacto guardado = contactoRepositorio.save(contacto);

            return modelMapper.map(guardado, ContactoDTO.class);

        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Error al crear contacto: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ContactoDTO actualizarEstadoContacto(Long idContacto, String nuevoEstado) {
        return contactoRepositorio.findById(idContacto)
                .map(contacto -> {
                    contacto.setEstado(nuevoEstado);
                    Contacto actualizado = contactoRepositorio.save(contacto);
                    return modelMapper.map(actualizado, ContactoDTO.class);
                })
                .orElseThrow(() -> new EntityNotFoundException("Contacto no encontrado con ID: " + idContacto));
    }

    @Override
    @Transactional
    public ContactoDTO buscarPorId(Long id) {
        return contactoRepositorio.findById(id)
                .map(contacto -> modelMapper.map(contacto, ContactoDTO.class))
                .orElse(null);
    }

    @Override
    @Transactional
    public List<ContactoDTO> listarTodos() {
        List<Contacto> lista = contactoRepositorio.findAll();
        return lista.stream()
                .map(contacto -> modelMapper.map(contacto, ContactoDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public List<ContactoDTO> listarContactosPorUsuario(Long idUsuario) {
        usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + idUsuario));

        List<Contacto> lista = contactoRepositorio.findByUsuarioId(idUsuario);

        return lista.stream()
                .map(contacto -> modelMapper.map(contacto, ContactoDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public List<ContactoDTO> listarContactosPorEstado(String estado) {
        List<Contacto> lista = contactoRepositorio.findByEstado(estado);
        return lista.stream()
                .map(contacto -> modelMapper.map(contacto, ContactoDTO.class))
                .toList();
    }
}
