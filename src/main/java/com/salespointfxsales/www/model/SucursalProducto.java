package com.salespointfxsales.www.model;

import java.io.Serializable;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SucursalProducto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short idSucursalProducto;

    @Column(nullable = false)
    private float inventario;

    @Column(nullable = false)
    private float precio;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private boolean vendible;
    
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean inventariable;

    @JoinColumn(name = "producto", referencedColumnName = "idProducto")
    @ManyToOne(optional = false)
    private Producto producto;

    @JoinColumn(name = "categoria", referencedColumnName = "idCategoria")
    @ManyToOne(optional = false)
    private Categoria categoria;

    @JoinColumn(name = "sucursal", referencedColumnName = "idSucursal")
    @ManyToOne(optional = false)
    private Sucursal sucursal;

    @OneToMany(mappedBy = "sucursalProducto", cascade = CascadeType.ALL)
    private List<MovimientoInventarioDetalle> listMovimientoInventarioDetalle;

    public SucursalProducto(float inventario, float precio, boolean vendible, Producto producto, Categoria categoria, Sucursal sucursal) {
        super();
        this.inventario = inventario;
        this.precio = precio;
        this.vendible = vendible;
        this.producto = producto;
        this.categoria = categoria;
        this.sucursal = sucursal;
    }

    @Override
    public String toString() {
        return  producto.getNombreProducto();
    }

    public SucursalProducto(Short idSucursalProducto, float inventario, float precio, boolean vendible, Producto producto, Categoria categoria, Sucursal sucursal) {
        super();
        this.idSucursalProducto = idSucursalProducto;
        this.inventario = inventario;
        this.precio = precio;
        this.vendible = vendible;
        this.producto = producto;
        this.categoria = categoria;
        this.sucursal = sucursal;
    }

    public SucursalProducto(Short idSucursalProducto, float inventario, Producto producto) {
        super();
        this.idSucursalProducto = idSucursalProducto;
        this.inventario = inventario;
        this.producto = producto;
    }

    public SucursalProducto(Short idSucursalProducto) {
        this.idSucursalProducto = idSucursalProducto;
    }

}
