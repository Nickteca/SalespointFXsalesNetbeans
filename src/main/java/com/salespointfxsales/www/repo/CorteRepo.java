package com.salespointfxsales.www.repo;

import com.salespointfxsales.www.model.Corte;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorteRepo extends JpaRepository<Corte, Integer>{
    Optional<Corte> findFirstBySucursalEstatusSucursalTrueOrderByIdCorteDesc();
}
