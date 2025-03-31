package com.grupouno.iot.minero.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupouno.iot.minero.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}