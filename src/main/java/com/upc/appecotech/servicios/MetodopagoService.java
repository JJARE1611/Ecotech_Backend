package com.upc.appecotech.servicios;

import com.upc.appecotech.dtos.MetodoPagoDTO;
import com.upc.appecotech.dtos.ProductoDTO;
import com.upc.appecotech.entidades.Metodopago;
import com.upc.appecotech.entidades.Producto;
import com.upc.appecotech.interfaces.IMetodopagoService;
import com.upc.appecotech.repositorios.MetodoPagoRepositorio;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetodopagoService implements IMetodopagoService {
    @Autowired
    private MetodoPagoRepositorio metodoPagoRepositorio;
    @Autowired
    private ModelMapper modelMapper;



    @Override
    @Transactional
    public List<MetodoPagoDTO> listarMetodopagos() {
        List<Metodopago> metodopagos = metodoPagoRepositorio.findAll();
        return metodopagos.stream()
                .map(metodopago -> modelMapper.map(metodopago, MetodoPagoDTO.class))
                .toList();
    }
}
