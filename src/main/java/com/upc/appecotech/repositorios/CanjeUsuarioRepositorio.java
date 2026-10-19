package com.upc.appecotech.repositorios;

import com.upc.appecotech.entidades.Canjeusuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CanjeUsuarioRepositorio extends JpaRepository<Canjeusuario, Long> {

    void deleteByIdusuario_Id(Long idusuario);

    List<Canjeusuario> findByIdusuarioIdAndEstadoCanje(Long idUsuario, String estadoCanje);

    @Query("SELECT c FROM Canjeusuario c WHERE c.idusuario.id = :idUsuario ORDER BY c.fechacanje DESC")
    List<Canjeusuario> findByUsuarioId(@Param("idUsuario") Long idUsuario);

    List<Canjeusuario> findByEstadoCanje(String estadoCanje);

    @Query("SELECT c FROM Canjeusuario c WHERE c.estadoCanje = :estado ORDER BY c.fechacanje DESC")
    List<Canjeusuario> findByEstadoCanjeOrderByFechacanjeDesc(@Param("estado") String estado);

    @Query("SELECT c FROM Canjeusuario c WHERE c.metodoEntrega = :metodo")
    List<Canjeusuario> findByMetodoEntrega(@Param("metodo") String metodo);
}
