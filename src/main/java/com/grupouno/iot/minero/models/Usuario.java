package com.grupouno.iot.minero.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(name = "nombre_usuario", nullable = false, unique = true)
	    private String nombreUsuario;

	    @Column(name = "clave", nullable = false)
	    private String clave;

	    @Column(name = "cuenta_habilitada")
	    private boolean cuentaHabilitada;

	    @Column(name = "cuenta_vencida")
	    private boolean cuentaVencida;

	    @Column(name = "cuenta_bloqueada")
	    private boolean cuentaBloqueada;

	    @Column(name = "credenciales_vencidas")
	    private boolean credencialesVencidas;

	    // Relación con Rol (Many-to-Many)
	    @ManyToMany
	    @JoinTable(
	        name = "usuarios_roles",
	        joinColumns = @JoinColumn(name = "id_usuario"),
	        inverseJoinColumns = @JoinColumn(name = "id_rol")
	    )
	    private List<Rol> roles;
	    
	    // Getters y Setters

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getNombreUsuario() {
			return nombreUsuario;
		}

		public void setNombreUsuario(String nombreUsuario) {
			this.nombreUsuario = nombreUsuario;
		}

		public String getClave() {
			return clave;
		}

		public void setClave(String clave) {
			this.clave = clave;
		}

		public boolean isCuentaHabilitada() {
			return cuentaHabilitada;
		}

		public void setCuentaHabilitada(boolean cuentaHabilitada) {
			this.cuentaHabilitada = cuentaHabilitada;
		}

		public boolean isCuentaVencida() {
			return cuentaVencida;
		}

		public void setCuentaVencida(boolean cuentaVencida) {
			this.cuentaVencida = cuentaVencida;
		}

		public boolean isCuentaBloqueada() {
			return cuentaBloqueada;
		}

		public void setCuentaBloqueada(boolean cuentaBloqueada) {
			this.cuentaBloqueada = cuentaBloqueada;
		}

		public boolean isCredencialesVencidas() {
			return credencialesVencidas;
		}

		public void setCredencialesVencidas(boolean credencialesVencidas) {
			this.credencialesVencidas = credencialesVencidas;
		}

		public List<Rol> getRoles() {
			return roles;
		}

		public void setRoles(List<Rol> roles) {
			this.roles = roles;
		}	    
	    


}
