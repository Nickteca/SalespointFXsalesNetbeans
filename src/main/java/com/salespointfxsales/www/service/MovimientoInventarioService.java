package com.salespointfxsales.www.service;

import com.salespointfxadmin.www.enums.NombreFolio;
import com.salespointfxsales.www.model.MovimientoInventario;
import com.salespointfxsales.www.repo.MovimientoInventarioRepo;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovimientoInventarioService {
    private final MovimientoInventarioRepo mir;
    
    public List<MovimientoInventario> findBySucursalEstatusSucursalTrueAndCreatedAtBetweenAndNombreFolio(LocalDate startTime, LocalDate endTime, NombreFolio nombreFolio){
        try {
            return mir.findBySucursalEstatusSucursalTrueAndCreatedAtBetweenAndNombreFolio(startTime.atStartOfDay(), endTime.atTime(23, 59, 59), nombreFolio);
        } catch (Exception e) {
            throw e;
        }
    }
}
