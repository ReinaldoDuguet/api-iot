FROM eclipse-temurin:21-jdk

LABEL maintainer="julio.dev@iotminero.com"
LABEL version="1.0.0"
LABEL description="Microservicio simulador TCP (ESP32) para IoT Minero"
LABEL app="iot-minero-tcp-simulator"

WORKDIR /app

COPY target/tcp-simulator-*.jar app.jar

EXPOSE 6000

ENTRYPOINT ["java", "-jar", "app.jar"]

