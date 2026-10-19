package com.upc.appecotech.controladores;

import com.upc.appecotech.dtos.MetodoPagoDTO;
import com.upc.appecotech.interfaces.IMetodopagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

public class MetodopagoController {
    @Autowired
    private IMetodopagoService metodopagoService;

    @GetMapping("/metodos-pagos")
    public ResponseEntity<List<MetodoPagoDTO>> ListarMetodosPagos()
    {
        return ResponseEntity.ok(metodopagoService.listarMetodopagos());
    }

}

