package com.salespointfxsales.www.service.reports;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ReportGenerator {

    private final DataSource dataSource;
    private final ResourceLoader resourceLoader;

    public byte[] generateReport(Integer idPedido) throws Exception {
        // Cargar el archivo .jasper
        InputStream reportStream = new ClassPathResource("/reports/CorteReporte.jasper").getInputStream();
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);

        // Configurar parámetros
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("corteId", idPedido); // Pasar el parámetro
        parameters.put("REPORT_CONNECTION", dataSource.getConnection());

        // Llenar el reporte con datos
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());
        // JasperViewer.viewReport(jasperPrint, false);
        System.out.println("Conexión a la base de datos: " + dataSource.getConnection());

        // Exportar a PDF
        // JasperPrintManager.printReport(jasperPrint, false);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
