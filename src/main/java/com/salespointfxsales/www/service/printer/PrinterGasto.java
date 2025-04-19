package com.salespointfxsales.www.service.printer;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst.Justification;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.Style.FontSize;
import com.github.anastaciocintra.output.PrinterOutputStream;
import com.salespointfxsales.www.model.SucursalGasto;
import java.io.IOException;
import javax.print.PrintService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrinterGasto {

    private final Printer it;

    public void imprimirGasto(SucursalGasto sg) throws IOException  {
        try {
            PrintService printService = PrinterOutputStream.getDefaultPrintService();
            PrinterOutputStream printerOutputStream = new PrinterOutputStream(printService);
            EscPos escpos = new EscPos(printerOutputStream);

            // Estilos
            Style titleStyle = new Style().setBold(true).setFontSize(FontSize._2, FontSize._2).setJustification(Justification.Center);
            Style headerStyle = new Style().setBold(true).setFontSize(FontSize._1, FontSize._1);
            Style normalStyle = new Style().setFontSize(FontSize._1, FontSize._1);

            // Encabezado
            escpos.writeLF(titleStyle, "REGISTRO DE GASTO");
            escpos.writeLF("--------------------------------");

            // Datos del gasto
            escpos.writeLF(headerStyle, "Descripción: " + sg.getGasto().getDescripcionGasto());
            escpos.writeLF(normalStyle, "Monto: " + sg.getMontoGasto());
            escpos.writeLF(normalStyle, "Sucursal: " + sg.getSucursal().getNombreSucursal());
            escpos.writeLF(normalStyle, "Fecha: " + sg.getCreatedAt());
            escpos.writeLF(normalStyle, "Autorizado por: ");
            escpos.writeLF(normalStyle, "Recibido por: ");
            escpos.writeLF("--------------------------------");

            // Espacio para firmas
            escpos.writeLF("\n\n\n");
            escpos.writeLF("------------------------");
            escpos.writeLF("Firma Autorizado");
            escpos.writeLF("\n\n\n");
            escpos.writeLF("------------------------");
            escpos.writeLF("Firma Recibido");

            // Final
            escpos.feed(5);
            escpos.cut(EscPos.CutMode.FULL);
            escpos.close();
        } catch (IOException e) {
            throw e;
        }
    }
}
