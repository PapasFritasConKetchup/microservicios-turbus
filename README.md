# TurbusApp - Microservicios

## Descripción

TurbusApp es un sistema basado en arquitectura de microservicios para la gestión de viajes en bus. Permite administrar pasajeros, trabajadores, rutas, buses, viajes y reservas de asientos.

El proyecto fue desarrollado con Spring Boot, Java 21, MySQL, JPA/Hibernate, validaciones, manejo de excepciones, logs con SLF4J y comunicación entre microservicios mediante WebClient.

## Integrantes

- Lucas Sepulveda

## Microservicios implementados

| Microservicio | Puerto | Función |
|---|---:|---|
| pasajeros-service | 8081 | Gestión de pasajeros |
| trabajadores-service | 8082 | Gestión de trabajadores |
| rutas-service | 8083 | Gestión de rutas |
| buses-service | 8084 | Gestión de buses y asientos |
| viajes-service | 8085 | Programación de viajes |
| reservas-service | 8086 | Gestión de reservas |

## Base de datos

El proyecto utiliza una base de datos MySQL única:

```sql
turbus_microservicios_db
