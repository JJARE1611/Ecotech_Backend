package com.upc.appecotech.security.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rol")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idrol", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "nombrerol", nullable = false, length = Integer.MAX_VALUE)
    private String nombrerol;

}