package com.upc.appecotech.repositorios;

import com.upc.appecotech.entidades.Historialdepunto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface HistorialPuntosRepository extends JpaRepository<Historialdepunto, Long> {

    @Query("SELECT h FROM Historialdepunto h WHERE h.idusuario.id = :idUsuario ORDER BY h.fecha DESC")
    List<Historialdepunto> findByUsuarioId(@Param("idUsuario") Long idUsuario);

    @Query("SELECT h FROM Historialdepunto h WHERE h.idusuario.id = :idUsuario AND h.tipomovimiento = :tipo ORDER BY h.fecha DESC")
    List<Historialdepunto> findByUsuarioIdAndTipo(@Param("idUsuario") Long idUsuario, @Param("tipo") String tipo);

    @Query("SELECT h FROM Historialdepunto h WHERE h.idusuario.id = :idUsuario AND h.fecha BETWEEN :inicio AND :fin ORDER BY h.fecha DESC")
    List<Historialdepunto> findByUsuarioIdAndFechaBetween(@Param("idUsuario") Long idUsuario, @Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    void deleteByIdusuario_Id(Long idusuario);

    @Query("SELECT COALESCE(SUM(h.puntosobtenidos), 0) " +
            "FROM Historialdepunto h " +
            "WHERE h.tipomovimiento = :tipo")
    Long sumPuntosByTipo(@Param("tipo") String tipo);

    @Query("SELECT u.nombre, u.apellidos, u.email, " +
            "COALESCE(SUM(h.puntosobtenidos) - SUM(h.puntoscanjeados), 0) " +
            "FROM Usuario u " +
            "LEFT JOIN Historialdepunto h ON h.idusuario.id = u.id " +
            "GROUP BY u.id, u.nombre, u.apellidos, u.email " +
            "HAVING COALESCE(SUM(h.puntosobtenidos) - SUM(h.puntoscanjeados), 0) > 0 " +
            "ORDER BY COALESCE(SUM(h.puntosobtenidos) - SUM(h.puntoscanjeados), 0) DESC")
    List<Object[]> obtenerUsuariosConMasPuntos(Pageable pageable);
}
