# Etapa 2: Imagen ligera
FROM eclipse-temurin:21-jre
LABEL maintainer="julio.dev@iotminero.com"
LABEL version="1.0.0"
LABEL description="Simulador TCP para IoT Minero"
LABEL app="iot-minero-tcp-simulator"

WORKDIR /app
COPY target/*.jar app.jar
# Expone el puerto 9092 para el API
EXPOSE 9091
ENTRYPOINT ["java", "-jar", "app.jar"]

