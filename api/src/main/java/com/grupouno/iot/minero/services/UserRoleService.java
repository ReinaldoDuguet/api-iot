package com.grupouno.iot.minero.services;

import com.grupouno.iot.minero.models.UserRole;
import java.util.List;

public interface UserRoleService {
    List<UserRole> getAllUserRoles();
    UserRole getUserRoleById(Long id);
    UserRole createUserRole(UserRole userRole);
    UserRole updateUserRole(Long id, UserRole userRole);
    void deleteUserRole(Long id);
    List<UserRole> findByUserId(Long userId);
    List<UserRole> findByRoleId(Long roleId);
}