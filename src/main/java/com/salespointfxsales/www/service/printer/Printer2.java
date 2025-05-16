package com.salespointfxsales.www.service.printer;

import java.util.Optional;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Printer2 {
    @Value("${printer.name}")
    public String impresora;
    
    public Optional<PrintService> obtenerImpresoraPorNombre() {
    try {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        
        for (PrintService service : services) {
            if (service.getName().equalsIgnoreCase(impresora)) {
                return Optional.of(service);
            }
        }

        System.out.println("⚠ No se encontró la impresora con nombre: " + impresora);
        return Optional.empty();
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("❌ Error al buscar impresora: " + e.getMessage());
        return Optional.empty();
    }
}
    //XP-80C
    
   
}
