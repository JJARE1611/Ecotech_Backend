package com.upc.appecotech.interfaces;

import com.upc.appecotech.dtos.MetodoPagoDTO;
import com.upc.appecotech.entidades.Metodopago;

import java.util.List;

public interface IMetodopagoService {
    public List<MetodoPagoDTO> listarMetodopagos();
}
