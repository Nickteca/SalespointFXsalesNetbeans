package com.salespointfxsales.www.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
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
    private float inicial;

    @Column(nullable = false)
    private float ventas;

    @Column(nullable = false)
    private float recoleccion;

    @Column(nullable = false)
    private float gasto;

    @Column(nullable = false)
    private float saldoFinal;

    @Column(nullable = false)
    private LocalDateTime apertuta;

    @Column(nullable = false)
    private LocalDateTime cierre;

    @Column(nullable = false)
    private short numFolios;

    @Column(nullable = false)
    private String folioIncial;

    @Column(nullable = false)
    private String folioFinal;
    
    @OneToMany(mappedBy = "corte", cascade = CascadeType.ALL)
    private List<CorteDetalle> listCorteDetalle;

    public Corte(Integer idCorte, Sucursal sucursal, MovimientoCaja movimientoCaja, float inicial, float ventas, float recoleccion, float gasto, float saldoFinal, LocalDateTime apertuta, LocalDateTime cierre, short numFolios, String folioIncial, String folioFinal) {
        this.idCorte = idCorte;
        this.sucursal = sucursal;
        this.movimientoCaja = movimientoCaja;
        this.inicial = inicial;
        this.ventas = ventas;
        this.recoleccion = recoleccion;
        this.gasto = gasto;
        this.saldoFinal = saldoFinal;
        this.apertuta = apertuta;
        this.cierre = cierre;
        this.numFolios = numFolios;
        this.folioIncial = folioIncial;
        this.folioFinal = folioFinal;
    }
    
    
    
}
