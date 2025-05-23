package com.salespointfxsales.www.model;

import io.micrometer.common.lang.Nullable;
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
public class SucursalRecoleccionDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSucursalRecoleccionDetalle;

    @Column(nullable = false)
    private short cantidad;
    
    @Column(nullable = false)
    private float subTotal;

    @JoinColumn(name = "billete", referencedColumnName = "idBillete")
    @ManyToOne(optional = false)
    private Billete billete;

    @JoinColumn(name = "sucursalRecoleccion", referencedColumnName = "idSucursalRecoleccion")
    @ManyToOne(optional = false)
    private SucursalRecoleccion sucursalRecoleccion;
}
