package com.grupouno.iot.minero.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.HashMap;
import java.util.Map;

@Converter(autoApply = false)
public class JsonbConverter implements AttributeConverter<Map<String, Object>, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        System.out.println("🎯 SERIALIZANDO METADATA a jsonb... ➜ " + attribute);
        try {
            return mapper.writeValueAsString(attribute);
        } catch (Exception e) {
            System.out.println("❌ Error serializando metadata: " + e.getMessage());
            return "{}";
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        try {
            return mapper.readValue(dbData, new TypeReference<>() {});
        } catch (Exception e) {
            return new HashMap<>();
        }
    }
}
