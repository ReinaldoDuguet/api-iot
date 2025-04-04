# IoT Mining System - Simulación y Procesamiento de Sensores en Tiempo Real

Este proyecto representa una arquitectura moderna orientada a microservicios para un sistema de **telemetría y simulación de sensores IoT** en un entorno minero. Su propósito es simular sensores físicos (como los basados en **ESP32 vía TCP/IP** y **Zigbee vía MQTT/Kafka**) y procesar los datos en tiempo real a través de una API REST desarrollada en **Java Spring Boot**.

---

## 🧠 Descripción General

El sistema está compuesto por 3 microservicios principales:

1. **API IoT (api)**  
   Gestiona sensores, ubicaciones, empresas, roles y permisos. Expone endpoints REST y recibe datos de sensores por TCP o Kafka.

2. **Simulador TCP (tcp-simulator)**  
   Simula sensores físicos que envían datos mediante conexiones TCP/IP, imitando el comportamiento de dispositivos ESP32.

3. **Simulador Kafka (kafka-simulator)**  
   Simula sensores Zigbee que publican datos en tópicos Kafka (como si estuviesen conectados por un Gateway MQTT/Zigbee).

---

### **Estructura del JSON** que recibe el servidor TCP/MQTT:

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
---
### 📦 Arquitectura del Proyecto

```
iot-mining-system/
├── api/                  # API REST principal con Spring Boot
├── simulators/
│   ├── kafka-simulator/  # Microservicio para simular sensores Kafka/Zigbee
│   └── tcp-simulator/    # Microservicio para simular sensores TCP/ESP32
├── docker/
│   ├── docker-compose.yml   # Orquestación completa del sistema
│   └── .env                 # Variables de entorno (opcional)
└── postman/             # Colección de pruebas Postman para endpoints y simuladores
```

---

## 🐳 Microservicios Dockerizados

### 1. API IoT

| Variable                           | Descripción                                       |
|------------------------------------|---------------------------------------------------|
| `SPRING_PROFILES_ACTIVE`           | Entorno de ejecución (`integration` recomendado) |
| `DB_HOST`, `DB_PORT`, `DB_NAME`    | Configuración PostgreSQL                         |
| `KAFKA_BOOTSTRAP_SERVERS`          | Broker Kafka (`kafka:9092`)                      |
| `TCP_ENABLED`                      | Habilita recepción por TCP                       |
| `KAFKA_ENABLED`                    | Habilita recepción por Kafka                     |

---

### 2. TCP Simulator

| Variable                      | Descripción                                           |
|-------------------------------|-------------------------------------------------------|
| `SPRING_PROFILES_ACTIVE`      | Entorno de ejecución                                 |
| `TCP_SIMULATOR_API_KEY`       | ApiKey del sensor simulado                           |
| `TCP_SIMULATOR_DELAY_MS`      | Frecuencia de envío de datos en milisegundos         |
| `TCP_SERVER_HOST`             | IP/host objetivo donde enviar los datos              |
| `TCP_SERVER_PORT`             | Puerto objetivo del servidor TCP (ej. 9999)          |

---

### 3. Kafka Simulator

| Variable                        | Descripción                                           |
|---------------------------------|-------------------------------------------------------|
| `SPRING_PROFILES_ACTIVE`        | Entorno de ejecución                                 |
| `KAFKA_BOOTSTRAP_SERVERS`       | Broker Kafka (`kafka:9092`)                          |
| `KAFKA_SIMULATOR_API_KEY`       | ApiKey del sensor simulado                           |
| `KAFKA_SIMULATOR_DELAY_MS`      | Frecuencia de envío de datos                         |
| `KAFKA_TOPIC`                   | Tópico Kafka a publicar (ej. `iot-sensor-data`)      |

---

## 🚀 ¿Cómo levantar todo?

```bash
docker compose up --build
```

Esto levantará:

- PostgreSQL
- Kafka + Zookeeper
- Kafka UI (para monitorear tópicos)
- API REST
- Simulador TCP
- Simulador Kafka

Todos conectados a través de la red `iot-net`.

---

## 📮 Pruebas con Postman

Incluimos una colección de pruebas ubicada en:

```
/postman/IoT Minero - Full System.postman_collection.json
```

Permite:

- Verificar endpoints REST
- Iniciar/detener simuladores
- Probar flujos completos
- Validar recepción de datos por TCP/Kafka

---

## ✅ Estado Actual

- [x] API REST funcional (sensores, ubicación, empresa, usuarios)
- [x] Recepción de datos por TCP
- [x] Recepción de datos desde Kafka
- [x] Simuladores separados por microservicio
- [x] Dockerizado completo
- [x] Pruebas Postman
- [ ] CI/CD pipeline en Jenkins (en desarrollo)
- [ ] Despliegue a EC2 (en progreso)

---

## 📌 Tecnologías Usadas

- Java 21 + Spring Boot 3
- Kafka + Kafka UI
- PostgreSQL
- Docker + Docker Compose
- Postman
- TCP/IP Socket Programming
- Multi-threaded Producers

---

## 🧪 Próximos pasos

- [ ] Integrar métricas con Prometheus y Grafana
- [ ] Validar integración en entorno de staging
- [ ] Despliegue con CI/CD en AWS EC2

---

## 🧑‍💻 Autores

- Grupo Uno - Ingeniería de Software
