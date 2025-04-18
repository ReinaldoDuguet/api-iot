package com.grupouno.iot.minero.services;

import com.grupouno.iot.minero.models.Role;
import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> getAllRoles();
    Optional<Role> getRoleById(Long id);
    Role createRole(Role newRole);
    Role updateRole(Long id, Role updatedRole);
    void deleteRole(Long id);
}