package com.upc.appecotech.entidades;

import com.upc.appecotech.security.entidades.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "historialdepuntos")
public class Historialdepunto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idhistoria", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idusuario", nullable = false)
    private Usuario idusuario;

    @NotNull
    @Column(name = "puntosobtenidos", nullable = false)
    private Integer puntosobtenidos;

    @NotNull
    @Column(name = "tipomovimiento", nullable = false, length = Integer.MAX_VALUE)
    private String tipomovimiento;

    @NotNull
    @Column(name = "descripcion", nullable = false, length = Integer.MAX_VALUE)
    private String descripcion;

    @NotNull
    @Column(name = "puntoscanjeados", nullable = false)
    private Integer puntoscanjeados;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

}
