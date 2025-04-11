package com.salespointfxsales.www.service;

import com.salespointfxsales.www.repo.GastoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GastoService {
    private final GastoRepo gr;
}
