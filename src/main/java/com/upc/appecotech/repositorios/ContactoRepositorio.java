package com.upc.appecotech.repositorios;

import com.upc.appecotech.entidades.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactoRepositorio extends JpaRepository<Contacto, Long> {
    @Query("SELECT c FROM Contacto c WHERE c.idusuario.id = :idUsuario ORDER BY c.fecha DESC")
    List<Contacto> findByUsuarioId(@Param("idUsuario") Long idUsuario);

    List<Contacto> findByEstado(String estado);
    void deleteByIdusuario_Id(Long idusuario);
}

