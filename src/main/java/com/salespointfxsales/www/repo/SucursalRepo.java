package com.salespointfxsales.www.repo;

import com.salespointfxsales.www.model.Sucursal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SucursalRepo extends JpaRepository<Sucursal, Integer> {
    Optional<Sucursal> findByEstatusSucursalTrue();
}
