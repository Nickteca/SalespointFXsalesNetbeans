package com.salespointfxsales.www.controller;

import com.salespointfxsales.www.config.SpringFXMLLoader;
import com.salespointfxsales.www.service.SucursalService;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StarterController implements Initializable {
    private final SucursalService ss;

    private final SpringFXMLLoader springFXMLLoader;

    @FXML
    private AnchorPane aPanePrincipal;

    @FXML
    private AnchorPane anchorFondo;

    @FXML
    private AnchorPane anchorfondo2;

    @FXML
    private BorderPane bPanePrincipal;

    @FXML
    private Button buttonAbrirCajon;

    @FXML
    private Button buttonGastos;

    @FXML
    private Button buttonInvetario;

    @FXML
    private Button buttonMovimientos;

    @FXML
    private Button buttonRecoleccion;

    @FXML
    private Button buttonVenta;

    @FXML
    private Button buttonVentas;

    
    @FXML
    private Label labelSucursal;

    @FXML
    void abrirCajon(ActionEvent event) {

    }

    @FXML
    void cerrarCaja(MouseEvent event) {

    }

    @FXML
    void gastos(ActionEvent event) {

    }

    @FXML
    void inventario(ActionEvent event) {
        cargarVista("/fxml/inventario.fxml");
    }

    @FXML
    void movimientos(ActionEvent event) {

    }

    @FXML
    void recoleccion(ActionEvent event) {

    }

    @FXML
    void venta(ActionEvent event) {
        cargarVista("/fxml/venta.fxml");
    }

    @FXML
    void ventas(ActionEvent event) {
         cargarVista("/fxml/ventas.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelSucursal.setText(ss.findByEstatusSucursalTrue().getNombreSucursal());
    }

    private void cargarVista(String fxmlPath) {
        try {
            FXMLLoader fxml = springFXMLLoader.load(fxmlPath);
            AnchorPane view = fxml.load();
            bPanePrincipal.setCenter(view);
            
        } catch (IOException e) {
            Alert error = new Alert(AlertType.ERROR);
            error.setTitle("StarterController Error!!!");
            error.setHeaderText("Error al cargar la vista");
            error.setContentText(e.getMessage() + "\n" + e.getCause());
            error.show();
            e.printStackTrace();
        }
    }
}
