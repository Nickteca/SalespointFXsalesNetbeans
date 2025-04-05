package com.salespointfxsales.www.model;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SucursalPedido {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer SucursalPedido;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@JoinColumn(name = "sucursal", referencedColumnName = "idSucursal")
	@ManyToOne(optional = false)
	private Sucursal sucursal;

	@OneToMany(mappedBy = "sucursalPedido", cascade = CascadeType.ALL)
	private List<SucursalPedidoDetalle> listSucursalpedidoDetalle;
}
