package com.upc.appecotech.config;

import com.upc.appecotech.dtos.*;
import com.upc.appecotech.entidades.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper(){ ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // ========== USUARIOEVENTO ==========
        mapper.createTypeMap(Usuarioevento.class, UsuarioEventoDTO.class)
                .addMapping(Usuarioevento::getId, UsuarioEventoDTO::setId)
                .addMapping(src -> src.getIdusuario().getId(), UsuarioEventoDTO::setIdusuario)
                .addMapping(src -> src.getIdevento().getId(), UsuarioEventoDTO::setIdevento)
                .addMapping(Usuarioevento::getIdevento, UsuarioEventoDTO::setEvento)
                .addMapping(Usuarioevento::getIdusuario, UsuarioEventoDTO::setUsuario);

        // ========== CANJEUSUARIO ==========
        mapper.createTypeMap(Canjeusuario.class, CanjeUsuarioDTO.class)
                .addMapping(Canjeusuario::getId, CanjeUsuarioDTO::setId)
                .addMapping(src -> src.getIdusuario().getId(), CanjeUsuarioDTO::setIdUsuario)
                .addMapping(src -> src.getIdproducto().getId(), CanjeUsuarioDTO::setIdProducto)
                .addMapping(Canjeusuario::getIdproducto, CanjeUsuarioDTO::setProducto)
                .addMapping(Canjeusuario::getIdusuario, CanjeUsuarioDTO::setUsuario);


        // ========== FEEDBACK ==========
        mapper.createTypeMap(Feedback.class, FeedbackDTO.class)
                .addMapping(Feedback::getId, FeedbackDTO::setId)
                .addMapping(src -> src.getIdusuario().getId(), FeedbackDTO::setIdUsuario)
                .addMapping(src -> src.getIdevento().getId(), FeedbackDTO::setIdEvento)
                .addMapping(Feedback::getIdusuario, FeedbackDTO::setUsuario)
                .addMapping(Feedback::getIdevento, FeedbackDTO::setEvento);

        // ========== SUSCRIPCION ==========
        mapper.createTypeMap(Suscripcion.class, SuscripcionDTO.class)
                .addMapping(Suscripcion::getId, SuscripcionDTO::setId)
                .addMapping(src -> src.getIdusuario().getId(), SuscripcionDTO::setIdusuario)
                .addMapping(src -> src.getIdmetodopago().getId(), SuscripcionDTO::setIdmetodopago);


        // ========== CONTACTO ==========
        mapper.createTypeMap(Contacto.class, ContactoDTO.class)
                .addMapping(Contacto::getId, ContactoDTO::setId)
                .addMapping(src -> src.getIdusuario().getId(), ContactoDTO::setIdUsuario);

        // ========== DEPOSITO ==========
        mapper.createTypeMap(Deposito.class, DepositoDTO.class)
                .addMapping(Deposito::getId, DepositoDTO::setId)
                .addMapping(src -> src.getIdusuario().getId(), DepositoDTO::setIdUsuario)
                .addMapping(Deposito::getIdusuario, DepositoDTO::setUsuario);

        // ========== HISTORIALDEPUNTOS ==========
        mapper.createTypeMap(Historialdepunto.class, HistorialPuntosDTO.class)
                .addMapping(Historialdepunto::getId, HistorialPuntosDTO::setId)
                .addMapping(src -> src.getIdusuario().getId(), HistorialPuntosDTO::setIdusuario);


        return mapper;
    }
}
