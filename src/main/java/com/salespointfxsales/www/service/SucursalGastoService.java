package com.salespointfxsales.www.service;

import com.salespointfxsales.www.model.SucursalGasto;
import com.salespointfxsales.www.repo.SucursalGastoRepo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SucursalGastoService {
    private final SucursalGastoRepo sgr;
    
    public List<SucursalGasto> findBySucursalEstatusSucursalTrueAndCreatedAtBetween(LocalDate inicio, LocalDate fin){
        return sgr.findBySucursalEstatusSucursalTrueAndCreatedAtBetween(inicio.atStartOfDay(), fin.atTime(11, 59, 59));
    }
}
