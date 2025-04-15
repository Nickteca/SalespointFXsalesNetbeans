package com.salespointfxsales.www.service;

import com.salespointfxsales.www.model.Billete;
import com.salespointfxsales.www.model.enums.BilleteValor;
import com.salespointfxsales.www.repo.BilleteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BilleteService {
    private final BilleteRepo br;
     public Billete findByBillete(BilleteValor bv){
         //System.out.println("El billete es: "+bv);
         try {
             if(bv.equals(null)){
                 throw new IllegalArgumentException("El valor del billete es nulo");
             }
             //System.out.println("este e sel billete: "+br.findByBilleteValor(bv));
             return br.findByBilleteValor(bv);
         } catch (IllegalArgumentException e) {
             throw e;
         }
     }
}
