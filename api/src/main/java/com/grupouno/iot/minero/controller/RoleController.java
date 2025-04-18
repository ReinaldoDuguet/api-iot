package com.grupouno.iot.minero.controller;

import com.grupouno.iot.minero.models.Role;
import com.grupouno.iot.minero.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private static final Logger logger = Logger.getLogger(RoleController.class.getName());

    @Autowired
    private RoleService roleService;

    @GetMapping
    public List<Role> getAllRoles(HttpServletRequest request) {
        logger.info(" --- Starting endpoint process getAllRoles: " + request.getRequestURI() + " ---");
        List<Role> roles = roleService.getAllRoles();
        logger.info("Total roles found: " + roles.size());
        return roles;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id, HttpServletRequest request) {
        logger.info(" --- Starting endpoint process getRoleById: " + request.getRequestURI() + " ---");
        Optional<Role> role = roleService.getRoleById(id);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role newRole, HttpServletRequest request) {
        logger.info(" --- Starting endpoint process createRole: " + request.getRequestURI() + " ---");
        try {
            Role createdRole = roleService.createRole(newRole);
            logger.info("Role created with ID: " + createdRole.getId());
            return ResponseEntity.ok(createdRole);
        } catch (Exception e) {
            logger.warning("Error creating role: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, 
            @RequestBody Role updatedRole,
            HttpServletRequest request) {
        logger.info(" --- Starting endpoint process updateRole: " + request.getRequestURI() + " ---");
        try {
            Role role = roleService.updateRole(id, updatedRole);
            logger.info("Role modified");
            return ResponseEntity.ok(role);
        } catch (RuntimeException e) {
            logger.warning("Role not found with ID: " + id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id, HttpServletRequest request) {
        logger.info(" --- Starting endpoint process deleteRole: " + request.getRequestURI() + " ---");
        try {
            roleService.deleteRole(id);
            logger.info("Role deleted");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.warning("Role not found with ID: " + id);
            return ResponseEntity.notFound().build();
        }
    }
}