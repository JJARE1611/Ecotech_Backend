package com.upc.appecotech.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "metodopago")
public class Metodopago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmetodopago", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "nombremetodo", nullable = false, length = Integer.MAX_VALUE)
    private String nombremetodo;

}