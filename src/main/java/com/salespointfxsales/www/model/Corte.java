package com.salespointfxsales.www.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {
    @Index(name = "creado", columnList = "apertuta"),
    @Index(name = "cerrado", columnList = "cierre")})
public class Corte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCorte;

    @JoinColumn(name = "sucursal", referencedColumnName = "idSucursal")
    @ManyToOne(optional = false)
    private Sucursal sucursal;

    @JoinColumn(name = "movimientoCaja", referencedColumnName = "idMovimientoCaja")
    @OneToOne(optional = false)
    private MovimientoCaja movimientoCaja;

    @Column(nullable = false)
    private float nicial;

    @Column(nullable = false)
    private float ventas;

    @Column(nullable = false)
    private float recoleccion;

    @Column(nullable = false)
    private float gasto;

    @Column(nullable = false)
    private LocalDateTime apertuta;

    @Column(nullable = false)
    private LocalDateTime cierre;

    @Column(nullable = false)
    private LocalDateTime numFolios;

    @Column(nullable = false)
    private String folioIncial;

    @Column(nullable = false)
    private String folioFinal;
}
