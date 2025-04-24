package com.salespointfxsales.www.service.printer;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.output.PrinterOutputStream;
import com.salespointfxsales.www.model.Venta;
import com.salespointfxsales.www.model.VentaDetalle;
import com.salespointfxsales.www.model.dto.ResultadoCobro;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrinterTicketService {

    private final Printer ps;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public boolean imprimirTicket(Venta v, ResultadoCobro rc) {
        PrinterOutputStream printerOutputStream = null;
        EscPos escpos = null;
        try {
            Optional<PrintService> optionalPrintService = ps.impresoraTermicaDefault();
            if (optionalPrintService.isEmpty() || optionalPrintService.get() == null) {
                System.out.println("⚠ Venta registrada pero no se pudo imprimir: no hay impresora.");
                return false; // No lanzamos excepción, solo salimos
            }
            // Obtenemos la impresora seleccionada
            PrintService printService = optionalPrintService.get();
            // Si la impresora es válida, creamos el flujo de impresión
            printerOutputStream = new PrinterOutputStream(printService);

            // Verificamos si el servicio de impresión está disponible
            if (printerOutputStream.equals(null)) {
                System.out.println("⚠ No se encontró una impresora válida.");
                //registrarVentaSinImpresion(v); // Registramos en la base de datos
                return false; // Regresamos falso ya que no podemos imprimir
            }
            escpos = new EscPos(printerOutputStream);

            Style titulo = new Style().setFontName(Style.FontName.Font_A_Default).setUnderline(Style.Underline.OneDotThick).setBold(true).setJustification(EscPosConst.Justification.Center);
            Style subtitulo = new Style().setFontName(Style.FontName.Font_A_Default);
            Style header = new Style().setFontName(Style.FontName.Font_A_Default);

            Style header2 = new Style().setFontName(Style.FontName.Font_B);

            // 🏪 IMPRIMIR ENCABEZADO
            escpos.writeLF(titulo, v.getSucursal().getEmpresa().getNombreEmpresa());
            escpos.writeLF(subtitulo, v.getSucursal().getNombreSucursal() + ", DIRECCION: " + v.getSucursal().getEmpresa().getDireccionEmpresa());
            //escpos.writeLF(header, v.getSucursal().getEmpresa().getDireccionEmpresa());
            escpos.writeLF(header, "Folio: " + v.getFolio() + " Fecha: " + formatter.format(v.getCreatedAt()));
            escpos.write(header2, "________________________________________________________________");
            escpos.writeLF(header, String.format("%-27s %5s %4s %8s", "Producto", "Cant", "Pre", "Subt"));
            float peso = 0;
            for (VentaDetalle vd : v.getListVentaDetalle()) {//36
                String line1 = String.format("%-36s %5d %8s %10s", vd.getSucursalProducto().getProducto(), vd.getCantidad(), "$" + String.format("%,.0f", vd.getPrecio()),
                        "$" + String.format("%,.0f", vd.getSubTotal())); // Formateo de subtotal sin decimales
                escpos.writeLF(header2, line1);
                peso+=vd.getPeso();
            }
            escpos.write(header2, "________________________________________________________________");
            
           if(peso>0) escpos.writeLF(header2, "PESO DE LA COSTILLA: "+peso+"Kg");
            escpos.writeLF(subtitulo, String.format("%30s  %16s", "Total:", "$" + String.format("%,.0f", v.getTotalVenta())));
            escpos.writeLF(header.setUnderline(Style.Underline.OneDotThick), String.format("%30s  %16s", "Efectivo:", "$" + String.format("%,.0f", rc.getPagoCon())));
            escpos.writeLF(subtitulo, String.format("%30s  %16s", "Cambio:", "$" + String.format("%,.0f", rc.getCambio())));
            escpos.writeLF(subtitulo, "***********GRACIAS POR SU PREFERENCIA***********");
            escpos.feed(5);
            escpos.cut(EscPos.CutMode.FULL);
            // Suponiendo que escpos es una instancia de una clase que tiene el método write
            byte[] abrirCaja = new byte[]{0x1B, 0x70, 0x00, 0x19, (byte) 0xFA};

            // Escribe los bytes al outputStream (suponiendo que escpos es un flujo de
            // salida)
            escpos.write(abrirCaja, 0, abrirCaja.length); // 0 es el offset y abrirCaja.length es la longitud
            return true;
        } catch (IOException | RuntimeException ex) {
            System.out.println("❌ Error al imprimir: " + ex.getMessage());
            return false; // Devolvemos false en caso de error
        } finally {
            try {
                if (escpos != null) {
                    escpos.close();
                }
                if (printerOutputStream != null) {
                    printerOutputStream.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(PrinterTicketService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
