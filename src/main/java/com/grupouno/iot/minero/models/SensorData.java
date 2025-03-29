package com.grupouno.iot.minero.models;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Setter
@Getter
@Entity
@Table(name = "sensor_data")
public class SensorData {

    // Getters y Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    @Column(name = "timestamp", nullable = false)
    private long timestamp;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "data", columnDefinition = "jsonb")
    private Map<String, Object> data;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}