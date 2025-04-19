package com.salespointfxsales.www;

import com.salespointfxsales.www.controller.AbrirCajaController;
import com.salespointfxsales.www.model.MovimientoCaja;
import com.salespointfxsales.www.model.Sucursal;
import com.salespointfxsales.www.model.enums.TipoMovimiento;
import com.salespointfxsales.www.service.MovimientoCajaService;
import com.salespointfxsales.www.service.SucursalService;
import java.io.IOException;
import java.net.ServerSocket;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class MainApp extends Application {

    private ConfigurableApplicationContext context;
    private MovimientoCajaService mcs;
    private SucursalService ss;

    private static ServerSocket serverSocket; // Para mantener el puerto abierto

    @Override
    public void init() {
        context = new SpringApplicationBuilder(SalespointFXsalesNetbeansApplication.class).run();
        mcs = context.getBean(MovimientoCajaService.class);
        ss = context.getBean(SucursalService.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        if (!isApplicationAlreadyRunning(9998)) {
            Sucursal s = ss.findByEstatusSucursalTrue();
            if (s != null) {
                MovimientoCaja mc = mcs.findlastmovimientoCajasucursalActiva();
                if (mc != null) {
                    if (mc.getTipoMovimientoCaja().equals(TipoMovimiento.APERTURA)) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource( "/fxml/starter.fxml"));
                        loader.setControllerFactory(context::getBean); // Inyectar Spring Beans
                        Parent root = loader.load();
                        Stage newStage = new Stage();
                        newStage.setTitle("Nickteca Solutions");
                        newStage.setScene(new Scene(root));
                        newStage.setMinHeight(768);
                        newStage.setMinWidth(1024);
                        newStage.show();
                    }
                    if (mc.getIdMovimientoCaja().equals(TipoMovimiento.CIERRE)) {
                        /*ABRIR CAJA CPON EL SALDO ANTERIOR DEL CORTE*/
                    }
                } else {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/abrircaja.fxml"));
                    loader.setControllerFactory(context::getBean); // Inyectar Spring Beans
                    AbrirCajaController acc = context.getBean(AbrirCajaController.class);

                    Parent root = loader.load();
                    primaryStage.setTitle("NictecaSolutions");
                    primaryStage.setScene(new Scene(root));
                    primaryStage.setResizable(false);
                    primaryStage.show();
                    acc.parametros(s.getNombreSucursal());
                }
            }

        } else {
            Alert error = new Alert(AlertType.ERROR);
            error.setTitle("Ya se ejecuto la palicacion");
            error.setHeaderText("Cierra esta intancia");
            error.setContentText("cerrar");
            error.showAndWait();
            System.exit(0);
        }
        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        loader.setControllerFactory(context::getBean); // Spring inyecta los controladores
        Parent root = loader.load();

        primaryStage.setTitle("Mi App con JavaFX + Spring Boot");
        primaryStage.setScene(new Scene(root));*/
        //primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        context.close(); // Cierra contexto Spring al cerrar la app
    }

    private static boolean isApplicationAlreadyRunning(int port) {
        try {
            serverSocket = new ServerSocket(port); // Intenta abrir el puerto
            serverSocket.setReuseAddress(true); // Permite reutilización rápida del puerto
            return false; // No está corriendo ya que se pudo abrir el puerto
        } catch (IOException e) {
            return true; // Ya está corriendo porque el puerto está ocupado
        }
    }
}
