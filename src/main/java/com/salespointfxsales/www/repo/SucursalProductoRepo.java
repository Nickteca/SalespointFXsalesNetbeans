package com.salespointfxsales.www.repo;

import com.salespointfxsales.www.model.Categoria;
import com.salespointfxsales.www.model.Producto;
import com.salespointfxsales.www.model.Sucursal;
import com.salespointfxsales.www.model.SucursalProducto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SucursalProductoRepo extends JpaRepository<SucursalProducto, Short>{
    List<SucursalProducto> findBySucursalEstatusSucursalTrueAndVendibleTrue();
    List<SucursalProducto> findByCategoriaAndSucursalEstatusSucursalTrueAndVendibleTrue(Categoria categoria);
    SucursalProducto findByIdSucursalProducto(Short id);
    Optional<SucursalProducto> findByProductoAndSucursal(Producto p, Sucursal s);
    List<SucursalProducto> findByInventariableTrue();
}