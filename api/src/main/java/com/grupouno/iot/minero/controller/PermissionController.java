package com.grupouno.iot.minero.controller;

import com.grupouno.iot.minero.models.Permission;
import com.grupouno.iot.minero.services.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController {

    private static final Logger logger = Logger.getLogger(PermissionController.class.getName());

    @Autowired
    private PermissionService permissionService;

    @GetMapping
    public List<Permission> getAllPermissions(HttpServletRequest request) {
        logger.info(" --- Starting endpoint process getAllPermissions: " + request.getRequestURI() + " ---");
        List<Permission> permissions = permissionService.getAllPermissions();
        logger.info("Total permissions found: " + permissions.size());
        return permissions;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Long id, HttpServletRequest request) {
        logger.info(" --- Starting endpoint process getPermissionById: " + request.getRequestURI() + " ---");
        Optional<Permission> permission = permissionService.getPermissionById(id);
        return permission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Permission> createPermission(@RequestBody Permission newPermission, HttpServletRequest request) {
        logger.info(" --- Starting endpoint process createPermission: " + request.getRequestURI() + " ---");
        try {
            Permission createdPermission = permissionService.createPermission(newPermission);
            logger.info("Permission created with ID: " + createdPermission.getId());
            return ResponseEntity.ok(createdPermission);
        } catch (Exception e) {
            logger.warning("Error creating permission: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Permission> updatePermission(@PathVariable Long id, 
            @RequestBody Permission updatedPermission,
            HttpServletRequest request) {
        logger.info(" --- Starting endpoint process updatePermission: " + request.getRequestURI() + " ---");
        try {
            Permission permission = permissionService.updatePermission(id, updatedPermission);
            logger.info("Permission modified");
            return ResponseEntity.ok(permission);
        } catch (RuntimeException e) {
            logger.warning("Permission not found with ID: " + id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id, HttpServletRequest request) {
        logger.info(" --- Starting endpoint process deletePermission: " + request.getRequestURI() + " ---");
        try {
            permissionService.deletePermission(id);
            logger.info("Permission deleted");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.warning("Permission not found with ID: " + id);
            return ResponseEntity.notFound().build();
        }
    }
}