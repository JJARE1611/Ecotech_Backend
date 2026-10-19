package com.upc.appecotech.entidades;

import com.upc.appecotech.security.entidades.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "deposito")
public class Deposito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddeposito", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idusuario", nullable = false)
    private Usuario idusuario;

    @NotNull
    @Column(name = "ubicacion", nullable = false, length = Integer.MAX_VALUE)
    private String ubicacion;

    @NotNull
    @Column(name = "fechaenvio", nullable = false)
    private LocalDate fechaenvio;

    @NotNull
    @Column(name = "descripcion", nullable = false, length = Integer.MAX_VALUE)
    private String descripcion;

    @NotNull
    @Column(name = "tiporesiduo", nullable = false, length = Integer.MAX_VALUE)
    private String tiporesiduo;

    @NotNull
    @Column(name = "ubicaciondeposito", nullable = false, length = Integer.MAX_VALUE)
    private String ubicaciondeposito;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String pruebas;

    @NotNull
    @Column(name = "cantidad", nullable = false)
    private BigDecimal cantidad;

    @NotNull
    @Column(name = "puntosotorgados", nullable = false)
    private Integer puntosotorgados;

    @NotNull
    @Column(name = "estado", nullable = false, length = Integer.MAX_VALUE)
    private String estado;

}