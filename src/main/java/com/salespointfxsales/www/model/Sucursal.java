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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "idSucursal", "nombreSucursal" }))
public class Sucursal implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Integer idSucursal;

	@Column(length = 30, nullable = false)
	private String nombreSucursal;

	@Column(length = 70, nullable = false)
	private String calleSucursal;

	@Column(length = 50, nullable = false)
	private String ciudadSucursal;

	@Column(length = 30, nullable = false)
	private String estadoSucursal;

	@Column(length = 15, nullable = false)
	private String telefonoSucursal;

	@Column(nullable = false)
	private boolean estatusSucursal;

	@Column(nullable = false)
	private LocalDateTime cretaedAt;

	@JoinColumn(name = "empresa", referencedColumnName = "idEmpresa")
	@ManyToOne(optional = false)
	private Empresa empresa;

	@OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL)
	private List<SucursalProducto> listSucursalProductos;

	@OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL)
	private List<MovimientoCaja> listMovimientoCaja;

	@OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL)
	private List<Folio> listFolio;

	@OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL)
	private List<SucursalGasto> listSucursalGasto;

	@OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL)
	private List<MovimientoInventario> listMovmientoInventario;

	@OneToMany(mappedBy = "sucursalDestino", cascade = CascadeType.ALL)
	private List<MovimientoInventario> listSucursalDestino;

	@OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL)
	private List<SucursalRecoleccion> listSucursalRecoleccion;

	@OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL)
	private List<SucursalPedido> listSucursalPedido;

	public Sucursal(Integer idSucursal, String nombreSucursal, String calleSucursal, String ciudadSucursal, String estadoSucursal, String telefonoSucursal, boolean estatusSucursal, Empresa empresa) {
		super();
		this.idSucursal = idSucursal;
		this.nombreSucursal = nombreSucursal;
		this.calleSucursal = calleSucursal;
		this.ciudadSucursal = ciudadSucursal;
		this.estadoSucursal = estadoSucursal;
		this.telefonoSucursal = telefonoSucursal;
		this.estatusSucursal = estatusSucursal;
		this.cretaedAt = LocalDateTime.now();
		this.empresa = empresa;
	}

	@Override
	public String toString() {
		return this.idSucursal + " " + this.nombreSucursal;
	}

}
