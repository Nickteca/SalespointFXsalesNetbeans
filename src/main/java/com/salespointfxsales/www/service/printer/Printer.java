package com.salespointfxsales.www.service.printer;

import java.util.Optional;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import org.springframework.stereotype.Service;

@Service
public class Printer {
    public Optional<PrintService> impresoraTermicaDefault() {
        try {
            PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();
            System.err.println( defaultPrintService.getName());
            if (defaultPrintService == null || defaultPrintService.equals(null)) {
                System.out.println("⚠ No hay impresora predeterminada configurada.");
                return Optional.empty();
            }
            return Optional.of(defaultPrintService);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error al buscar impresora: " + e.getMessage());
            return Optional.empty();
        }
    }
}
