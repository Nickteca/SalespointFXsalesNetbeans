package com.salespointfxsales.www.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(/* indexes = { @Index(name = "creado", columnList = "createdAt") }, */uniqueConstraints = @UniqueConstraint(columnNames = { "idEmpresa", "createdAt" }))
public class Empresa implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Short idEmpresa;

	@Column(length = 50, nullable = false)
	private String nombreEmpresa;

	@Column(length = 100, nullable = false)
	private String direccionEmpresa;

	@Column(length = 12, nullable = false)
	private String telefonoEmpresa;

	@Column(length = 15, nullable = false)
	private String rfcEmpresa;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
	private List<Sucursal> listSucursal;

	public Empresa(Short idEmpresa, String nombreEmpresa, String direccionEmpresa, String telefonoEmpresa, String rfcEmpresa, LocalDateTime createdAt) {
		super();
		this.idEmpresa = idEmpresa;
		this.nombreEmpresa = nombreEmpresa;
		this.direccionEmpresa = direccionEmpresa;
		this.telefonoEmpresa = telefonoEmpresa;
		this.rfcEmpresa = rfcEmpresa;
		this.createdAt = createdAt;
	}

}
