package com.salespointfxsales.www.service;

import com.salespointfxsales.www.model.Sucursal;
import com.salespointfxsales.www.repo.SucursalRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SucursalService {
    private final SucursalRepo sr;
    
    public Sucursal findByEstatusSucursalTrue(){
        return sr.findByEstatusSucursalTrue().orElse(null);
    }
}
