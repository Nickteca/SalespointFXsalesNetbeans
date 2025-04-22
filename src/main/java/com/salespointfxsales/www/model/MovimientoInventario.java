package com.salespointfxsales.www.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.salespointfxadmin.www.model.enums.Naturaleza;
import com.salespointfxsales.www.model.enums.NombreFolio;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = { @Index(name = "indice", columnList = "folioCompuesto"), @Index(name = "indice_fecha", columnList = "createdAt") })
public class MovimientoInventario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idMovimientoInventario;

	@Column(nullable = false, length = 17, unique = true)
	private String folioCompuesto;

	@Column(nullable = false)
	private Naturaleza naturaleza;

	@Column(nullable = false)
	private NombreFolio nombreFolio;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(nullable = true, length = 50)
	private String decripcion;

	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@JoinColumn(name = "sucursal", referencedColumnName = "idSucursal")
	@ManyToOne(optional = false)
	private Sucursal sucursal;

	@JoinColumn(name = "sucursalDestino", referencedColumnName = "idSucursal")
	@ManyToOne(optional = true)
	private Sucursal sucursalDestino;

	@JoinColumn(name = "folio", referencedColumnName = "idFolio")
	@ManyToOne(optional = false)
	private Folio folio;

	@OneToMany(mappedBy = "movimientoInventario", cascade = CascadeType.ALL)
	private List<MovimientoInventarioDetalle> listMovimientoInventarioDetalle;

	public MovimientoInventario(Integer idMovimientoInventario, String folioCompuesto, Naturaleza naturaleza, NombreFolio nombreFolio, String decripcion, Sucursal sucursal, Sucursal sucursaldestino,
			Folio folio) {
		super();
		this.idMovimientoInventario = idMovimientoInventario;
		this.folioCompuesto = folioCompuesto;
		this.naturaleza = naturaleza;
		this.nombreFolio = nombreFolio;
		this.decripcion = decripcion;
		this.sucursal = sucursal;
		this.folio = folio;
		this.sucursalDestino = sucursaldestino;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	public MovimientoInventario(Integer idMovimientoInventario, String folioCompuesto, Naturaleza naturaleza, NombreFolio nombreFolio, String decripcion, Sucursal sucursal, Sucursal sucursaldestino,
			List<MovimientoInventarioDetalle> listMovimientoInventarioDetalle) {
		super();
		this.idMovimientoInventario = idMovimientoInventario;
		this.folioCompuesto = folioCompuesto;
		this.naturaleza = naturaleza;
		this.nombreFolio = nombreFolio;
		this.decripcion = decripcion;
		this.sucursal = sucursal;
		this.sucursalDestino = sucursaldestino;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
		this.listMovimientoInventarioDetalle = listMovimientoInventarioDetalle;
	}

	public MovimientoInventario(Integer idMovimientoInventario, String folioCompuesto, Naturaleza naturaleza) {
		super();
		this.idMovimientoInventario = idMovimientoInventario;
		this.folioCompuesto = folioCompuesto;
		this.naturaleza = naturaleza;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

}
