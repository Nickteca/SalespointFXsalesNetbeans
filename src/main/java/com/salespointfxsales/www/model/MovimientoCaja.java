package com.salespointfxsales.www.model;

import com.salespointfxsales.www.model.enums.TipoMovimiento;
import jakarta.persistence.CascadeType;
import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = @Index(name = "indice_fecha", columnList = "createdAt"))
public class MovimientoCaja implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer idMovimientoCaja;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipoMovimientoCaja;

    @Column(nullable = false)
    private float saldoAnterior;

    @Column(nullable = true)
    private float saldoFinal;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @JoinColumn(name = "sucursal", referencedColumnName = "idSucursal")
    @ManyToOne(optional = false)
    private Sucursal sucursal;

    @OneToOne(mappedBy = "movimientoCaja",cascade = CascadeType.ALL)
    private Corte corte;

    public MovimientoCaja(Integer idMovimientoCaja, TipoMovimiento tipoMovimientoCaja, float saldoAnterior, float saldoFinal, LocalDateTime createdAt) {
        this.idMovimientoCaja = idMovimientoCaja;
        this.tipoMovimientoCaja = tipoMovimientoCaja;
        this.saldoAnterior = saldoAnterior;
        this.saldoFinal = saldoFinal;
        this.createdAt = createdAt;
    }

    public MovimientoCaja(Integer idMovimientoCaja, TipoMovimiento tipoMovimientoCaja, float saldoAnterior, float saldoFinal, LocalDateTime createdAt, Sucursal sucursal) {
        this.idMovimientoCaja = idMovimientoCaja;
        this.tipoMovimientoCaja = tipoMovimientoCaja;
        this.saldoAnterior = saldoAnterior;
        this.saldoFinal = saldoFinal;
        this.createdAt = createdAt;
        this.sucursal = sucursal;
    }

}
