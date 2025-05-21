package com.salespointfxsales.www.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Configuracion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idConfiguracion;
    
    @Column(length = 50, nullable = false, unique = true)
    private String clave;
    
    @Column(length = 50, nullable = false)
    private String valor;
    
    @Column(length = 50, nullable = false)
    private String descripcion;

    @JoinColumn(name = "sucursal", referencedColumnName = "idSucursal")
    @ManyToOne(optional = false)
    private Sucursal sucursal;
}
