package com.salespointfxsales.www.model.dto;

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
