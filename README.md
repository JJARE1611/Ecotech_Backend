# 🌱 EcoTech Backend API

> API RESTful robusta para plataforma de gestión de reciclaje y sostenibilidad ambiental

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)

## 📋 Descripción

Backend completo de EcoTech, una plataforma web diseñada para fomentar prácticas de reciclaje responsable mediante gamificación, eventos comunitarios y sistema de recompensas. Desarrollado con Spring Boot siguiendo arquitectura en capas y principios SOLID.

### ✨ Características Principales

- 🔐 **Autenticación y Autorización**: Spring Security con JWT (roles: USER, ADMIN)
- 💳 **Sistema de Suscripciones**: Planes Básico (gratuito), Premium y VIP con beneficios diferenciados
- ♻️ **Gestión de Depósitos**: Registro, validación y aprobación de reciclaje con cálculo de puntos
- 🎯 **Sistema de Puntos (EcoPuntos)**: Acumulación con multiplicadores según suscripción (x1, x2, x3)
- 🎪 **Eventos Comunitarios**: Creación, inscripción y validación de asistencia con restricciones por tipo
- 🎁 **Sistema de Canjes**: Catálogo de productos canjeables con descuentos por suscripción
- 📊 **Historial y Reportes**: Tracking completo de depósitos, puntos, canjes y eventos
- 🔔 **Notificaciones**: Sistema de alertas para usuarios
- 💬 **Retroalimentación**: Valoración y comentarios de eventos

## 🛠️ Stack Tecnológico

- **Framework**: Spring Boot 3.2.x
- **Lenguaje**: Java 17
- **Seguridad**: Spring Security + JWT (JSON Web Tokens)
- **Persistencia**: Spring Data JPA + Hibernate
- **Base de Datos**: PostgreSQL 15
- **Validación**: Bean Validation (Hibernate Validator)
- **Documentación**: Postman Collection (100+ endpoints)
- **Build Tool**: Maven
