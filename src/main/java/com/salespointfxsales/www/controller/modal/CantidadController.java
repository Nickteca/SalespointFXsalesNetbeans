package com.salespointfxsales.www.controller.modal;

import com.salespointfxsales.www.model.VentaDetalle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class CantidadController implements Initializable {

    @FXML
    private TextField txtCantidad;

    private final StringBuilder input = new StringBuilder();
    private VentaDetalle ventaDetalle;

    private Consumer<Integer> onCantidadConfirmada;

    public void setVentaDetalle(VentaDetalle ventaDetalle) {
        this.ventaDetalle = ventaDetalle;
        // Establecer la cantidad actual en el campo de texto
        txtCantidad.setText(String.valueOf(ventaDetalle.getCantidad()));
    }

    @FXML
    private void onNumberClick(ActionEvent event) {
        Button btn = (Button) event.getSource();
        String digit = btn.getText();

        // Evitar múltiples ceros al inicio
        if (input.length() == 0 && digit.equals("0")) {
            return;
        }

        input.append(digit);
        txtCantidad.setText(input.toString());
    }

    @FXML
    private void onClear() {
        input.setLength(0);
        txtCantidad.setText("");
    }

    @FXML
    private void onBackspace() {
        if (input.length() > 0) {
            input.deleteCharAt(input.length() - 1);
            txtCantidad.setText(input.toString());
        }
    }

    @FXML
    private void onAceptar() {
        String texto = txtCantidad.getText();
        if (texto != null && !texto.isEmpty()) {
            try {
                int nuevaCantidad = Integer.parseInt(texto);
                if (nuevaCantidad <= 0) {
                    mostrarAlerta("La cantidad debe ser mayor que cero.");
                } else {
                    // Actualizar la cantidad en el VentaDetalle
                    ventaDetalle.setCantidad((short) nuevaCantidad);
                    ventaDetalle.setSubTotal(nuevaCantidad * ventaDetalle.getPrecio());
                    cerrarVentana();
                }
            } catch (NumberFormatException e) {
                mostrarAlerta("Cantidad no válida.");
            }
        } else {
            mostrarAlerta("Debe ingresar una cantidad.");
        }
    }

    @FXML
    private void onCancel() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtCantidad.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Entrada inválida");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
