package com.grupouno.iot.minero.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupouno.iot.minero.models.User;
import com.grupouno.iot.minero.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public Optional<User> getUserById(Long id) {
		return userRepository.findById(id);
	}

	public User updateUser(Long id, User updatedUser) {
		return userRepository.findById(id).map(user -> {
			user.setUsername(updatedUser.getUsername());
			user.setPasswordHash(updatedUser.getPasswordHash());
			user.setEnabled(updatedUser.isEnabled());
			user.setBlocked(updatedUser.isBlocked());
			user.setCredentialsExpired(updatedUser.isCredentialsExpired());
			user.setUpdatedAt(LocalDateTime.now()); // Actualizar la fecha de modificación
			return userRepository.save(user);
		}).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
	}

	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}
}