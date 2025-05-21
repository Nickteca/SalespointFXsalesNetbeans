package com.salespointfxsales.www.service;

import com.salespointfxsales.www.model.Configuracion;
import com.salespointfxsales.www.repo.ConfiguracionRepo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfiguracionService {

    private final ConfiguracionRepo cr;

    public List<Configuracion> findAll() {
        try {
            return cr.findAll();
        } catch (Exception e) {
            throw e;
        }
    }

    public String getValor(String clave) {
        return cr.findByClave(clave).getValor();
    }

    public Configuracion save(Configuracion configuracion) {
        return cr.save(configuracion);
    }
    
    public void delete(Configuracion configuracion){
        cr.delete(configuracion);
    }
}
