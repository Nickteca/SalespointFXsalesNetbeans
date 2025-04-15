package com.salespointfxsales.www.repo;

import com.salespointfxsales.www.model.Billete;
import com.salespointfxsales.www.model.enums.BilleteValor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BilleteRepo extends JpaRepository<Billete, Integer>{
    Billete findByBilleteValor(BilleteValor bv);
}
