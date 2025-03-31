package com.grupouno.iot.minero.listener; // Paquete actual donde está el listener

import com.grupouno.iot.minero.services.SensorKafkaProcessor; // Inyectamos la clase que procesa el mensaje
import org.apache.kafka.clients.consumer.ConsumerRecord; // Clase de Kafka que representa un mensaje
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty; // Para habilitar esta clase solo si iot.mode=kafka
import org.springframework.kafka.annotation.KafkaListener; // Anotación para escuchar tópicos
import org.springframework.stereotype.Component; // Declara esta clase como un componente Spring

@Component
@ConditionalOnProperty(name = "iot.mode", havingValue = "kafka") // Se habilita solo si 'iot.mode=kafka' en application.properties
public class SensorKafkaConsumer {

    private final SensorKafkaProcessor processor; // Dependencia que hace el trabajo real de procesar el mensaje

    public SensorKafkaConsumer(SensorKafkaProcessor processor) {
        this.processor = processor;
    }

    @KafkaListener(topics = "iot-sensor-data", groupId = "iot-minero-group") // Listener activo en este tópico
    public void consume(ConsumerRecord<String, String> record) {
        processor.processKafkaMessage(record.value()); // Solo delega la lógica al procesador
    }
}
