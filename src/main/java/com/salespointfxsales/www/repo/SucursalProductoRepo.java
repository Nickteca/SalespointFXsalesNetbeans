/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.salespointfxsales.www.repo;

import com.salespointfxsales.www.model.Categoria;
import com.salespointfxsales.www.model.SucursalProducto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Sistemas
 */
public interface SucursalProductoRepo extends JpaRepository<SucursalProducto, Short>{
    List<SucursalProducto> findBySucursalEstatusSucursalTrueAndVendibleTrue();
    List<SucursalProducto> findByCategoriaAndSucursalEstatusSucursalTrueAndVendibleTrue(Categoria categoria);
}
