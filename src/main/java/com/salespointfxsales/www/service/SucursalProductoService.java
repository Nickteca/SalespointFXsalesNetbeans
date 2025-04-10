package com.salespointfxsales.www.service;

import com.salespointfxsales.www.model.Categoria;
import com.salespointfxsales.www.model.SucursalProducto;
import com.salespointfxsales.www.repo.SucursalProductoRepo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SucursalProductoService {
    private final SucursalProductoRepo spr;
    
    public List<SucursalProducto> findBySucursalEsttusSucursalTrueAndVendibleTrue(){
        return spr.findBySucursalEstatusSucursalTrueAndVendibleTrue();
    }
    
    public List<SucursalProducto> findByCategoriaAndSucursalEstatusSucursalTrueAndVendibleTrue(Categoria categoria){
        return spr.findByCategoriaAndSucursalEstatusSucursalTrueAndVendibleTrue(categoria);
    }
    
    public SucursalProducto findByIdSucursalProducto(short id){
        return spr.findByIdSucursalProducto(id);
    }
    
    public List<SucursalProducto> findByInventariable(){
        return spr.findByInventariableTrue();
    }
}
