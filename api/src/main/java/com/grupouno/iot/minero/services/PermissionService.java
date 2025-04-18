package com.grupouno.iot.minero.services;

import com.grupouno.iot.minero.models.Permission;
import java.util.List;
import java.util.Optional;

public interface PermissionService {
    List<Permission> getAllPermissions();
    Optional<Permission> getPermissionById(Long id);
    Permission createPermission(Permission newPermission);
    Permission updatePermission(Long id, Permission updatedPermission);
    void deletePermission(Long id);
}