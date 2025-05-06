package com.salespointfxsales.www.service.printer;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.output.PrinterOutputStream;
import com.salespointfxsales.www.model.Corte;
import com.salespointfxsales.www.model.Empresa;
import com.salespointfxsales.www.model.MovimientoCaja;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrinterCorteService {

    private final Printer p;

    public boolean imprimirCorte(Corte c, MovimientoCaja mca, MovimientoCaja mcc) {
        try {
            PrintService printService = PrinterOutputStream.getDefaultPrintService();
            PrinterOutputStream printerOutputStream = new PrinterOutputStream(printService);
            EscPos escpos = new EscPos(printerOutputStream);

            // Estilos
            Style titleStyle = new Style().setBold(true).setFontSize(Style.FontSize._2, Style.FontSize._2);
            Style headerStyle = new Style().setBold(true).setFontSize(Style.FontSize._1, Style.FontSize._1);
            Style normalStyle = new Style().setFontSize(Style.FontSize._1, Style.FontSize._1);

            Empresa e = c.getSucursal().getEmpresa();

            escpos.writeLF(titleStyle, c.getSucursal().getEmpresa().getNombreEmpresa());
            escpos.writeLF(titleStyle, c.getSucursal().getNombreSucursal() + ", DIRECCION: " + "" + c.getSucursal().getEstadoSucursal() + "" + c.getSucursal().getCiudadSucursal() + " " + c.getSucursal().getCalleSucursal());
            escpos.writeLF(titleStyle, "RFC: " + c.getSucursal().getEmpresa().getRfcEmpresa());
            escpos.writeLF(new Style().setFontName(Style.FontName.Font_B), "________________________________________________________________");
            escpos.writeLF(new Style().setFontName(Style.FontName.Font_B), "-----------------------------FECHA------------------------------");
            escpos.writeLF(titleStyle, "APERTURA: " + mca.getCreatedAt());
            escpos.writeLF(titleStyle, "CIERRE  : " + mcc.getCreatedAt());
            escpos.writeLF(new Style().setFontName(Style.FontName.Font_B), "-----------------------------FONDO------------------------------");
            escpos.writeLF(titleStyle, "SALDO ANTERIOR:" + c.getInicial() + ":::" + mca.getSaldoAnterior());
            escpos.writeLF(titleStyle, "VENTAS        :" + c.getVentas());
            escpos.writeLF(titleStyle, "GASTOS        :" + c.getGasto());
            escpos.writeLF(titleStyle, "RECOLECCION   :" + c.getRecoleccion());
            escpos.writeLF(titleStyle, "SALDO FINAL   :" + c.getSaldoFinal() + ":::" + mcc.getSaldoFinal());
            escpos.writeLF(new Style().setFontName(Style.FontName.Font_B), "-------------------------DETALLE FOLIOS-------------------------");
            escpos.writeLF(titleStyle, "FOLIO INICIAL :" + c.getFolioIncial());
            escpos.writeLF(titleStyle, "FOLIO FINAL   :" + c.getFolioFinal());
            escpos.writeLF(titleStyle, "NUMERO FOLIOS :" + c.getNumFolios());
            escpos.writeLF(new Style().setFontName(Style.FontName.Font_B), "________________________________________________________________");
            escpos.writeLF(new Style().setFontName(Style.FontName.Font_B), "------------------------DETALLES PRODUCTOS----------------------");

            c.getListCorteDetalle().forEach(cd -> {
                
                try {
                    escpos.writeLF(normalStyle, cd.getSucursalProducto().toString());
                    escpos.writeLF(normalStyle, cd.getEntrada() + "");
                    escpos.writeLF(normalStyle, cd.getSalida() + "");
                    escpos.writeLF(normalStyle, cd.getTraspasoEntrada() + "");
                    escpos.writeLF(normalStyle, cd.getTraspasoSalida() + "");
                    escpos.writeLF(normalStyle, cd.getVenta() + "");
                    escpos.writeLF(normalStyle, cd.getVenta() + "");
                    escpos.writeLF(normalStyle, cd.getCanceladas() + "");
                    escpos.writeLF(normalStyle, cd.getPeso() + "");
                    escpos.writeLF(normalStyle, cd.getExistencia() + "");
                } catch (IOException ex) {
                    Logger.getLogger(PrinterCorteService.class.getName()).log(Level.SEVERE, null, ex);
                }

                
            });
            escpos.feed(5);
            escpos.cut(EscPos.CutMode.FULL);
            // Suponiendo que escpos es una instancia de una clase que tiene el método write
            byte[] abrirCaja = new byte[]{0x1B, 0x70, 0x00, 0x19, (byte) 0xFA};
            escpos.write(abrirCaja, 0, abrirCaja.length); // 0 es el offset y abrirCaja.length es la longitud
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }
}
