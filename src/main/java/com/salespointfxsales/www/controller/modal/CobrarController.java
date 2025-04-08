package com.salespointfxsales.www.controller.modal;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CobrarController implements Initializable {

    private final StringBuilder input = new StringBuilder();

    @FXML
    private Label labelCambio;

    @FXML
    private Label labelTotal;

    @FXML
    private TextField tFieldCantidad;

    private boolean cobroExitoso = false;

    // Método que te permite obtener el estado del cobro
    public boolean isCobroExitoso() {
        return cobroExitoso;
    }

    @FXML
    void billete(ActionEvent event) {

    }

    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage) labelCambio.getScene().getWindow(); // Asumiendo que tienes un botón
        stage.close();
    }

    @FXML
    void clear(ActionEvent event) {
        input.setLength(0);
        tFieldCantidad.setText("");
    }

    @FXML
    void cobrar(ActionEvent event) {
        // Aquí tu lógica de cobro
        cobroExitoso = true; // Establecer si el cobro fue exitoso
        cancelar(event);
    }

    @FXML
    void eliminar(ActionEvent event) {
        if (input.length() > 0) {
            input.deleteCharAt(input.length() - 1);
            tFieldCantidad.setText(input.toString());
        }
    }

    @FXML
    void numero(ActionEvent event) {
        Button btn = (Button) event.getSource();
        String digit = btn.getText();

        // Evitar múltiples ceros al inicio
        if (input.length() == 0 && digit.equals("0")) {
            return;
        }

        input.append(digit);
        tFieldCantidad.setText(input.toString());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tFieldCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || newValue.startsWith("0")) {
                tFieldCantidad.setText(oldValue); // Si no es un número válido o empieza con 0, vuelve al valor anterior
            }
        });
    }

}
