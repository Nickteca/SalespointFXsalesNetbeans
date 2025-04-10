package com.salespointfxsales.www.model;

import com.salespointfxadmin.www.model.enums.Naturaleza;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = { @Index(name = "indexFechaVenta", columnList = "createdAt"), @Index(name = "indexFolii",columnList = "folio") })
public class Venta{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVenta;
    
	@Column(nullable = false, length = 17)
	private String folio;

	@Column(nullable = false)
	private boolean status;

        @Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Naturaleza naturalezaVenta;

	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Column(nullable = false)
	private float totalVenta;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(nullable = true)
	private LocalDateTime updatedAt;

	@JoinColumn(name = "sucursal", referencedColumnName = "idSucursal")
	@ManyToOne(optional = false)
	private Sucursal sucursal;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "venta")
	private List<VentaDetalle> listVentaDetalle;
}