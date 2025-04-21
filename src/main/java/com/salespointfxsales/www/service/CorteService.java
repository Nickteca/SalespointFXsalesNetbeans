package com.salespointfxsales.www.service;

import com.salespointfxsales.www.repo.CorteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CorteService {
    private final CorteRepo cr;
}
