# Proyecto: Plataforma IoT Minera

Este proyecto implementa una **plataforma de monitoreo IoT para el sector minero** desarrollada en **Java con Spring Boot**, que permite la recolección de datos desde sensores físicos (ESP32 y Zigbee), simuladores TCP/Kafka, almacenamiento en PostgreSQL, y el procesamiento de datos en tiempo real.

---

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Security**
- **PostgreSQL**
- **Kafka (Confluent)**
- **TCP/IP Socket Server**
- **Docker y Docker Compose**
- **Librerías Adicionales**: Lombok, Jackson, Hibernate Types (JSONB)

---

## Arquitectura del Proyecto

### Capas principales:

- **controller/**: Controladores REST
- **dto/**: Data Transfer Objects
- **dao/**: Acceso a entidades a bajo nivel
- **models/**: Entidades del dominio (User, Sensor, SensorData, etc.)
- **repository/**: Interfaces JPA para acceso a datos
- **services/**:
    - `impl`: Lógica de negocio para usuarios y sensores
    - `simulator`: Simuladores Kafka
    - `tcp`: Procesamiento TCP
- **simulator/**: Simuladores de ESP32 por TCP
- **tcp/**: Lógica base para servidor TCP listener
- **config/**: Seguridad, inicializadores y configuraciones
- **postman/**: Colección de pruebas

---

## Funcionalidades Clave

### 1. **Usuarios**

- Registro, modificación, eliminación (CRUD)
- Roles y permisos (ADMIN / USER)
- Autenticación con Spring Security (JWT en desarrollo)
- Endpoints:
    - `GET /api/v1/users`
    - `GET /api/v1/users/{id}`
    - `POST /api/v1/users`
    - `PUT /api/v1/users/{id}`
    - `DELETE /api/v1/users/{id}`

### 2. **Simulador Kafka (Sensor Zigbee)**

- Envió de datos simulados por Kafka
- Configurable mediante `KafkaSimulatorConfig`
- Control REST: `SimulatorController`
- Endpoints REST:
    - `/api/v1/simulator/kafka/start`
    - `/api/v1/simulator/kafka/stop`
    - `/api/v1/simulator/kafka/status`

### 3. **Servidor TCP (Sensor ESP32)**

- Servidor TCP escucha en puerto definido (`application.properties`)
- Procesa datos JSON en tiempo real desde dispositivos externos
- Estructura del JSON:
```json
{
  "api_key": "sensor-api-key-123",
  "json_data": [
    {
      "datetime": 1742861495,
      "temp": 22.1,
      "humidity": 0.6
    }
  ]
}
```

- Clases:
    - `TcpSensorServer`, `TcpSensorListener`, `SensorDataTCPProcessor`

### 4. **Simulador ESP32 TCP (Productor)**

- Crea conexiones TCP simuladas cada X segundos
- Envia datos a `localhost:9000` (o IP externa)
- Control REST:
    - `/api/v1/simulator/esp32/start`
    - `/api/v1/simulator/esp32/stop`
    - `/api/v1/simulator/esp32/status`
    - `/api/v1/simulator/esp32/pause`
    - `/api/v1/simulator/esp32/resume`

- Archivos:
    - `Esp32Simulator`, `Esp32SimulatorManager`, `Esp32SimulatorConfig`

---

## Directorio `postman/`

Ubica dentro de esta carpeta los archivos `.json` con la colección de pruebas para Postman. Puedes importarla directamente para probar:

- Endpoints REST
- Simulación de datos Kafka
- Simulación de datos TCP

> Recomendado: nombrar el archivo como `iot-minero.postman_collection.json`

---

## Variables de Configuración

### application.properties
```properties
# TCP Server
tcp.server.enabled=true
tcp.server.port=9000

# Kafka
kafka.bootstrap-servers=localhost:9092
kafka.topic.sensor-data=sensor-data-topic

# Base de Datos
spring.datasource.url=jdbc:postgresql://localhost:5432/iot_minero
spring.datasource.username=postgres
spring.datasource.password=tu_contraseña
```

---

## Ejecución Local

1. Clona el repositorio
2. Inicia PostgreSQL y Kafka
3. Ejecuta la app desde tu IDE o `./mvnw spring-boot:run`
4. Usa Postman o simuladores TCP/Kafka

---

## Docker (Pendiente)

Planeado:
- Docker Compose para Kafka, PostgreSQL y App Java
- Simuladores en contenedores separados (TCP / Kafka)

---

## Testing

- JUnit 5 + Mockito
- Tests implementados:
    - `UserControllerTest`
    - Tests de simuladores TCP/Kafka (en desarrollo)

---

## Contribuciones Futuras

- Autenticación JWT
- Dashboard frontend (React o Angular)
- Exportación de datos
- Alertas en tiempo real
- Validaciones avanzadas para dispositivos y roles

---

