package com.salespointfxsales.www.service;

import com.salespointfxsales.www.model.Producto;
import com.salespointfxsales.www.model.ProductoPaquete;
import com.salespointfxsales.www.repo.ProductoPaqueteRepo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductoPaqueteService {
    private final ProductoPaqueteRepo ppr;
    
    public List<ProductoPaquete> findByPaquete(Producto paquete){
        return ppr.findByPaquete(paquete);
    }
}
