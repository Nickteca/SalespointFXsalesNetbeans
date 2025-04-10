package com.salespointfxsales.www.service.printer;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import org.springframework.stereotype.Service;

@Service
public class Printer {
    public PrintService impresoraTermicaDefault() {
        PrintService defaultPrintService = null;
        try {
            defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();
            if (defaultPrintService == null) {
                System.out.println("No hay una impresora predeterminada configurada.");
                throw new Exception("No hay impresora pero se registro la venta");
            }
            return defaultPrintService;
        } catch (Exception e) {
            return defaultPrintService;
        }

    }
}
