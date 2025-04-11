package com.salespointfxsales.www.repo;

import com.salespointfxsales.www.model.Venta;
import com.salespointfxsales.www.model.VentaDetalle;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VentaDetalleRepo extends JpaRepository<VentaDetalle, Integer> {

    @Query("SELECT vd FROM VentaDetalle vd WHERE vd.venta.sucursal.estatusSucursal = true AND vd.venta.createdAt BETWEEN :fechaApertura AND :fechaVenta")
    List<VentaDetalle> findResumenVentasPorSucursalProducto(
            @Param("fechaApertura") LocalDateTime fechaApertura,
            @Param("fechaVenta") LocalDateTime fechaVenta
    );
}
