package com.upc.appecotech.security.repositorios;


import com.upc.appecotech.security.entidades.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RolRepositorio extends JpaRepository<Rol, Long> {
    boolean existsByNombrerol(String nombrerol);

    Optional<Rol> findByNombrerol(String nombrerol);
}
