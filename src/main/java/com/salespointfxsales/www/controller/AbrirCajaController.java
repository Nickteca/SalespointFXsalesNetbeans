package com.salespointfxsales.www.controller;

import com.salespointfxsales.www.config.SpringFXMLLoader;
import com.salespointfxsales.www.model.MovimientoCaja;
import com.salespointfxsales.www.model.enums.TipoMovimiento;
import com.salespointfxsales.www.service.MovimientoCajaService;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AbrirCajaController implements Initializable {

    private final SpringFXMLLoader springFXMLLoader;
    private final MovimientoCajaService mcs;

    @FXML
    private Button buttonAbrirCaja;

    @FXML
    private Button buttonCancelar;

    @FXML
    private Label labelsucursal;

    @FXML
    private TextField tFieldSaldoAnteriro;

    @FXML
    private TextField tFieldSaldoCaja;

    @FXML
    void abrirCaja(ActionEvent event) {
        try {
            String texto = tFieldSaldoAnteriro.getText();
            if (texto.isEmpty()) {
                throw new IllegalArgumentException("Debes ingresar el saldo de apertura.");
            }

            float saldo = Float.parseFloat(texto);

            MovimientoCaja movimiento = mcs.saveMovimiento(TipoMovimiento.APERTURA,saldo,0);

            if (movimiento != null) {
                FXMLLoader loader = springFXMLLoader.load("/fxml/starter.fxml");
                Parent root = loader.load();
                Stage newStage = new Stage();
                newStage.setTitle("Nickteca Solutions");
                newStage.setScene(new Scene(root));
                newStage.setMinHeight(768);
                newStage.setMinWidth(1024);
                newStage.show();
            }

        } catch (NumberFormatException e) {
            mostrarError("El saldo ingresado no es un número válido.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarError(e.getMessage());
        } catch (IOException e) {
            mostrarError("No se pudo cargar la vista starter: " + e.getMessage());
        } catch (Exception e) {
            mostrarError("Ocurrió un error inesperado: " + e.getMessage());
        }
    }

    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage) buttonCancelar.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void parametros(String sucursal) {
        labelsucursal.setText(sucursal);
    }

    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText("Se ha producido un error");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
