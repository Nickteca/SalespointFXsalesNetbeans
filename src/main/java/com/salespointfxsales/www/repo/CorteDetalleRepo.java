package com.salespointfxsales.www.repo;

import com.salespointfxsales.www.model.Corte;
import com.salespointfxsales.www.model.CorteDetalle;
import com.salespointfxsales.www.model.SucursalProducto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CorteDetalleRepo extends JpaRepository<CorteDetalle, Integer> {

    @Query("SELECT cd FROM CorteDetalle cd WHERE cd.sucursalProducto = :sp AND cd.corte.sucursal.estatusSucursal = true ORDER BY cd.corte.idCorte DESC")
    List<CorteDetalle> findUltimoPorProducto(@Param("sp") SucursalProducto sp);
    
    //List<CorteDetalle> findBySucursalProductoAndCorteSucursalEstatusSucursalTrueOrderBycorteDesc(SucursalProducto sp);
   CorteDetalle findByCorteAndSucursalProducto(Corte corte, SucursalProducto sp);
}
