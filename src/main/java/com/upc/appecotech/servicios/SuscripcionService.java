package com.upc.appecotech.servicios;

import com.upc.appecotech.dtos.BeneficiosDTO;
import com.upc.appecotech.dtos.SuscripcionDTO;
import com.upc.appecotech.entidades.Metodopago;
import com.upc.appecotech.entidades.Suscripcion;
import com.upc.appecotech.security.entidades.Usuario;
import com.upc.appecotech.interfaces.ISuscripcionService;
import com.upc.appecotech.repositorios.MetodoPagoRepositorio;
import com.upc.appecotech.repositorios.SuscripcionRepositorio;
import com.upc.appecotech.security.repositorios.UsuarioRepositorio;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Service
public class SuscripcionService implements ISuscripcionService {
    @Autowired
    private SuscripcionRepositorio suscripcionRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private MetodoPagoRepositorio metodoPagoRepositorio;
    @Autowired
    private ModelMapper modelMapper;

    private static final List<String> PLANES_VALIDOS = Arrays.asList("Basico", "Premium", "VIP");

    private static final List<String> ESTADOS_VALIDOS = Arrays.asList("Activa", "Cancelada", "Vencida", "Programada");

    @Override
    @Transactional
    public SuscripcionDTO crearSuscripcion(SuscripcionDTO suscripcionDTO) {
        try {
            Usuario usuario = usuarioRepositorio.findById(suscripcionDTO.getIdusuario())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

            List<Suscripcion> suscripciones = suscripcionRepositorio.findByUsuarioId(usuario.getId());
            boolean tieneActiva = suscripciones.stream()
                    .anyMatch(s -> "Activa".equals(s.getEstado()) &&
                            s.getFechafin().isAfter(LocalDate.now()) &&
                            !"Basico".equals(s.getTipoplan())); // ← AGREGAR ESTA LÍNEA

            if (tieneActiva) {
                throw new IllegalStateException(
                        "Ya tienes una suscripción activa. " +
                                "Si deseas cambiar de plan, usa la opción 'Cambiar Plan'."
                );
            }

            Metodopago metodoPago = metodoPagoRepositorio.findById(suscripcionDTO.getIdmetodopago())
                    .orElseThrow(() -> new EntityNotFoundException("Método de pago no encontrado"));


            if (!PLANES_VALIDOS.contains(suscripcionDTO.getTipoplan())) {
                throw new IllegalArgumentException("Plan inválido. Debe ser: Basico, Premium o VIP");
            }

            suscripciones.stream()
                    .filter(s -> "Basico".equals(s.getTipoplan()) && "Activa".equals(s.getEstado()))
                    .forEach(s -> {
                        s.setEstado("Reemplazada");
                        suscripcionRepositorio.save(s);
                    });

            Suscripcion suscripcion = new Suscripcion();
            suscripcion.setIdusuario(usuario);
            suscripcion.setIdmetodopago(metodoPago);
            suscripcion.setTipoplan(suscripcionDTO.getTipoplan());
            suscripcion.setFechainicio(LocalDate.now());
            suscripcion.setFechafin(calcularFechaFin(suscripcionDTO.getTipoplan()));
            suscripcion.setDescripcion(generarDescripcion(suscripcionDTO.getTipoplan()));
            suscripcion.setEstado("Activa");
            suscripcion.setMonto(calcularMontoPlan(suscripcionDTO.getTipoplan()));

            asignarBeneficios(suscripcion, suscripcionDTO.getTipoplan());

            Suscripcion guardada = suscripcionRepositorio.save(suscripcion);

            return convertirADTO(guardada);

        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Error al crear suscripción: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public SuscripcionDTO cambiarPlan(Long idUsuario, String nuevoPlan, Long idMetodoPago) {

        if (!PLANES_VALIDOS.contains(nuevoPlan)) {
            throw new IllegalArgumentException("Plan inválido. Debe ser: Basico, Premium o VIP");
        }

        Usuario usuario = usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Metodopago metodoPago = metodoPagoRepositorio.findById(idMetodoPago)
                .orElseThrow(() -> new EntityNotFoundException("Método de pago no encontrado"));

        Suscripcion suscripcionActual = suscripcionRepositorio.findByUsuarioId(idUsuario).stream()
                .filter(s -> "Activa".equals(s.getEstado()))
                .findFirst()
                .orElse(null);

        if (suscripcionActual == null) {
            SuscripcionDTO nuevaSuscripcion = new SuscripcionDTO();
            nuevaSuscripcion.setIdusuario(idUsuario);
            nuevaSuscripcion.setIdmetodopago(idMetodoPago);
            nuevaSuscripcion.setTipoplan(nuevoPlan);
            return crearSuscripcion(nuevaSuscripcion);
        }

        String planActual = suscripcionActual.getTipoplan();

        int nivelActual = obtenerNivelPlan(planActual);
        int nivelNuevo = obtenerNivelPlan(nuevoPlan);

        if (nivelNuevo > nivelActual) {
            return aplicarUpgrade(suscripcionActual, nuevoPlan, metodoPago);
        } else if (nivelNuevo < nivelActual) {
            return programarDowngrade(suscripcionActual, nuevoPlan);
        } else {
            throw new IllegalArgumentException("El plan seleccionado es el mismo que el actual");
        }
    }


    private SuscripcionDTO aplicarUpgrade(Suscripcion suscripcionActual, String nuevoPlan, Metodopago metodoPago) {

        suscripcionActual.setTipoplan(nuevoPlan);
        suscripcionActual.setFechafin(calcularFechaFin(nuevoPlan));
        suscripcionActual.setDescripcion(generarDescripcion(nuevoPlan));
        suscripcionActual.setMonto(calcularMontoPlan(nuevoPlan));
        suscripcionActual.setIdmetodopago(metodoPago);

        asignarBeneficios(suscripcionActual, nuevoPlan);

        Suscripcion actualizada = suscripcionRepositorio.save(suscripcionActual);
        return convertirADTO(actualizada);
    }


    private SuscripcionDTO programarDowngrade(Suscripcion suscripcionActual, String nuevoPlan) {
        suscripcionActual.setProximoPlan(nuevoPlan);

        Suscripcion actualizada = suscripcionRepositorio.save(suscripcionActual);
        return convertirADTO(actualizada);
    }

    @Override
    @Transactional
    public SuscripcionDTO cancelarSuscripcion(Long idSuscripcion) {
        return suscripcionRepositorio.findById(idSuscripcion)
                .map(suscripcion -> {
                    suscripcion.setProximoPlan("Basico");


                    Suscripcion actualizada = suscripcionRepositorio.save(suscripcion);
                    return convertirADTO(actualizada);
                })
                .orElseThrow(() -> new EntityNotFoundException("Suscripción no encontrada"));
    }


    @Override
    @Transactional
    public SuscripcionDTO cancelarCambioProgramado(Long idSuscripcion) {
        return suscripcionRepositorio.findById(idSuscripcion)
                .map(suscripcion -> {
                    if (suscripcion.getProximoPlan() == null) {
                        throw new IllegalStateException("No hay ningún cambio programado para cancelar");
                    }

                    suscripcion.setProximoPlan(null);
                    suscripcion.setEstado("Activa");

                    Suscripcion actualizada = suscripcionRepositorio.save(suscripcion);
                    return convertirADTO(actualizada);
                })
                .orElseThrow(() -> new EntityNotFoundException("Suscripción no encontrada"));
    }

    @Override
    @Transactional
    public void procesarSuscripcionesVencidas() {
        LocalDate hoy = LocalDate.now();
        List<Suscripcion> suscripciones = suscripcionRepositorio.findAll();

        for (Suscripcion suscripcion : suscripciones) {
            if ("Activa".equals(suscripcion.getEstado()) && suscripcion.getFechafin().isBefore(hoy)) {

                if (suscripcion.getProximoPlan() != null) {
                    suscripcion.setTipoplan(suscripcion.getProximoPlan());
                    suscripcion.setFechainicio(hoy);
                    suscripcion.setFechafin(calcularFechaFin(suscripcion.getProximoPlan()));
                    suscripcion.setDescripcion(generarDescripcion(suscripcion.getProximoPlan()));
                    suscripcion.setMonto(calcularMontoPlan(suscripcion.getProximoPlan()));
                    asignarBeneficios(suscripcion, suscripcion.getProximoPlan());
                    suscripcion.setProximoPlan(null);
                    suscripcion.setEstado("Activa");
                } else {
                    suscripcion.setTipoplan("Basico");
                    suscripcion.setFechainicio(hoy);
                    suscripcion.setFechafin(calcularFechaFin("Basico"));
                    suscripcion.setDescripcion(generarDescripcion("Basico"));
                    suscripcion.setMonto(calcularMontoPlan("Basico"));
                    suscripcion.setEstado("Activa");
                    asignarBeneficios(suscripcion, "Basico");
                }

                suscripcionRepositorio.save(suscripcion);
            }
        }
    }

    @Override
    public BeneficiosDTO obtenerBeneficios(String tipoplan) {
        BeneficiosDTO beneficios = new BeneficiosDTO();
        beneficios.setTipoplan(tipoplan);

        switch (tipoplan) {
            case "Basico":
                beneficios.setMultiplicadorPuntos(1);
                beneficios.setDescuentoCanje(0);
                beneficios.setAccesoEventosVIP(false);
                beneficios.setPrioridadEventos(false);
                beneficios.setContenidoExclusivo(false);
                beneficios.setDescripcionBeneficios(
                        "• Acceso a eventos públicos\n" +
                                "• Canje de productos estándar\n" +
                                "• 1x multiplicador de puntos"
                );
                break;

            case "Premium":
                beneficios.setMultiplicadorPuntos(2);
                beneficios.setDescuentoCanje(15);
                beneficios.setAccesoEventosVIP(false);
                beneficios.setPrioridadEventos(true);
                beneficios.setContenidoExclusivo(false);
                beneficios.setDescripcionBeneficios(
                        "• Acceso prioritario a eventos\n" +
                                "• 15% descuento en canjes\n" +
                                "• 2x multiplicador de puntos\n" +
                                "• Beneficios de plan Básico"
                );
                break;

            case "VIP":
                beneficios.setMultiplicadorPuntos(3);
                beneficios.setDescuentoCanje(30);
                beneficios.setAccesoEventosVIP(true);
                beneficios.setPrioridadEventos(true);
                beneficios.setContenidoExclusivo(true);
                beneficios.setDescripcionBeneficios(
                        "• Eventos exclusivos VIP\n" +
                                "• 30% descuento en canjes\n" +
                                "• 3x multiplicador de puntos\n" +
                                "• Contenido exclusivo\n" +
                                "• Acceso prioritario a eventos\n" +
                                "• Todos los beneficios Premium y Básico"
                );
                break;

            default:
                beneficios.setMultiplicadorPuntos(1);
                beneficios.setDescuentoCanje(0);
                beneficios.setAccesoEventosVIP(false);
                beneficios.setPrioridadEventos(false);
                beneficios.setContenidoExclusivo(false);
                beneficios.setDescripcionBeneficios("Plan básico estándar");
        }

        return beneficios;
    }


    private void asignarBeneficios(Suscripcion suscripcion, String tipoplan) {
        BeneficiosDTO beneficios = obtenerBeneficios(tipoplan);
        suscripcion.setDescuentoCanje(beneficios.getDescuentoCanje());
        suscripcion.setAccesoEventosVIP(beneficios.getAccesoEventosVIP());
        suscripcion.setPrioridadEventos(beneficios.getPrioridadEventos());
        suscripcion.setContenidoExclusivo(beneficios.getContenidoExclusivo());
    }

    private int obtenerNivelPlan(String plan) {
        return switch (plan) {
            case "Basico" -> 1;
            case "Premium" -> 2;
            case "VIP" -> 3;
            default -> 0;
        };
    }

    private BigDecimal calcularMontoPlan(String tipoplan) {
        return switch (tipoplan) {
            case "Basico" -> new BigDecimal("0.00");
            case "Premium" -> new BigDecimal("29.90");
            case "VIP" -> new BigDecimal("79.90");
            default -> new BigDecimal("0.00");
        };
    }

    private SuscripcionDTO convertirADTO(Suscripcion suscripcion) {
        SuscripcionDTO dto = modelMapper.map(suscripcion, SuscripcionDTO.class);

        // Calcular campos adicionales
        BeneficiosDTO beneficios = obtenerBeneficios(suscripcion.getTipoplan());
        dto.setMultiplicadorPuntos(beneficios.getMultiplicadorPuntos());

        // Calcular días restantes
        long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), suscripcion.getFechafin());
        dto.setDiasRestantes((int) Math.max(0, diasRestantes));

        return dto;
    }

