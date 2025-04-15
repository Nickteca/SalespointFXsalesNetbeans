package com.salespointfxsales.www.model;

import com.salespointfxsales.www.model.enums.BilleteValor;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import java.util.List;
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
	private BilleteValor billeteValor;


	@OneToMany(mappedBy = "billete", cascade = CascadeType.ALL)
	private List<SucursalRecoleccionDetalle> listSucursalRecoleccionDetalle;
}
