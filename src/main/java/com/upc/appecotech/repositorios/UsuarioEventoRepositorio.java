package com.upc.appecotech.repositorios;


import com.upc.appecotech.entidades.Evento;
import com.upc.appecotech.entidades.Usuarioevento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsuarioEventoRepositorio extends JpaRepository<Usuarioevento, Long> {

    boolean existsByIdusuario_IdAndIdevento_Id(Long idUsuario, Long idEvento);

    List<Usuarioevento> findByIdevento_Id(Long idEvento);

    @Query("SELECT ue FROM Usuarioevento ue WHERE ue.idusuario.id = :idUsuario AND ue.idevento.id = :idEvento")
    List<Usuarioevento> findByUsuarioIdAndEventoId(@Param("idUsuario") Long idUsuario, @Param("idEvento") Long idEvento);

    List<Usuarioevento> findByIdusuario_Id(Long idUsuario);


    void deleteByIdusuario_Id(Long idusuario);
    List<Evento> existsByIdevento_Id(Long idevento_Id);

    Long countByIdevento_Id(Long idEvento);
}
