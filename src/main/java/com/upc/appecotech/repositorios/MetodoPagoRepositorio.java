package com.upc.appecotech.repositorios;

import com.upc.appecotech.entidades.Metodopago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MetodoPagoRepositorio extends JpaRepository<Metodopago, Long> {
    Optional<Metodopago> findByNombremetodo(String nombremetodo);

}
