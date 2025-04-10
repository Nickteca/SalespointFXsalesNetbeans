package com.salespointfxsales.www.repo;

import com.salespointfxsales.www.model.Producto;
import com.salespointfxsales.www.model.ProductoPaquete;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoPaqueteRepo extends JpaRepository<ProductoPaquete, Short>{
    List<ProductoPaquete> findByPaquete(Producto paquete);
}
