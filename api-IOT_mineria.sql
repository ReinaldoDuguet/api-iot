-- Companies
CREATE TABLE public.companies (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    api_key VARCHAR(64) NOT NULL UNIQUE,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Countries
CREATE TABLE public.countries (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Cities
CREATE TABLE public.cities (
    id SERIAL PRIMARY KEY,
    country_id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (country_id) REFERENCES public.countries(id) ON DELETE RESTRICT,
    UNIQUE (country_id, name) -- Ciudad única por país
);

-- Locations
CREATE TABLE public.locations (
    id SERIAL PRIMARY KEY,
    company_id INTEGER NOT NULL,
    city_id INTEGER NOT NULL,
    name VARCHAR(100) NOT NULL,
    metadata JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (company_id) REFERENCES public.companies(id) ON DELETE CASCADE,
    FOREIGN KEY (city_id) REFERENCES public.cities(id) ON DELETE RESTRICT
);

-- Locations index
CREATE INDEX idx_locations_company ON public.locations(company_id);
CREATE INDEX idx_locations_city ON public.locations(city_id);
CREATE INDEX idx_cities_country ON public.cities(country_id);

-- Users
CREATE TABLE public.users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    is_enabled BOOLEAN DEFAULT true,
    is_expired BOOLEAN DEFAULT false,
    is_locked BOOLEAN DEFAULT false,
    credentials_expired BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Permissions
CREATE TABLE public.permissions (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Roles
CREATE TABLE public.roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Junction Table: Role-Permissions
CREATE TABLE public.role_permissions (
    id SERIAL PRIMARY KEY,
    role_id INTEGER NOT NULL,
    permission_id INTEGER NOT NULL,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES public.roles(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES public.permissions(id) ON DELETE CASCADE,
    UNIQUE (role_id, permission_id) -- Evita duplicados
);

-- Junction Table: User-Roles
CREATE TABLE public.user_roles (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES public.roles(id) ON DELETE CASCADE,
    UNIQUE (user_id, role_id) -- Evita asignaciones duplicadas
);

-- Users role index
CREATE INDEX idx_user_roles_user ON public.user_roles(user_id);
CREATE INDEX idx_role_permissions_role ON public.role_permissions(role_id);

-- Sensors categories
CREATE TABLE public.sensor_categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sensors
CREATE TABLE public.sensors (
    id SERIAL PRIMARY KEY,
    location_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    name VARCHAR(100) NOT NULL UNIQUE,
    api_key VARCHAR(64) NOT NULL UNIQUE,
    metadata JSONB,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (location_id) REFERENCES public.locations(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES public.sensor_categories(id) ON DELETE RESTRICT
);

-- Sensors Data
CREATE TABLE public.sensor_data (
    id BIGSERIAL PRIMARY KEY,
    sensor_id INTEGER NOT NULL,
    timestamp BIGINT NOT NULL,
    data JSONB NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sensor_id) REFERENCES public.sensors(id) ON DELETE CASCADE
);

-- Sensors index
CREATE INDEX idx_sensors_location ON public.sensors(location_id);
CREATE INDEX idx_sensor_data_sensor ON public.sensor_data(sensor_id);
CREATE INDEX idx_sensor_data_time ON public.sensor_data(timestamp);