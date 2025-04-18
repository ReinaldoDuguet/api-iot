package com.grupouno.iot.minero.services.impl;

import com.grupouno.iot.minero.models.Permission;
import com.grupouno.iot.minero.repository.PermissionRepository;
import com.grupouno.iot.minero.services.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public Optional<Permission> getPermissionById(Long id) {
        return permissionRepository.findById(id);
    }

    @Override
    public Permission createPermission(Permission newPermission) {
        newPermission.setCreatedAt(LocalDateTime.now());
        return permissionRepository.save(newPermission);
    }

    @Override
    public Permission updatePermission(Long id, Permission updatedPermission) {
        return permissionRepository.findById(id).map(permission -> {
            permission.setName(updatedPermission.getName());
            permission.setDescription(updatedPermission.getDescription());
            return permissionRepository.save(permission);
        }).orElseThrow(() -> new RuntimeException("Permiso no encontrado con ID: " + id));
    }

    @Override
    public void deletePermission(Long id) {
        permissionRepository.deleteById(id);
    }
}