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
@Table(name = "usuarioevento")
public class Usuarioevento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuarioevento", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idusuario", nullable = false)
    private Usuario idusuario;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idevento", nullable = false)
    private Evento idevento;

    @NotNull
    @Column(name = "fechainscripcion", nullable = false)
    private LocalDate fechainscripcion;

    @NotNull
    @Column(name = "asistio", nullable = false)
    private Boolean asistio = false;

    @NotNull
    @Column(name = "puntosotorgados", nullable = false)
    private Integer puntosotorgados;

}
