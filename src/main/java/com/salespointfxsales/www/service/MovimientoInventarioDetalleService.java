package com.salespointfxsales.www.service;

import com.salespointfxsales.www.model.MovimientoInventario;
import com.salespointfxsales.www.model.MovimientoInventarioDetalle;
import com.salespointfxsales.www.repo.MovimientoInventarioDetalleRepo;
import com.salespointfxsales.www.repo.MovimientoInventarioRepo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovimientoInventarioDetalleService {
    private final MovimientoInventarioDetalleRepo midr;
    
    public List<MovimientoInventarioDetalle> findByMovimientoInventario(MovimientoInventario mi){
        return midr.findByMovimientoInventario(mi);
    }
}
