package com.salespointfxsales.www.model;

import com.salespointfxsales.www.model.enums.BilleteValor;
import java.io.Serializable;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Billete implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idBillete;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private BilleteValor billete;

	@Column(nullable = false)
	private int cantidad;

	@Column(nullable = false)
	private int subtotal;

	@JoinColumn(name = "sucursalRecoleccion", referencedColumnName = "idSucursalRecoleccion")
	@ManyToOne(optional = false)
	private SucursalRecoleccion sucursalRecoleccion;

	public Billete(Integer idBillete, BilleteValor billete, int cantidad, int subtotal) {
		super();
		this.idBillete = idBillete;
		this.billete = billete;
		this.cantidad = cantidad;
		this.subtotal = subtotal;
	}

}