    @Override
    @Transactional
    public boolean validarSuscripcionActiva(Long idUsuario) {
        List<Suscripcion> suscripciones = suscripcionRepositorio.findByUsuarioId(idUsuario);

        return suscripciones.stream()
                .anyMatch(s -> "Activa".equals(s.getEstado()) &&
                        s.getFechafin().isAfter(LocalDate.now()));
    }

    @Override
    @Transactional
    public SuscripcionDTO actualizarEstadoSuscripcion(Long idSuscripcion, String nuevoEstado) {
        if (!ESTADOS_VALIDOS.contains(nuevoEstado)) {
            throw new IllegalArgumentException("Estado inválido. Debe ser: Activa, Cancelada, Vencida o Programada");
        }

        return suscripcionRepositorio.findById(idSuscripcion)
                .map(suscripcion -> {
                    suscripcion.setEstado(nuevoEstado);
                    Suscripcion actualizada = suscripcionRepositorio.save(suscripcion);
                    return convertirADTO(actualizada);
                })
                .orElseThrow(() -> new EntityNotFoundException("Suscripción no encontrada con ID: " + idSuscripcion));
    }

    @Override
    @Transactional
    public SuscripcionDTO renovarSuscripcion(Long idSuscripcion, String nuevoPlan) {
        if (!PLANES_VALIDOS.contains(nuevoPlan)) {
            throw new IllegalArgumentException("Plan inválido. Debe ser: Basico, Premium o VIP");
        }

        return suscripcionRepositorio.findById(idSuscripcion)
                .map(suscripcion -> {
                    LocalDate nuevaFechaInicio;
                    LocalDate nuevaFechaFin;

                    if (suscripcion.getFechafin().isAfter(LocalDate.now())) {
                        nuevaFechaInicio = suscripcion.getFechafin();
                        nuevaFechaFin = calcularFechaFinDesde(nuevoPlan, nuevaFechaInicio);
                    } else {
                        nuevaFechaInicio = LocalDate.now();
                        nuevaFechaFin = calcularFechaFin(nuevoPlan);
                    }

                    suscripcion.setTipoplan(nuevoPlan);
                    suscripcion.setFechainicio(nuevaFechaInicio);
                    suscripcion.setFechafin(nuevaFechaFin);
                    suscripcion.setDescripcion(generarDescripcion(nuevoPlan));
                    suscripcion.setEstado("Activa");
                    suscripcion.setMonto(calcularMontoPlan(nuevoPlan));
                    asignarBeneficios(suscripcion, nuevoPlan);

                    Suscripcion renovada = suscripcionRepositorio.save(suscripcion);
                    return convertirADTO(renovada);
                })
                .orElseThrow(() -> new EntityNotFoundException("Suscripción no encontrada con ID: " + idSuscripcion));
    }


