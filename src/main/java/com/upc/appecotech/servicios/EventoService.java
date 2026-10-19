package com.upc.appecotech.servicios;

import com.upc.appecotech.dtos.EventoDTO;
import com.upc.appecotech.entidades.Evento;
import com.upc.appecotech.interfaces.IEventoService;
import com.upc.appecotech.repositorios.EventoRepositorio;
import com.upc.appecotech.repositorios.UsuarioEventoRepositorio;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Service
public class EventoService implements IEventoService {
    @Autowired
    private EventoRepositorio eventoRepositorio;

    @Autowired
    private UsuarioEventoRepositorio usuarioEventoRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    private static final List<String> ESTADOS_VALIDOS = Arrays.asList("programado", "en curso", "finalizado");
    private static final List<String> CATEGORIAS_VALIDAS = Arrays.asList("Reciclaje", "Limpieza", "Plantación", "Educación");


    @Scheduled(fixedRate = 60000)
    @Transactional
    public void actualizarEstadosEventos() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDate hoy = ahora.toLocalDate();
        LocalTime horaActual = ahora.toLocalTime();

        List<Evento> eventos = eventoRepositorio.findAll();

        for (Evento evento : eventos) {
            if (evento.getFecha() == null || evento.getHoraInicio() == null || evento.getHoraFin() == null) {
                continue;
            }

            LocalDateTime inicioEvento = LocalDateTime.of(evento.getFecha(), evento.getHoraInicio());
            LocalDateTime finEvento = LocalDateTime.of(evento.getFecha(), evento.getHoraFin());

            // Programado → En curso
            if ("programado".equals(evento.getEstadoEvento()) &&
                    ahora.isAfter(inicioEvento) && ahora.isBefore(finEvento)) {
                evento.setEstadoEvento("en curso");
                eventoRepositorio.save(evento);
            }

            // En curso → Finalizado
            if ("en curso".equals(evento.getEstadoEvento()) && ahora.isAfter(finEvento)) {
                evento.setEstadoEvento("finalizado");
                eventoRepositorio.save(evento);
            }
        }
    }

    @Override
    @Transactional
    public EventoDTO crearEvento(EventoDTO eventoDTO) {
        if (eventoDTO.getFecha().isBefore(LocalDate.now())) {
            throw new RuntimeException("La fecha del evento no puede ser anterior a hoy");
        }

        if (eventoDTO.getHoraInicio() == null || eventoDTO.getHoraFin() == null) {
            throw new RuntimeException("Hora de inicio y fin son obligatorias");
        }

        if (eventoDTO.getHoraFin().isBefore(eventoDTO.getHoraInicio())) {
            throw new RuntimeException("La hora de fin debe ser posterior a la hora de inicio");
        }

        Evento evento = modelMapper.map(eventoDTO, Evento.class);

        evento.setEstadoEvento("programado");


        if (evento.getCategoria() != null && !evento.getCategoria().isEmpty()) {
            if (!CATEGORIAS_VALIDAS.contains(evento.getCategoria())) {
                throw new IllegalArgumentException("Categoría inválida. Debe ser: Reciclaje, Limpieza, Plantación o Educación");
            }
        }


        if (evento.getCapacidadMaxima() != null && evento.getCapacidadMaxima() < 0) {
            throw new IllegalArgumentException("La capacidad máxima no puede ser negativa");
        }

        Evento guardado = eventoRepositorio.save(evento);
        return modelMapper.map(guardado, EventoDTO.class);
    }

    @Override
    @Transactional
    public EventoDTO actualizarEvento(Long id, EventoDTO eventoDTO) {
        return eventoRepositorio.findById(id)
                .map(eventoExistente -> {
                    if (eventoDTO.getNombre() != null) {
                        eventoExistente.setNombre(eventoDTO.getNombre());
                    }
                    if (eventoDTO.getFecha() != null) {
                        eventoExistente.setFecha(eventoDTO.getFecha());
                    }
                    if (eventoDTO.getLugar() != null) {
                        eventoExistente.setLugar(eventoDTO.getLugar());
                    }
                    if (eventoDTO.getDescripcion() != null) {
                        eventoExistente.setDescripcion(eventoDTO.getDescripcion());
                    }
                    if (eventoDTO.getPuntos() != null) {
                        eventoExistente.setPuntos(eventoDTO.getPuntos());
                    }

                    if (eventoDTO.getHoraInicio() != null) {
                        eventoExistente.setHoraInicio(eventoDTO.getHoraInicio());
                    }
                    if (eventoDTO.getHoraFin() != null) {
                        if (eventoDTO.getHoraInicio() != null &&
                                eventoDTO.getHoraFin().isBefore(eventoDTO.getHoraInicio())) {
                            throw new RuntimeException("La hora de fin debe ser posterior a la hora de inicio");
                        }
                        eventoExistente.setHoraFin(eventoDTO.getHoraFin());
                    }

                    if (eventoDTO.getCapacidadMaxima() != null) {
                        if (eventoDTO.getCapacidadMaxima() < 0) {
                            throw new IllegalArgumentException("La capacidad máxima no puede ser negativa");
                        }
                        eventoExistente.setCapacidadMaxima(eventoDTO.getCapacidadMaxima());
                    }

                    if (eventoDTO.getEstadoEvento() != null) {
                        if (!ESTADOS_VALIDOS.contains(eventoDTO.getEstadoEvento().toLowerCase())) {
                            throw new IllegalArgumentException("Estado inválido. Debe ser: programado, en curso o finalizado");
                        }
                        eventoExistente.setEstadoEvento(eventoDTO.getEstadoEvento());
                    }

                    if (eventoDTO.getCategoria() != null) {
                        if (!eventoDTO.getCategoria().isEmpty() && !CATEGORIAS_VALIDAS.contains(eventoDTO.getCategoria())) {
                            throw new IllegalArgumentException("Categoría inválida. Debe ser: Reciclaje, Limpieza, Plantación o Educación");
                        }
                        eventoExistente.setCategoria(eventoDTO.getCategoria());
                    }

                    if (eventoDTO.getImagenBanner() != null) {
                        eventoExistente.setImagenBanner(eventoDTO.getImagenBanner());
                    }

                    if (eventoDTO.getTipoEvento() != null) {
                        eventoExistente.setTipoEvento(eventoDTO.getTipoEvento());
                    }

                    Evento actualizado = eventoRepositorio.save(eventoExistente);
                    return modelMapper.map(actualizado, EventoDTO.class);
                })
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Integer obtenerCapacidadDisponible(Long id) {
        Evento evento = eventoRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con ID: " + id));

        if (evento.getCapacidadMaxima() == null) {
            return null;
        }

        Long inscritos = usuarioEventoRepositorio.countByIdevento_Id(id);
        return evento.getCapacidadMaxima() - inscritos.intValue();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean tieneCapacidadDisponible(Long id) {
        Integer disponible = obtenerCapacidadDisponible(id);
        return disponible == null || disponible > 0;
    }


    @Override
    @Transactional(readOnly = true)
    public EventoDTO buscarPorId(Long id) {
        return eventoRepositorio.findById(id)
                .map(evento -> modelMapper.map(evento, EventoDTO.class))
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventoDTO> listarTodos() {
        List<Evento> eventos = eventoRepositorio.findAll();
        return eventos.stream()
                .map(evento -> modelMapper.map(evento, EventoDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventoDTO> listarEventosProximos() {
        List<Evento> eventos = eventoRepositorio.findByFechaGreaterThanEqualOrderByFechaAsc(LocalDate.now());
        return eventos.stream()
                .map(evento -> modelMapper.map(evento, EventoDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventoDTO> listarEventosPasados() {
        List<Evento> eventos = eventoRepositorio.findByFechaLessThanOrderByFechaDesc(LocalDate.now());
        return eventos.stream()
                .map(evento -> modelMapper.map(evento, EventoDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public void eliminarEvento(Long id) {
        Evento evento = eventoRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con ID: " + id));
        eventoRepositorio.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventoDTO> listarPorEstado(String estadoEvento) {
        if (!ESTADOS_VALIDOS.contains(estadoEvento.toLowerCase())) {
            throw new IllegalArgumentException("Estado inválido. Debe ser: programado, en curso o finalizado");
        }
        List<Evento> eventos = eventoRepositorio.findByEstadoEvento(estadoEvento);
        return eventos.stream()
                .map(evento -> modelMapper.map(evento, EventoDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventoDTO> listarPorCategoria(String categoria) {
        if (!CATEGORIAS_VALIDAS.contains(categoria)) {
            throw new IllegalArgumentException("Categoría inválida. Debe ser: Reciclaje, Limpieza, Plantación o Educación");
        }
        List<Evento> eventos = eventoRepositorio.findByCategoria(categoria);
        return eventos.stream()
                .map(evento -> modelMapper.map(evento, EventoDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventoDTO> listarEventosProgramados() {
        List<Evento> eventos = eventoRepositorio.findEventosProgramados(LocalDate.now());
        return eventos.stream()
                .map(evento -> modelMapper.map(evento, EventoDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventoDTO> listarEventosEnCurso() {
        List<Evento> eventos = eventoRepositorio.findEventosEnCurso();
        return eventos.stream()
                .map(evento -> modelMapper.map(evento, EventoDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventoDTO> listarEventosFinalizados() {
        List<Evento> eventos = eventoRepositorio.findEventosFinalizados();
        return eventos.stream()
                .map(evento -> modelMapper.map(evento, EventoDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventoDTO> listarPorCategoriaYEstado(String categoria, String estadoEvento) {
        if (!ESTADOS_VALIDOS.contains(estadoEvento.toLowerCase())) {
            throw new IllegalArgumentException("Estado inválido. Debe ser: programado, en curso o finalizado");
        }
        if (!CATEGORIAS_VALIDAS.contains(categoria)) {
            throw new IllegalArgumentException("Categoría inválida. Debe ser: Reciclaje, Limpieza, Plantación o Educación");
        }
        List<Evento> eventos = eventoRepositorio.findByCategoriaAndEstadoEvento(categoria, estadoEvento);
        return eventos.stream()
                .map(evento -> modelMapper.map(evento, EventoDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventoDTO> listarEventosProgramadosPorCategoria(String categoria) {
        if (!CATEGORIAS_VALIDAS.contains(categoria)) {
            throw new IllegalArgumentException("Categoría inválida. Debe ser: Reciclaje, Limpieza, Plantación o Educación");
        }
        List<Evento> eventos = eventoRepositorio.findEventosProgramadosPorCategoria(categoria, LocalDate.now());
        return eventos.stream()
                .map(evento -> modelMapper.map(evento, EventoDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventoDTO> listarEventosConCapacidadDisponible() {
        List<Evento> eventos = eventoRepositorio.findEventosConCapacidadDisponible();
        return eventos.stream()
                .map(evento -> modelMapper.map(evento, EventoDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> listarCategorias() {
        return CATEGORIAS_VALIDAS;
    }

    @Override
    @Transactional
    public EventoDTO actualizarEstadoEvento(Long id, String nuevoEstado) {
        if (!ESTADOS_VALIDOS.contains(nuevoEstado.toLowerCase())) {
            throw new IllegalArgumentException("Estado inválido. Debe ser: programado, en curso o finalizado");
        }

        return eventoRepositorio.findById(id)
                .map(evento -> {
                    evento.setEstadoEvento(nuevoEstado);
                    Evento actualizado = eventoRepositorio.save(evento);
                    return modelMapper.map(actualizado, EventoDTO.class);
                })
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public EventoDTO actualizarCapacidad(Long id, Integer nuevaCapacidad) {
        if (nuevaCapacidad < 0) {
            throw new IllegalArgumentException("La capacidad no puede ser negativa");
        }

        return eventoRepositorio.findById(id)
                .map(evento -> {
                    evento.setCapacidadMaxima(nuevaCapacidad);
                    Evento actualizado = eventoRepositorio.save(evento);
                    return modelMapper.map(actualizado, EventoDTO.class);
                })
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con ID: " + id));
    }
}