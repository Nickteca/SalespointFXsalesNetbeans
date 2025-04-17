package com.salespointfxsales.www.model;

import java.io.Serializable;
import java.util.List;

import com.salespointfxadmin.www.model.enums.Naturaleza;
import com.salespointfxadmin.www.enums.NombreFolio;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "folio", uniqueConstraints = { @UniqueConstraint(columnNames = "idFolio") })
public class Folio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(nullable = false)
	private Short idFolio;

	@Column(nullable = false, length = 4)
	private String acronimoFolio;

	@Column(nullable = false)
	private int numeroFolio;

        @Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Naturaleza naturalezaFolio;

        @Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private NombreFolio nombreFolio;
	
	

	@JoinColumn(name = "sucursal", referencedColumnName = "idSucursal")
	@ManyToOne(optional = false)
	private Sucursal sucursal;
	
	@OneToMany(mappedBy = "folio", cascade = CascadeType.ALL)
	private List<MovimientoInventario> listMovimientoInventario;

	public Folio(String acronimoFolio, int numeroFolio, Naturaleza naturalezaFolio, Sucursal sucursal, NombreFolio nombreFolio) {
		super();
		this.acronimoFolio = acronimoFolio;
		this.numeroFolio = numeroFolio;
		this.naturalezaFolio = naturalezaFolio;
		this.sucursal = sucursal;
		this.nombreFolio = nombreFolio;
	}

	public String folioCompuesto() {
		return acronimoFolio + "" + sucursal.getIdSucursal() + "-" + numeroFolio + "";
	}
	
	public String folioCompuestoEditing() {
		return acronimoFolio + "" + sucursal.getIdSucursal() + "-" + (numeroFolio-1) + "";
	}

	@Override
	public String toString() {
		return nombreFolio.toString();
	}

}