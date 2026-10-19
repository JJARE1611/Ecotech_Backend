package com.upc.appecotech.servicios;

import com.upc.appecotech.dtos.HistorialPuntosDTO;
import com.upc.appecotech.entidades.Historialdepunto;
import com.upc.appecotech.security.entidades.Usuario;
import com.upc.appecotech.interfaces.IHistorialdepuntoService;
import com.upc.appecotech.repositorios.HistorialPuntosRepository;
import com.upc.appecotech.security.repositorios.UsuarioRepositorio;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HistorialPuntosService implements IHistorialdepuntoService {
    @Autowired
    private HistorialPuntosRepository historialPuntosRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Override
    @Transactional
    public HistorialPuntosDTO registrarMovimiento(HistorialPuntosDTO historialDTO) {
        try {
            Usuario usuario = usuarioRepositorio.findById(historialDTO.getIdusuario())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + historialDTO.getIdusuario()));

            Historialdepunto historial = new Historialdepunto();
            historial.setIdusuario(usuario);
            historial.setPuntosobtenidos(historialDTO.getPuntosobtenidos());
            historial.setPuntoscanjeados(historialDTO.getPuntoscanjeados());
            historial.setTipomovimiento(historialDTO.getTipomovimiento());
            historial.setDescripcion(historialDTO.getDescripcion());
            historial.setFecha(LocalDate.now());

            Historialdepunto guardado = historialPuntosRepository.save(historial);

            return modelMapper.map(guardado, HistorialPuntosDTO.class);

        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Error al registrar movimiento: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public List<HistorialPuntosDTO> obtenerHistorialUsuario(Long idUsuario) {
        usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + idUsuario));

        List<Historialdepunto> historial = historialPuntosRepository.findByUsuarioId(idUsuario);

        return historial.stream()
                .map(h -> modelMapper.map(h, HistorialPuntosDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public List<HistorialPuntosDTO> obtenerHistorialPorTipo(Long idUsuario, String tipoMovimiento) {
        usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + idUsuario));

        List<Historialdepunto> historial = historialPuntosRepository.findByUsuarioIdAndTipo(idUsuario, tipoMovimiento);

        return historial.stream()
                .map(h -> modelMapper.map(h, HistorialPuntosDTO.class))
                .toList();

    }

    @Override
    @Transactional
    public Integer calcularPuntosDisponibles(Long idUsuario) {
        usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + idUsuario));

        List<Historialdepunto> historial = historialPuntosRepository.findByUsuarioId(idUsuario);

        int puntosObtenidos = historial.stream()
                .mapToInt(Historialdepunto::getPuntosobtenidos)
                .sum();

        int puntosCanjeados = historial.stream()
                .mapToInt(Historialdepunto::getPuntoscanjeados)
                .sum();

        return puntosObtenidos - puntosCanjeados;
    }

    @Override
    @Transactional
    public List<HistorialPuntosDTO> obtenerHistorialPorFechas(Long idUsuario, LocalDate inicio, LocalDate fin) {
        usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + idUsuario));

        List<Historialdepunto> historial = historialPuntosRepository.findByUsuarioIdAndFechaBetween(idUsuario, inicio, fin);

        return historial.stream()
                .map(h -> modelMapper.map(h, HistorialPuntosDTO.class))
                .toList();
    }
}
