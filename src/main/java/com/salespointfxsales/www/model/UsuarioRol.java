package com.salespointfxsales.www.model;
import java.io.Serializable;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRol implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private short idRol;

	@Column(nullable = false, length = 20)
	private String nombreRol;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "rolIdRol")
	private List<Usuario> listUsuario;

	public UsuarioRol(String nombreRol) {
		super();
		this.nombreRol = nombreRol;
	}

}
