package com.salespointfxsales.www.service.reports;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportGenerator {

    private final DataSource dataSource;
    private final ResourceLoader resourceLoader;

    public byte[] generateReport(int idPedido) throws Exception {
        // Cargar el archivo .jasper
        InputStream reportStream = new ClassPathResource("/reports/CorteReporte.jasper").getInputStream();
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);

        // Configurar parámetros
        try (Connection conn = dataSource.getConnection()) {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("corteId", idPedido);

            log.info("Parámetros pasados:");
            parameters.forEach((k, v) ->log.info(k + " = " + v));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
            System.out.println("Páginas generadas: " + jasperPrint.getPages().size());
            if (jasperPrint.getPages().isEmpty()) {
                System.out.println("⚠️ El reporte no tiene páginas. Revisa los parámetros o la conexión.");
            }
            return JasperExportManager.exportReportToPdf(jasperPrint);
        }
        // JasperViewer.viewReport(jasperPrint, false);
        //System.out.println("Conexión a la base de datos: " + dataSource.getConnection());

        // Exportar a PDF
        // JasperPrintManager.printReport(jasperPrint, false);
        //return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
