package com.salespointfxsales.www.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductoPaquete implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Short idProductoPaquete;

	@JoinColumn(name = "paquete", referencedColumnName = "idProducto")
	@ManyToOne(optional = false)
	private Producto paquete;

	@Column(nullable = false)
	private float cantidad;

	@JoinColumn(name = "productoPaquete", referencedColumnName = "idProducto")
	@ManyToOne(optional = false)
	private Producto productoPaquete;

	public ProductoPaquete(Producto paquete, float cantidad, Producto productoPaquete) {
		super();
		this.paquete = paquete;
		this.cantidad = cantidad;
		this.productoPaquete = productoPaquete;
	}

	public ProductoPaquete(float cantidad, Producto productoPaquete) {
		super();
		this.cantidad = cantidad;
		this.productoPaquete = productoPaquete;
	}

	@Override
	public String toString() {
		// Verificar si la cantidad tiene decimales
		if (cantidad == (int) cantidad) {
			// Si no tiene decimales, mostrar como entero
			return productoPaquete.getNombreProducto() + "-" + (int) cantidad;
		} else {
			// Si tiene decimales, mostrar con un solo decimal
			return productoPaquete.getNombreProducto() + "-" + String.format("%.1f", cantidad);
		}
	}

}
