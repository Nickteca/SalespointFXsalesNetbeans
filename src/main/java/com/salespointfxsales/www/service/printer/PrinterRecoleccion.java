package com.salespointfxsales.www.service.printer;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.EscPosConst.Justification;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.Style.FontName;
import com.github.anastaciocintra.escpos.Style.FontSize;
import com.github.anastaciocintra.output.PrinterOutputStream;
import com.salespointfxsales.www.model.Billete;
import com.salespointfxsales.www.model.SucursalRecoleccion;
import com.salespointfxsales.www.model.SucursalRecoleccionDetalle;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.print.PrintService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrinterRecoleccion {

    private final Printer2 p;

    public void imprimirRecoleccion(SucursalRecoleccion sr) throws Exception {
        PrinterOutputStream printerOutputStream = null;
        EscPos escpos = null;
        try {
            Optional<PrintService> ops = p.obtenerImpresoraPorNombre();
            if (ops.isPresent()) {

                PrintService impresora = ops.get();
                printerOutputStream = new PrinterOutputStream(impresora);
                escpos = new EscPos(printerOutputStream);

                // 🔹 ESTILO: Título (Nombre de la empresa)
                Style titleStyle = new Style().setBold(true).setFontSize(Style.FontSize._2, Style.FontSize._2).setJustification(EscPosConst.Justification.Center);
                // @eSTILO PARA SUBTITULOS
                Style subtitleStyle = new Style().setBold(true).setFontSize(FontSize._1, FontSize._1).setJustification(Justification.Center);
                Style headercontete = new Style().setFontName(FontName.Font_C).setJustification(Justification.Left_Default);
                // @eSTILO PARA SUBTITULOS
                Style headerStyle = new Style().setBold(true).setFontSize(FontSize._1, FontSize._1);

                // Style fontA = new Style().setFontName(Style.FontName.Font_A_Default) //
                // Usa Font A (normal) .setFontSize(Style.FontSize._1, Style.FontSize._1);
                Style fontB = new Style().setFontName(Style.FontName.Font_B).setBold(true).setFontSize(FontSize._1, FontSize._1);
                Style fontB2 = new Style().setFontName(Style.FontName.Font_B).setBold(true);
                Style fontA = new Style().setFontName(Style.FontName.Font_A_Default).setBold(true);
                // Usa Font B (más pequeño)

                /*
			 * // 🔹 ESTILO: Subtítulo (Sucursal y dirección) Style subtitleStyle = new
			 * Style().setFontSize(Style.FontSize._1,
			 * Style.FontSize._1).setJustification(EscPosConst.Justification.Left_Default);
			 * 
			 * // 🔹 ESTILO: Encabezado de productos Style headerStyle = new
			 * Style().setBold(true).setFontSize(Style.FontSize._1,
			 * Style.FontSize._1).setJustification(EscPosConst.Justification.Left_Default);
			 * 
			 * // 🔹 ESTILO: Texto normal para los productos Style productStyle = new
			 * Style().setFontSize(Style.FontSize._1,
			 * Style.FontSize._1).setJustification(EscPosConst.Justification.Left_Default);
			 * 
			 * // 🔹 ESTILO: Total (Negrita y tamaño grande) Style totalStyle = new
			 * Style().setBold(true).setFontSize(Style.FontSize._2,
			 * Style.FontSize._2).setJustification(EscPosConst.Justification.Right);
			 * 
			 * // Style fontA = new Style().setFontName(Style.FontName.Font_A_Default) //
			 * Usa Font A (normal) .setFontSize(Style.FontSize._1, Style.FontSize._1);
			 * 
			 * Style fontB = new Style().setFontName(Style.FontName.Font_B).setBold(true);
			 * // Usa Font B (más pequeño)
                 */
                // 🏪 IMPRIMIR ENCABEZADO
                escpos.writeLF(titleStyle, "RECOLECCION: " + sr.getSucursal().getNombreSucursal());
                escpos.writeLF(fontA.setJustification(Justification.Center), sr.getSucursal().getEmpresa().getNombreEmpresa());
                // escpos.writeLF(headercontete, sr.getSucursal().getCalleSucursal() + " " +
                // sr.getSucursal().getCiudadSucursal() + " " +
                // sr.getSucursal().getEstadoSucursal());

                escpos.writeLF(headerStyle, String.format("%-24s %5s %8s", "Billete", "Cantidad", "subtotal"));
                escpos.writeLF(fontB, "----------------------------------------------------------------");
                List<SucursalRecoleccionDetalle> lsrd = sr.getListSucursalRecoleccionDetalle();
                //lsrd.sort(Comparator.comparing(Billete::getBillete).reversed());
                for (SucursalRecoleccionDetalle rb : lsrd) {
                    // if (rb.getCantidad() > 0) {
                    String line = String.format("%-20s %10s %10s", rb.getBillete(), rb.getCantidad(), "$" + rb.getSubTotal());
                    escpos.writeLF(line);
                    // }
                }
                escpos.writeLF(fontB, "----------------------------------------------------------------");
                escpos.writeLF(headerStyle.setBold(true), String.format("%-22s %5s %8s %8s", "", "", "Total", "$" + String.format("%,.0f", sr.getTotalRecoleccion())));

                // Cortar papel y cerrar conexión
                escpos.feed(5);
                escpos.cut(EscPos.CutMode.FULL);

                escpos.close();
            } else {
                log.error("❌ No existe la impresora");
                throw new IOException("No existe la impresora o cambio");
            }
        } catch (Exception e) {
            throw e;
//            infoAlert.showAndWait();
        } finally {

        }
    }
}
