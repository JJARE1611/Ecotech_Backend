package com.upc.appecotech.interfaces;

import com.upc.appecotech.dtos.FeedbackDTO;

import java.util.List;

public interface IFeedbackService {
    FeedbackDTO crearFeedback(FeedbackDTO feedbackDTO);
    boolean validarAsistenciaEvento(Long idUsuario, Long idEvento);
    FeedbackDTO actualizarFeedback(Long idFeedback, FeedbackDTO feedbackDTO);
    void eliminarFeedback(Long idFeedback);
    FeedbackDTO buscarPorId(Long id);
    List<FeedbackDTO> listarTodos();
    List<FeedbackDTO> listarFeedbacksPorEvento(Long idEvento);
    List<FeedbackDTO> listarFeedbacksPorUsuario(Long idUsuario);
    Double calcularPromedioEvento(Long idEvento);
}
