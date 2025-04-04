-- ⚙️ Corrige columna metadata en tabla locations
ALTER TABLE locations
ALTER COLUMN metadata TYPE jsonb
  USING metadata::jsonb;

-- ⚙️ Corrige columna metadata en tabla sensors
ALTER TABLE sensors
ALTER COLUMN metadata TYPE jsonb
  USING metadata::jsonb;

ALTER TABLE sensor_data
    ALTER COLUMN data TYPE jsonb
        USING data::jsonb;