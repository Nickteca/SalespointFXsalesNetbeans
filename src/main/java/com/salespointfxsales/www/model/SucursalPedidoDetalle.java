package com.salespointfxsales.www.model;

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
public class SucursalPedidoDetalle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idSucursalPedidoDetalle;

	@Column(nullable = false)
	private float cantidad;

	@JoinColumn(name = "sucursalProducto", referencedColumnName = "idSucursalProducto")
	@ManyToOne(optional = false)
	private SucursalProducto sucursalProducto;

	@JoinColumn(name = "sucursalPedido", referencedColumnName = "SucursalPedido")
	@ManyToOne(optional = false)
	private SucursalPedido sucursalPedido;

	public SucursalPedidoDetalle(Integer idSucursalPedidoDetalle, float cantidad, SucursalProducto sucursalProducto) {
		super();
		this.idSucursalPedidoDetalle = idSucursalPedidoDetalle;
		this.cantidad = cantidad;
		this.sucursalProducto = sucursalProducto;
	}

}
