package com.salespointfxsales.www.service;

import com.salespointfxsales.www.model.enums.NombreFolio;
import com.salespointfxsales.www.model.Folio;
import com.salespointfxsales.www.repo.FolioRepo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FolioService {

    private final FolioRepo fr;
    
    public List<Folio> findBySucursalEstatusSucursalTrue(){
        return fr.findBySucursalEstatusSucursalTrue();
    }

    public Folio getFolioVenta() {
        return fr.findByNombreFolio(NombreFolio.Venta);
    }

    @Transactional
    public int updateFolioVenta(Folio folio) {
        return fr.incrementarNumeroFolio(folio.getIdFolio());
    }
}
