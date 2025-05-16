package com.salespointfxsales.www.service.printer;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.output.PrinterOutputStream;
import com.salespointfxsales.www.model.SucursalPedido;
import com.salespointfxsales.www.model.SucursalPedidoDetalle;
import java.util.List;
import java.util.Optional;
import javax.print.PrintService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrinterPedidoService {

    private final Printer2 p;

    public boolean imprimirPedido(SucursalPedido sp) throws Exception {
        boolean imprimio = false;
        PrinterOutputStream printerOutputStream = null;
        EscPos escpos = null;
        try {
            Optional<PrintService> ops = p.obtenerImpresoraPorNombre();
            if (ops.isPresent()) {
                /*SE OBTINE LA IMPRESORA Y SE ASIGNA PAR APROCEDER A IMPRIMIR*/
                PrintService impresora = ops.get();
                printerOutputStream = new PrinterOutputStream(impresora);
                escpos = new EscPos(printerOutputStream);

                /*ESTILOS PAR IMPRIMIR*/
                Style titleStyle = new Style().setBold(true).setFontName(Style.FontName.Font_A_Default).setJustification(EscPosConst.Justification.Center);
                Style subTitleStyle = new Style().setBold(true).setFontName(Style.FontName.Font_A_Default).setJustification(EscPosConst.Justification.Center);
                Style headerStyle = new Style().setFontName(Style.FontName.Font_A_Default).setJustification(EscPosConst.Justification.Left_Default);
                Style contenidoStyle = new Style().setFontName(Style.FontName.Font_B).setJustification(EscPosConst.Justification.Center);

                // 🏪 IMPRIMIR ENCABEZADO
                escpos.writeLF(titleStyle, "PEDIDO: " + sp.getSucursal().getNombreSucursal());
                escpos.writeLF(subTitleStyle, sp.getSucursal().getEmpresa().getNombreEmpresa());

                /*ENCABEZADO DE LOS DETALLES*/
                escpos.writeLF(headerStyle, String.format("%-29s %-18s", "PRODUCTO", "CANTIDAD"));
                escpos.writeLF(headerStyle, "------------------------------------------------");
                List<SucursalPedidoDetalle> lspedidod = sp.getListSucursalpedidoDetalle();
                for (SucursalPedidoDetalle sucursalPedidoDetalle : lspedidod) {
                    String line = String.format("%-39s %-24s", sucursalPedidoDetalle.getSucursalProducto(), sucursalPedidoDetalle.getCantidad());
                    escpos.writeLF(contenidoStyle, line);
                }
                escpos.feed(5);
                escpos.cut(EscPos.CutMode.FULL);

                escpos.close();
                return true;
            } else {
                System.err.println("❌ No existe la impresora");
                return false;
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
