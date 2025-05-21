package com.salespointfxsales.www.repo;

import com.salespointfxsales.www.model.Configuracion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfiguracionRepo extends JpaRepository<Configuracion, Integer>{
    Configuracion findByClave(String clave);
}
