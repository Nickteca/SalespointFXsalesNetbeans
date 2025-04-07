package com.salespointfxsales.www.service;

import com.salespointfxsales.www.model.Categoria;
import com.salespointfxsales.www.repo.CategoriaRepo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepo cr;
    
    public List<Categoria> findAll(){
        return cr.findAll();
    }
}
