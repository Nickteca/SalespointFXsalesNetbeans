package com.salespointfxsales.www.model;

import java.time.LocalDateTime;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private short idUsuario;

	@Column(nullable = false, length = 20, unique = true)
	private String nombreUsuario;

	@Column(nullable = false, length = 100)
	private String passwordUsuario;

	@Basic(optional = false)
	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Basic(optional = true)
	@Column(nullable = true)
	private LocalDateTime updatedAt;

	@Basic(optional = true)
	@Column(nullable = true)
	private LocalDateTime deletedAt;

	@JoinColumn(name = "rolIdRol", referencedColumnName = "idRol")
	@ManyToOne(optional = false)
	private UsuarioRol rolIdRol;

	public Usuario(String nombreUsuario, String passwordUsuario, UsuarioRol rolIdRol) {
		super();
		this.nombreUsuario = nombreUsuario;
		this.passwordUsuario = passwordUsuario;
		this.createdAt = LocalDateTime.now();
		this.rolIdRol = rolIdRol;
	}

}