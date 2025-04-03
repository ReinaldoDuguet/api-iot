Write-Host "📦 Compilando todos los servicios con Maven Wrapper..."

# API
cd api
Write-Host "▶️ Compilando API..."
./mvnw.cmd clean package -DskipTests
cd ..
docker build -t iot-api -f api/Dockerfile ./api

# TCP SIMULATOR
cd simulators/tcp-simulator
Write-Host "▶️ Compilando TCP Simulator..."
./mvnw.cmd clean package -DskipTests
cd ../..
docker build -t iot-tcp-simulator -f simulators/tcp-simulator/Dockerfile ./simulators/tcp-simulator

# KAFKA SIMULATOR
cd simulators/kafka-simulator
Write-Host "▶️ Compilando Kafka Simulator..."
./mvnw.cmd clean package -DskipTests
cd ../..
docker build -t iot-kafka-simulator -f simulators/kafka-simulator/Dockerfile ./simulators/kafka-simulator

Write-Host "✅ Todas las imágenes fueron construidas correctamente."
