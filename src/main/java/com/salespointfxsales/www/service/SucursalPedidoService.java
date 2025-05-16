package com.salespointfxsales.www.service;

import com.salespointfxsales.www.repo.SucursalPedidoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SucursalPedidoService {
    private final SucursalPedidoRepo spr;
}
