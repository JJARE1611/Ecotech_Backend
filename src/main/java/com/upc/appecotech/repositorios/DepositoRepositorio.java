package com.upc.appecotech.repositorios;


import com.upc.appecotech.entidades.Deposito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public interface DepositoRepositorio extends JpaRepository<Deposito, Long> {
    @Query("SELECT d FROM Deposito d WHERE d.idusuario.id = :idUsuario ")
    List<Deposito> findByUsuarioId(@Param("idUsuario") Long idUsuario);

    void deleteByIdusuario_Id(Long idusuario);

    @Query("SELECT d.tiporesiduo, COUNT(d), SUM(d.cantidad), SUM(d.puntosotorgados) " +
            "FROM Deposito d " +
            "WHERE d.estado = 'Aprobado' " +
            "AND d.fechaenvio BETWEEN :inicio AND :fin " +
            "GROUP BY d.tiporesiduo " +
            "ORDER BY COUNT(d) DESC")
    List<Object[]> obtenerEstadisticasPorTipo(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );

    @Query("SELECT FUNCTION('TO_CHAR', d.fechaenvio, 'YYYY-MM'), " +
            "COUNT(d), SUM(d.cantidad) " +
            "FROM Deposito d " +
            "WHERE d.estado = 'Aprobado' " +
            "AND d.fechaenvio BETWEEN :inicio AND :fin " +
            "GROUP BY FUNCTION('TO_CHAR', d.fechaenvio, 'YYYY-MM') " +
            "ORDER BY FUNCTION('TO_CHAR', d.fechaenvio, 'YYYY-MM') ASC")
    List<Object[]> obtenerDepositosPorMes(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );
}
