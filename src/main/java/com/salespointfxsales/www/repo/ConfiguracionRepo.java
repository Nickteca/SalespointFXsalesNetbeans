package com.salespointfxsales.www.repo;

import com.salespointfxsales.www.model.Configuracion;
import com.salespointfxsales.www.model.Sucursal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfiguracionRepo extends JpaRepository<Configuracion, Integer>{
    Configuracion findByClave(String clave);
    List<Configuracion> findByClaveAndSucursalEstatusSucursalTrue(String clave);
    //List<Configuracion> findByClave(String clave);
}
