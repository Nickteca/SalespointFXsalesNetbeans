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
public class VentaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVentaDetalle;

    @Column(nullable = false)
    private float cantidad;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    
    @Column(nullable = false)
    private float precio;

    @Column(nullable = false)
    private float subTotal;

    @JoinColumn(name = "sucursalProducto", referencedColumnName = "idSucursalProducto")
    @ManyToOne(optional = false)
    private SucursalProducto sucursalProducto;

    @JoinColumn(name = "venta", referencedColumnName = "idVenta")
    @ManyToOne(optional = false)
    private Venta venta;
}
