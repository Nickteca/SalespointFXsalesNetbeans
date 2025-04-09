package com.salespointfxsales.www.repo;

import com.salespointfxadmin.www.enums.NombreFolio;
import com.salespointfxsales.www.model.Folio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FolioRepo extends JpaRepository<Folio, Short>{
    Folio findByNombreFolio(NombreFolio nf);
    
    @Modifying
    @Query("UPDATE Folio f SET f.numeroFolio = f.numeroFolio + 1 WHERE f.idFolio = :idFolio")
    int incrementarNumeroFolio(@Param("idFolio") Short idFolio);
}
