package com.upc.appecotech.controladores;

import com.upc.appecotech.security.entidades.Rol;
import com.upc.appecotech.interfaces.IRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

public class RolController {
    @Autowired
    private IRolService rolService;


    @GetMapping("roles")
    public List<Rol> listarRoles(){
        return rolService.listarRoles();
    }
}
