package com.upc.appecotech.repositorios;

import com.upc.appecotech.entidades.Suscripcion;
import com.upc.appecotech.security.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface SuscripcionRepositorio extends JpaRepository<Suscripcion, Long> {

    void deleteByIdusuario_Id(Long idusuario);

    @Query("SELECT s FROM Suscripcion s WHERE s.idusuario.id = :idUsuario ORDER BY s.fechainicio DESC")
    List<Suscripcion> findByUsuarioId(@Param("idUsuario") Long idUsuario);


    List<Suscripcion> findByEstado(String estado);

    @Query("SELECT s FROM Suscripcion s WHERE s.estado = 'Activa' AND s.fechafin < :fecha")
    List<Suscripcion> findSuscripcionesVencidas(@Param("fecha") LocalDate fecha);

    @Query("SELECT s FROM Suscripcion s WHERE s.idusuario.id = :idUsuario AND s.estado = 'Activa' AND s.fechafin > :fecha")
    List<Suscripcion> findSuscripcionActivaUsuario(@Param("idUsuario") Long idUsuario, @Param("fecha") LocalDate fecha);

    @Query("SELECT s FROM Suscripcion s WHERE s.tipoplan = :plan ORDER BY s.fechainicio DESC")
    List<Suscripcion> findByTipoplan(@Param("plan") String plan);

    Optional<Suscripcion> findFirstByIdusuarioAndEstadoOrderByFechainicioDesc(Usuario usuario, String estado);
}
