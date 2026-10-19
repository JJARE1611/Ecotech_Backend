package com.upc.appecotech.servicios;

import com.upc.appecotech.dtos.EventoDTO;
import com.upc.appecotech.dtos.SuscripcionDTO;
import com.upc.appecotech.dtos.UsuarioDTO;
import com.upc.appecotech.dtos.UsuarioEventoDTO;
import com.upc.appecotech.entidades.Evento;
import com.upc.appecotech.entidades.Historialdepunto;
import com.upc.appecotech.entidades.Usuarioevento;
import com.upc.appecotech.security.entidades.Usuario;
import com.upc.appecotech.interfaces.ISuscripcionService;
import com.upc.appecotech.interfaces.IUsuarioEventoService;
import com.upc.appecotech.repositorios.EventoRepositorio;
import com.upc.appecotech.repositorios.HistorialPuntosRepository;
import com.upc.appecotech.repositorios.UsuarioEventoRepositorio;
import com.upc.appecotech.security.repositorios.UsuarioRepositorio;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioEventoService implements IUsuarioEventoService {
    @Autowired
    private UsuarioEventoRepositorio usuarioEventoRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private EventoRepositorio eventoRepositorio;
    @Autowired
    private HistorialPuntosRepository historialPuntosRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ISuscripcionService suscripcionService;

    @Override
    @Transactional
    public UsuarioEventoDTO registrarUsuarioEvento(UsuarioEventoDTO usuarioEventoDTO) {
        try {
            Usuario usuario = usuarioRepositorio.findById(usuarioEventoDTO.getIdusuario())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + usuarioEventoDTO.getIdusuario()));

            Evento evento = eventoRepositorio.findById(usuarioEventoDTO.getIdevento())
                    .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con ID: " + usuarioEventoDTO.getIdevento()));

            if (!"programado".equals(evento.getEstadoEvento())) {
                throw new RuntimeException("Solo puedes inscribirte a eventos en estado 'programado'. " +
                        "Estado actual: " + evento.getEstadoEvento());
            }


            if (evento.getCapacidadMaxima() != null) {
                Long inscritos = usuarioEventoRepositorio.countByIdevento_Id(evento.getId());
                if (inscritos >= evento.getCapacidadMaxima()) {
                    throw new RuntimeException("El evento ha alcanzado su capacidad máxima");
                }
            }

            validarAccesoEvento(usuario.getId(), evento);

            boolean yaInscrito = usuarioEventoRepositorio.existsByIdusuario_IdAndIdevento_Id(
                    usuarioEventoDTO.getIdusuario(), usuarioEventoDTO.getIdevento());

            if (yaInscrito) {
                throw new RuntimeException("El usuario ya está inscrito en este evento");
            }

            Usuarioevento usuarioEvento = new Usuarioevento();
            usuarioEvento.setIdusuario(usuario);
            usuarioEvento.setIdevento(evento);
            usuarioEvento.setFechainscripcion(LocalDate.now());
            usuarioEvento.setAsistio(false);
            usuarioEvento.setPuntosotorgados(0);

            Usuarioevento guardado = usuarioEventoRepositorio.save(usuarioEvento);

            return convertirADTO(guardado);

        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Error al registrar inscripción: " + e.getMessage());
        }
    }

    private void validarAccesoEvento(Long idUsuario, Evento evento) {
        SuscripcionDTO suscripcion = suscripcionService.obtenerSuscripcionActivaUsuario(idUsuario);
        String planUsuario = suscripcion != null ? suscripcion.getTipoplan() : "Basico";
        String tipoEvento = evento.getTipoEvento();

        if (tipoEvento == null) return;

        switch (tipoEvento.toLowerCase()) {
            case "vip":
                if (!"VIP".equals(planUsuario)) {
                    throw new RuntimeException("Este es un evento exclusivo VIP. Actualiza tu plan para acceder.");
                }
                break;

            case "premium":
                if ("Basico".equals(planUsuario)) {
                    throw new RuntimeException("Este evento requiere plan Premium o VIP. Actualiza tu plan para acceder.");
                }
                break;

            case "publico":
                break;

            default:
                break;
        }
    }

    @Override
    @Transactional
    public UsuarioEventoDTO marcarAsistencia(Long idUsuarioEvento, boolean asistio) {
        Usuarioevento usuarioevento = usuarioEventoRepositorio.findById(idUsuarioEvento)
                .orElseThrow(() -> new EntityNotFoundException("Inscripción no encontrada con ID: " + idUsuarioEvento));

        usuarioevento.setAsistio(asistio);

        if (asistio) {
            Integer multiplicador = suscripcionService.obtenerMultiplicadorPuntos(
                    usuarioevento.getIdusuario().getId()
            );

            int puntosBase = usuarioevento.getIdevento().getPuntos();
            int puntosConMultiplicador = puntosBase * multiplicador;

            usuarioevento.setPuntosotorgados(puntosConMultiplicador);

            Historialdepunto historialdepunto = new Historialdepunto();
            historialdepunto.setIdusuario(usuarioevento.getIdusuario());
            historialdepunto.setPuntosobtenidos(puntosConMultiplicador);
            historialdepunto.setFecha(LocalDate.now());
            historialdepunto.setTipomovimiento("Evento");
            historialdepunto.setDescripcion(
                    String.format("Participación en evento: %s (x%d plan %s)",
                            usuarioevento.getIdevento().getNombre(),
                            multiplicador,
                            obtenerNombrePlan(usuarioevento.getIdusuario().getId()))
            );
            historialdepunto.setPuntoscanjeados(0);

            historialPuntosRepository.save(historialdepunto);
        } else {
            usuarioevento.setPuntosotorgados(0);
        }

        Usuarioevento actualizado = usuarioEventoRepositorio.save(usuarioevento);
        return convertirADTO(actualizado);
    }

    private String obtenerNombrePlan(Long idUsuario) {
        SuscripcionDTO suscripcion = suscripcionService.obtenerSuscripcionActivaUsuario(idUsuario);
        return suscripcion != null ? suscripcion.getTipoplan() : "Básico";
    }

    @Override
    @Transactional
    public List<UsuarioEventoDTO> listarInscritosPorEvento(Long idEvento) {
        eventoRepositorio.findById(idEvento)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con ID: " + idEvento));

        List<Usuarioevento> lista = usuarioEventoRepositorio.findByIdevento_Id(idEvento);

        return lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UsuarioEventoDTO buscarPorId(Long id) {
        return usuarioEventoRepositorio.findById(id)
                .map(this::convertirADTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public List<UsuarioEventoDTO> listarTodos() {
        List<Usuarioevento> lista = usuarioEventoRepositorio.findAll();
        return lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UsuarioEventoDTO> listarEventosPorUsuario(Long idUsuario) {
        usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + idUsuario));
        List<Usuarioevento> lista = usuarioEventoRepositorio.findByIdusuario_Id(idUsuario);
        return lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventoDTO> listarEventosDisponiblesParaUsuario(Long idUsuario) {
        SuscripcionDTO suscripcion = suscripcionService.obtenerSuscripcionActivaUsuario(idUsuario);
        String planUsuario = suscripcion != null ? suscripcion.getTipoplan() : "Basico";

        List<Evento> todosLosEventos = eventoRepositorio.findAll();

        return todosLosEventos.stream()
                .filter(evento -> puedeAccederEvento(planUsuario, evento.getTipoEvento()))
                .map(evento -> modelMapper.map(evento, EventoDTO.class))
                .collect(Collectors.toList());
    }

    private boolean puedeAccederEvento(String planUsuario, String tipoEvento) {
        if (tipoEvento == null) return true;

        return switch (tipoEvento.toLowerCase()) {
            case "vip" -> "VIP".equals(planUsuario);
            case "premium" -> "Premium".equals(planUsuario) || "VIP".equals(planUsuario);
            case "publico" -> true;
            default -> true;
        };
    }

    private UsuarioEventoDTO convertirADTO(Usuarioevento entidad) {
        UsuarioEventoDTO dto = new UsuarioEventoDTO();
        dto.setId(entidad.getId());
        dto.setIdusuario(entidad.getIdusuario().getId());
        dto.setIdevento(entidad.getIdevento().getId());
        dto.setFechainscripcion(entidad.getFechainscripcion());
        dto.setAsistio(entidad.getAsistio());
        dto.setPuntosotorgados(entidad.getPuntosotorgados());

        if (entidad.getIdevento() != null) {
            dto.setEvento(modelMapper.map(entidad.getIdevento(), EventoDTO.class));
        }

        if (entidad.getIdusuario() != null) {
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setId(entidad.getIdusuario().getId());
            usuarioDTO.setNombre(entidad.getIdusuario().getNombre());
            usuarioDTO.setEmail(entidad.getIdusuario().getEmail());
            usuarioDTO.setTelefono(entidad.getIdusuario().getTelefono());
            dto.setUsuario(usuarioDTO);
        }

        return dto;
    }

    @Override
    @Transactional
    public void cancelarInscripcion(Long idInscripcion) {
        Usuarioevento inscripcion = usuarioEventoRepositorio.findById(idInscripcion)
                .orElseThrow(() -> new EntityNotFoundException("Inscripción no encontrada con ID: " + idInscripcion));

        if (!"programado".equalsIgnoreCase(inscripcion.getIdevento().getEstadoEvento())) {
            throw new RuntimeException("Solo puedes cancelar inscripciones de eventos programados. " +
                    "Estado actual del evento: " + inscripcion.getIdevento().getEstadoEvento());
        }

        if (inscripcion.getAsistio()) {
            throw new RuntimeException("No puedes cancelar una inscripción donde ya se marcó asistencia");
        }

        usuarioEventoRepositorio.deleteById(idInscripcion);
    }
}