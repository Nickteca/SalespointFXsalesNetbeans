package com.salespointfxsales.www.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Gasto implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Short idGasto;

	@Column(nullable = false, unique = true)
	private String descripcionGasto;

	@Column(nullable = false)
	private LocalDateTime createdt;

	@OneToMany(mappedBy = "gasto")
	private List<SucursalGasto> listGastoSucursal;

	public Gasto(Short idGasto, String descripcionGasto) {
		super();
		this.idGasto = idGasto;
		this.descripcionGasto = descripcionGasto;
		this.createdt = LocalDateTime.now();
	}

	@Override
	public String toString() {
		return descripcionGasto;
	}
}