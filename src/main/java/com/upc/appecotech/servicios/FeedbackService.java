package com.upc.appecotech.servicios;

import com.upc.appecotech.dtos.FeedbackDTO;
import com.upc.appecotech.entidades.Evento;
import com.upc.appecotech.entidades.Feedback;
import com.upc.appecotech.security.entidades.Usuario;
import com.upc.appecotech.entidades.Usuarioevento;
import com.upc.appecotech.interfaces.IFeedbackService;
import com.upc.appecotech.repositorios.EventoRepositorio;
import com.upc.appecotech.repositorios.FeedbackRepositorio;
import com.upc.appecotech.repositorios.UsuarioEventoRepositorio;
import com.upc.appecotech.security.repositorios.UsuarioRepositorio;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FeedbackService implements IFeedbackService {
    @Autowired
    private FeedbackRepositorio feedbackRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private EventoRepositorio eventoRepositorio;
    @Autowired
    private UsuarioEventoRepositorio usuarioEventoRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public FeedbackDTO crearFeedback(FeedbackDTO feedbackDTO) {
        try {
            Usuario usuario = usuarioRepositorio.findById(feedbackDTO.getIdUsuario())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + feedbackDTO.getIdUsuario()));

            Evento evento = eventoRepositorio.findById(feedbackDTO.getIdEvento())
                    .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con ID: " + feedbackDTO.getIdEvento()));


            if (!validarAsistenciaEvento(usuario.getId(), evento.getId())) {
                throw new RuntimeException("El usuario no asistió a este evento");
            }

            boolean yaExisteFeedback = feedbackRepositorio.existsByUsuarioIdAndEventoId(usuario.getId(), evento.getId());
            if (yaExisteFeedback) {
                throw new RuntimeException("El usuario ya dejó feedback para este evento");
            }

            if (feedbackDTO.getPuntuacion() < 1 || feedbackDTO.getPuntuacion() > 5) {
                throw new RuntimeException("La puntuación debe estar entre 1 y 5");
            }

            if (feedbackDTO.getComentario() == null || feedbackDTO.getComentario().trim().isEmpty()) {
                throw new RuntimeException("El comentario no puede estar vacío");
            }

            Feedback feedback = new Feedback();
            feedback.setIdusuario(usuario);
            feedback.setIdevento(evento);
            feedback.setComentario(feedbackDTO.getComentario());
            feedback.setPuntuacion(feedbackDTO.getPuntuacion());
            feedback.setFecha(LocalDate.now());

            Feedback guardado = feedbackRepositorio.save(feedback);

            return modelMapper.map(guardado, FeedbackDTO.class);

        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Error al crear feedback: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean validarAsistenciaEvento(Long idUsuario, Long idEvento) {
        List<Usuarioevento> inscripciones = usuarioEventoRepositorio.findByUsuarioIdAndEventoId(idUsuario, idEvento);

        return inscripciones.stream()
                .anyMatch(ue -> ue.getAsistio() != null && ue.getAsistio());
    }

    @Override
    @Transactional
    public FeedbackDTO actualizarFeedback(Long idFeedback, FeedbackDTO feedbackDTO) {
        return feedbackRepositorio.findById(idFeedback)
                .map(feedbackExistente -> {
                    // Actualización campo por campo
                    if (feedbackDTO.getPuntuacion() != null) {
                        if (feedbackDTO.getPuntuacion() < 1 || feedbackDTO.getPuntuacion() > 5) {
                            throw new RuntimeException("La puntuación debe estar entre 1 y 5");
                        }
                        feedbackExistente.setPuntuacion(feedbackDTO.getPuntuacion());
                    }

                    if (feedbackDTO.getComentario() != null && !feedbackDTO.getComentario().trim().isEmpty()) {
                        feedbackExistente.setComentario(feedbackDTO.getComentario());
                    }

                    Feedback actualizado = feedbackRepositorio.save(feedbackExistente);
                    return modelMapper.map(actualizado, FeedbackDTO.class);
                })
                .orElseThrow(() -> new EntityNotFoundException("Feedback no encontrado con ID: " + idFeedback));
    }



    @Override
    @Transactional
    public void eliminarFeedback(Long idFeedback) {
        if (!feedbackRepositorio.existsById(idFeedback)) {
            throw new EntityNotFoundException("Feedback no encontrado con ID: " + idFeedback);
        }
        feedbackRepositorio.deleteById(idFeedback);
    }

    @Override
    @Transactional
    public FeedbackDTO buscarPorId(Long id) {
        return feedbackRepositorio.findById(id)
                .map(feedback -> modelMapper.map(feedback, FeedbackDTO.class))
                .orElse(null);
    }

    @Override
    @Transactional
    public List<FeedbackDTO> listarTodos() {
        List<Feedback> lista = feedbackRepositorio.findAll();
        return lista.stream()
                .map(feedback -> modelMapper.map(feedback, FeedbackDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public List<FeedbackDTO> listarFeedbacksPorEvento(Long idEvento) {
        eventoRepositorio.findById(idEvento)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con ID: " + idEvento));

        List<Feedback> lista = feedbackRepositorio.findByEventoId(idEvento);

        return lista.stream()
                .map(feedback -> modelMapper.map(feedback, FeedbackDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public List<FeedbackDTO> listarFeedbacksPorUsuario(Long idUsuario) {
        usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + idUsuario));

        List<Feedback> lista = feedbackRepositorio.findByUsuarioId(idUsuario);

        return lista.stream()
                .map(feedback -> modelMapper.map(feedback, FeedbackDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public Double calcularPromedioEvento(Long idEvento) {
        eventoRepositorio.findById(idEvento)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con ID: " + idEvento));

        List<Feedback> feedbacks = feedbackRepositorio.findByEventoId(idEvento);

        if (feedbacks.isEmpty()) {
            return 0.0;
        }

        double promedio = feedbacks.stream()
                .mapToInt(Feedback::getPuntuacion)
                .average()
                .orElse(0.0);

        return Math.round(promedio * 100.0) / 100.0;
    }
}
