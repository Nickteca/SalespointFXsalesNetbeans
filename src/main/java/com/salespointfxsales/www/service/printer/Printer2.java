package com.salespointfxsales.www.service.printer;

import com.salespointfxsales.www.service.ConfiguracionService;
import java.util.Optional;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Printer2 {
    
    private final ConfiguracionService cs;
    
    /*@Value("${printer.name}")
    public String impresora = cs.getValor("impresora_ticket");*/
    
    public Optional<PrintService> obtenerImpresoraPorNombre() {
    try {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        
        for (PrintService service : services) {
            if (service.getName().equalsIgnoreCase(cs.getValor("impresora_ticket"))) {
                return Optional.of(service);
            }
        }

        System.out.println("⚠ No se encontró la impresora con nombre: " + cs.getValor("impresora_ticket"));
        return Optional.empty();
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("❌ Error al buscar impresora: " + e.getMessage());
        return Optional.empty();
    }
}
    //XP-80C
    
   
}
