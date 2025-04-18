# Etapa 2: Imagen ligera
FROM eclipse-temurin:21-jre
LABEL maintainer="julio.dev@iotminero.com"
LABEL version="1.0.0"
LABEL description="Microservicio API principal para IoT Minero"
LABEL app="iot-minero-api"
WORKDIR /app
COPY target/api.jar app.jar
# Expone el puerto 8080 para el API
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

