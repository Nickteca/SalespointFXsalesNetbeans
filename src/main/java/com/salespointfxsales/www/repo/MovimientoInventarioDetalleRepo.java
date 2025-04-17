package com.salespointfxsales.www.repo;

import com.salespointfxsales.www.model.MovimientoInventario;
import com.salespointfxsales.www.model.MovimientoInventarioDetalle;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoInventarioDetalleRepo extends JpaRepository<MovimientoInventarioDetalle, Integer>{
 List<MovimientoInventarioDetalle> findByMovimientoInventario(MovimientoInventario mi);   
}
