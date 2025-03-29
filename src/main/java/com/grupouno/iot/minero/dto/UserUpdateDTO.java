package com.grupouno.iot.minero.dto;

import com.grupouno.iot.minero.models.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {
    private String username;
    private String password; // puede ser opcional
    private boolean enabled;
    private boolean blocked;
    private boolean credentialsExpired;

    public static User toEntity(UserUpdateDTO dto, User existingUser) {
        existingUser.setUsername(dto.getUsername());
        existingUser.setEnabled(dto.isEnabled());
        existingUser.setBlocked(dto.isBlocked());
        existingUser.setCredentialsExpired(dto.isCredentialsExpired());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            existingUser.setPasswordHash(dto.getPassword()); // debe llegar hasheado antes de guardar
        }

        return existingUser;
    }
}
