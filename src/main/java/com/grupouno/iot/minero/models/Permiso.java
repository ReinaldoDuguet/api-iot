package com.grupouno.iot.minero.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "permisos")
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "permiso", nullable = false)
    private String permiso;

    @Column(name = "estado", nullable = false)
    private boolean estado;

    @Column(name = "creacion", nullable = false)
    private LocalDateTime creacion;
    
    // Getters y Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPermiso() {
		return permiso;
	}

	public void setPermiso(String permiso) {
		this.permiso = permiso;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public LocalDateTime getCreacion() {
		return creacion;
	}

	public void setCreacion(LocalDateTime creacion) {
		this.creacion = creacion;
	}
    
}
