package com.salespointfxsales.www.service;

import com.salespointfxsales.www.model.Gasto;
import com.salespointfxsales.www.repo.GastoRepo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GastoService {
    private final GastoRepo gr;
    
    public List<Gasto> findAll(){
        return gr.findAll();
    }
}
