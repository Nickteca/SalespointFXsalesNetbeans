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
@Table(indexes = @Index(name = "creado", columnList = "createdAt"))
public class SucursalRecoleccion implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idSucursalRecoleccion;

	@Column(nullable = false)
	private float totalRecoleccion;

	@Column(nullable = false)
	private LocalDateTime createdAt;
        
        @OneToMany(cascade = CascadeType.ALL, mappedBy = "sucursalRecoleccion")
	private List<SucursalRecoleccionDetalle> listSucursalRecoleccionDetalle;

	@JoinColumn(name = "sucursal", referencedColumnName = "idSucursal")
	@ManyToOne(optional = false)
	private Sucursal sucursal;

	
}