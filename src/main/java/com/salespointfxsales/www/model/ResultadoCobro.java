package com.salespointfxsales.www.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoCobro {
    private float total;
    private float pagoCon;
    private float cambio;
    private boolean exito;
}