    private LocalDate calcularFechaFinDesde(String tipoPlan, LocalDate desde) {
        return switch (tipoPlan) {
            case "Basico" -> desde.plusMonths(1);
            case "Premium" -> desde.plusMonths(6);
            case "VIP" -> desde.plusYears(1);
            default -> desde.plusMonths(1);
        };
    }

    @Override
    public SuscripcionDTO buscarPorId(Long id) {
        return suscripcionRepositorio.findById(id)
                .map(this::convertirADTO)
                .orElse(null);
    }

    @Override
    public List<SuscripcionDTO> listarTodas() {
        List<Suscripcion> lista = suscripcionRepositorio.findAll();
        return lista.stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    public List<SuscripcionDTO> listarSuscripcionesPorUsuario(Long idUsuario) {
        usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + idUsuario));

        List<Suscripcion> lista = suscripcionRepositorio.findByUsuarioId(idUsuario);

        return lista.stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    public SuscripcionDTO obtenerSuscripcionActivaUsuario(Long idUsuario) {
        List<Suscripcion> suscripciones = suscripcionRepositorio.findByUsuarioId(idUsuario);

        return suscripciones.stream()
                .filter(s -> "Activa".equals(s.getEstado()) && s.getFechafin().isAfter(LocalDate.now()))
                .findFirst()
                .map(this::convertirADTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public Integer obtenerMultiplicadorPuntos(Long idUsuario) {
        SuscripcionDTO suscripcionActiva = obtenerSuscripcionActivaUsuario(idUsuario);

        if (suscripcionActiva == null) {
            return 1;
        }

        return switch (suscripcionActiva.getTipoplan()) {
            case "Basico" -> 1;
            case "Premium" -> 2;
            case "VIP" -> 3;
            default -> 1;
        };
    }

    private LocalDate calcularFechaFin(String tipoPlan) {
        LocalDate ahora = LocalDate.now();

        return switch (tipoPlan) {
            case "Basico" -> ahora.plusMonths(1);
            case "Premium" -> ahora.plusMonths(6);
            case "VIP" -> ahora.plusYears(1);
            default -> ahora.plusMonths(1);
        };
    }

    private String generarDescripcion(String tipoPlan) {
        return switch (tipoPlan) {
            case "Basico" -> "Plan Básico - Acceso estándar a funcionalidades";
            case "Premium" -> "Plan Premium - Beneficios adicionales y más ecoPuntos";
            case "VIP" -> "Plan VIP - Acceso completo y beneficios exclusivos";
            default -> "Plan estándar";
        };
    }
}