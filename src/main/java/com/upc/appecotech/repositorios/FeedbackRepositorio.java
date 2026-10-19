package com.upc.appecotech.repositorios;

import com.upc.appecotech.entidades.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface FeedbackRepositorio extends JpaRepository<Feedback, Long> {
    @Query("SELECT f FROM Feedback f WHERE f.idevento.id = :idEvento")
    List<Feedback> findByEventoId(@Param("idEvento") Long idEvento);

    @Query("SELECT f FROM Feedback f WHERE f.idusuario.id = :idUsuario")
    List<Feedback> findByUsuarioId(@Param("idUsuario") Long idUsuario);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Feedback f WHERE f.idusuario.id = :idUsuario AND f.idevento.id = :idEvento")
    boolean existsByUsuarioIdAndEventoId(@Param("idUsuario") Long idUsuario, @Param("idEvento") Long idEvento);

    void deleteByIdusuario_Id(Long idusuario);
}
