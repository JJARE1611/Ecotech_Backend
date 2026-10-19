package com.upc.appecotech.entidades;

import com.upc.appecotech.security.entidades.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "notificacion")
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idnotificacion", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idusuario", nullable = false)
    private Usuario idusuario;

    @NotNull
    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @NotNull
    @Column(name = "mensaje", nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @NotNull
    @Column(name = "tipo", nullable = false)
    private String tipo; // canje_pendiente, canje_aprobado, canje_entregado, canje_rechazado

    @Column(name = "idReferencia") // ID del canje relacionado
    private Long idReferencia;

    @NotNull
    @Column(name = "leida", nullable = false)
    private Boolean leida = false;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "icono")
    private String icono; // emoji o clase de icono
}