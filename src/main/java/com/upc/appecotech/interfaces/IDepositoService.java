package com.upc.appecotech.interfaces;

import com.upc.appecotech.dtos.DepositoDTO;

import java.util.List;

public interface IDepositoService {
    DepositoDTO registrarDeposito(DepositoDTO depositoDTO);
    DepositoDTO buscarPorId(Long id);
    List<DepositoDTO> findAll();
    DepositoDTO actualizarDeposito(Long id, DepositoDTO depositoDTO);
    DepositoDTO validarDeposito(Long id, boolean aprobado);
    List<DepositoDTO> listarDepositosPorUsuario(Long idUsuario);
}
