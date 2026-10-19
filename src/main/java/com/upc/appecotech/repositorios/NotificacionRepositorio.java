package com.upc.appecotech.repositorios;

import com.upc.appecotech.entidades.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepositorio extends JpaRepository<Notificacion, Long> {

    @Query("SELECT n FROM Notificacion n WHERE n.idusuario.id = :idUsuario ORDER BY n.fecha DESC")
    List<Notificacion> findByUsuarioIdOrderByFechaDesc(Long idUsuario);

    @Query("SELECT n FROM Notificacion n WHERE n.idusuario.id = :idUsuario AND n.leida = false ORDER BY n.fecha DESC")
    List<Notificacion> findNoLeidasByUsuarioId(Long idUsuario);

    @Query("SELECT COUNT(n) FROM Notificacion n WHERE n.idusuario.id = :idUsuario AND n.leida = false")
    Long contarNoLeidasByUsuarioId(Long idUsuario);
}