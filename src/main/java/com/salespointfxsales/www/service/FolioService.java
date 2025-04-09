package com.salespointfxsales.www.service;

import com.salespointfxadmin.www.enums.NombreFolio;
import com.salespointfxsales.www.model.Folio;
import com.salespointfxsales.www.repo.FolioRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FolioService {

    private final FolioRepo fr;

    public Folio getFolioVenta() {
        return fr.findByNombreFolio(NombreFolio.Venta);
    }

    @Transactional
    public int updateFolioVenta(Folio folio) {
        return fr.incrementarNumeroFolio(folio.getIdFolio());
    }
}
