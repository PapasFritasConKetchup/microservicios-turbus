# TurbusApp - Sistema de Gestión de Viajes en Bus

## Descripción del Proyecto

TurbusApp es una plataforma de gestión de viajes en bus desarrollada bajo una arquitectura de microservicios independientes. El sistema permite administrar pasajeros, trabajadores, rutas, buses, viajes y reservas de asientos, garantizando la separación funcional entre servicios y la comunicación REST entre ellos a través de un API Gateway centralizado.

El proyecto fue desarrollado utilizando Java 21, Spring Boot, MySQL y Maven, siguiendo el patrón de diseño CSR (Controller–Service–Repository).

---

## Integrantes

| Nombre | Equipo |
|--------|--------|
| Lucas David Sepúlveda Carrasco | Equipo 9 |

---

## Microservicios Implementados

| Microservicio | Puerto | Descripción |
|---------------|--------|-------------|
| microservicio-pasajeros | 8081 | Gestión de pasajeros del sistema |
| microservicio-trabajadores | 8082 | Gestión de trabajadores y conductores |
| microservicio-rutas | 8083 | Gestión de rutas de viaje |
| microservicio-buses | 8084 | Gestión de buses y asientos |
| microservicio-viajes | 8085 | Gestión de viajes programados |
| microservicio-reservas | 8086 | Gestión de reservas de asientos |
| api-gateway | 8080 | Gateway centralizado de enrutamiento |

---

## Rutas del API Gateway

El API Gateway corre en el puerto **8080** y enruta las peticiones hacia cada microservicio usando el prefijo `/gateway`:

| Ruta Gateway | Microservicio Destino |
|---|---|
| `http://localhost:8080/gateway/pasajeros/api/pasajeros` | microservicio-pasajeros (8081) |
| `http://localhost:8080/gateway/trabajadores/api/trabajadores` | microservicio-trabajadores (8082) |
| `http://localhost:8080/gateway/rutas/api/rutas` | microservicio-rutas (8083) |
| `http://localhost:8080/gateway/buses/api/buses` | microservicio-buses (8084) |
| `http://localhost:8080/gateway/viajes/api/viajes` | microservicio-viajes (8085) |
| `http://localhost:8080/gateway/reservas/api/reservas` | microservicio-reservas (8086) |

---

## Documentación Swagger/OpenAPI

Cada microservicio expone su documentación técnica en Swagger UI:

| Microservicio | URL Swagger |
|---|---|
| Pasajeros | http://localhost:8081/swagger-ui/index.html |
| Trabajadores | http://localhost:8082/swagger-ui/index.html |
| Rutas | http://localhost:8083/swagger-ui/index.html |
| Buses | http://localhost:8084/swagger-ui/index.html |
| Viajes | http://localhost:8085/swagger-ui/index.html |
| Reservas | http://localhost:8086/swagger-ui/index.html |

---

## Instrucciones de Ejecución Local

### Requisitos previos
- Java 21
- Maven
- MySQL (XAMPP recomendado)
- IntelliJ IDEA

### Pasos

1. Clona el repositorio:
```bash
git clone https://github.com/PapasFritasConKetchup/microservicios-turbus
```

2. Inicia MySQL desde XAMPP Control Panel.

3. Abre cada microservicio en IntelliJ IDEA como proyecto independiente.

4. Ejecuta cada microservicio en este orden:
   - microservicio-pasajeros (8081)
   - microservicio-trabajadores (8082)
   - microservicio-rutas (8083)
   - microservicio-buses (8084)
   - microservicio-viajes (8085)
   - microservicio-reservas (8086)
   - api-gateway (8080)

5. Verifica que los servicios estén corriendo accediendo a:
```
http://localhost:8081/api/pasajeros
```

---

## Instrucciones de Ejecución con Docker

### Requisitos previos
- Docker Desktop instalado y corriendo

### Pasos

1. Construye las imágenes de cada microservicio. Dentro de cada carpeta ejecuta:
```bash
$env:JAVA_HOME = "C:\Users\papas\.jdks\ms-21.0.10"
.\mvnw.cmd clean package -DskipTests
docker build -t ms-pasajeros:1.0 .
docker build -t ms-trabajadores:1.0 .
docker build -t ms-ruta:1.0 .
docker build -t ms-buses:1.0 .
docker build -t ms-viajes:1.0 .
docker build -t ms-reservas:1.0 .
docker build -t api-gateway:1.0 .
```

2. Crea la red Docker:
```bash
docker network create turbus-network
```

3. Levanta los contenedores:
```bash
docker run -d --name ms-pasajeros --network turbus-network -p 8081:8081 -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/turbus_microservicios_db -e SPRING_DATASOURCE_USERNAME=root -e SPRING_DATASOURCE_PASSWORD= ms-pasajeros:1.0

docker run -d --name ms-trabajadores --network turbus-network -p 8082:8082 -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/turbus_microservicios_db -e SPRING_DATASOURCE_USERNAME=root -e SPRING_DATASOURCE_PASSWORD= ms-trabajadores:1.0

docker run -d --name ms-ruta --network turbus-network -p 8083:8083 -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/turbus_microservicios_db -e SPRING_DATASOURCE_USERNAME=root -e SPRING_DATASOURCE_PASSWORD= ms-ruta:1.0

docker run -d --name ms-buses --network turbus-network -p 8084:8084 -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/turbus_microservicios_db -e SPRING_DATASOURCE_USERNAME=root -e SPRING_DATASOURCE_PASSWORD= ms-buses:1.0

docker run -d --name ms-viajes --network turbus-network -p 8085:8085 -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/turbus_microservicios_db -e SPRING_DATASOURCE_USERNAME=root -e SPRING_DATASOURCE_PASSWORD= ms-viajes:1.0

docker run -d --name ms-reservas --network turbus-network -p 8086:8086 -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/turbus_microservicios_db -e SPRING_DATASOURCE_USERNAME=root -e SPRING_DATASOURCE_PASSWORD= ms-reservas:1.0

docker run -d --name api-gateway --network turbus-network -p 8080:8080 api-gateway:1.0
```

4. Verifica que todos los contenedores estén corriendo:
```bash
docker ps
```

5. Accede a cualquier servicio a través del Gateway:
```
http://localhost:8080/gateway/pasajeros/api/pasajeros
```

---

## Tecnologías Utilizadas

- Java 21
- Spring Boot
- Spring Cloud Gateway
- Spring Data JPA
- MySQL
- Swagger / SpringDoc OpenAPI
- JUnit 5 + Mockito
- Docker
- Maven
