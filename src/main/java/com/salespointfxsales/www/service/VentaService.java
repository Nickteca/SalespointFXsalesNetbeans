package com.salespointfxsales.www.service;

import com.salespointfxsales.www.repo.VentaDetalleRepo;
import com.salespointfxsales.www.repo.VentaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VentaService {
    private final VentaRepo vr;
    private final VentaDetalleRepo vdr;
    
    
}
