package com.grupouno.iot.minero.repository;

import com.grupouno.iot.minero.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    // por ahora no es necesario pero servira para
    // Activar/desactivar un rol (isActive = false)
    //Consultar todos los roles activos de un usuario
    //Borrar o editar un UserRole sin tocar al User
    // Obtener estadísticas por rol
}
