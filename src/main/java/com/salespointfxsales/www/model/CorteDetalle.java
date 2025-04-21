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
public class CorteDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCorteDetalle;

    @JoinColumn(name = "corte", referencedColumnName = "idCorte")
    @ManyToOne(optional = false)
    private Corte corte;

    @JoinColumn(name = "sucursalProducto", referencedColumnName = "idSucursalProducto")
    @ManyToOne(optional = false)
    private SucursalProducto sucursalProducto;

    @Column(nullable = false)
    private float entrada;

    @Column(nullable = false)
    private float venta;

    @Column(nullable = false)
    private float salida;

    @Column(nullable = false)
    private float traspasoSalida;

    @Column(nullable = false)
    private float traspasoEntrada;

    @Column(nullable = false)
    private float canceladas;

    @Column(nullable = false)
    private float existencia;
}
