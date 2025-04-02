FROM eclipse-temurin:21-jdk

LABEL maintainer="julio.dev@iotminero.com"
LABEL version="1.0.0"
LABEL description="Microservicio simulador Kafka (Zigbee) para IoT Minero"
LABEL app="iot-minero-kafka-simulator"

WORKDIR /app

COPY target/kafka-simulator-*.jar app.jar

# No se expone puerto porque Kafka es saliente
# pero igual puede servir para documentación o monitoreo
EXPOSE 9092

ENTRYPOINT ["java", "-jar", "app.jar"]
