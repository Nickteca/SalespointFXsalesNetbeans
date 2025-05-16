package com.salespointfxsales.www.service;

import com.salespointfxsales.www.repo.SucursalPedidoDetalleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SucursalPedidoDetalleService {
    private final SucursalPedidoDetalleRepo spdr;
}
