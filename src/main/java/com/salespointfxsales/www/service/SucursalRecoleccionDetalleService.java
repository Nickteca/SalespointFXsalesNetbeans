package com.salespointfxsales.www.service;

import com.salespointfxsales.www.repo.SucursalRecoleccionDetalleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SucursalRecoleccionDetalleService {
    private final SucursalRecoleccionDetalleRepo srdr;
}
