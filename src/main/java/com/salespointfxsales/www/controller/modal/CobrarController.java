package com.salespointfxsales.www.controller.modal;

import com.salespointfxsales.www.model.dto.ResultadoCobro;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CobrarController implements Initializable {

    private static final DecimalFormat formatoMoneda = new DecimalFormat("#");
    private ResultadoCobro resultadoCobro;

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
        Button btn = (Button) event.getSource();
        String billete = btn.getText();
        input.setLength(0);
        input.append(billete);
        tFieldCantidad.setText(input.toString());
        calcularCambio();
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
        labelCambio.setText("$-" + labelTotal.getText());
    }

    @FXML
    void cobrar(ActionEvent event) {
        try {
            float pagoCon = Float.parseFloat(tFieldCantidad.getText());
            float total = Float.parseFloat(labelTotal.getText());
            float cambio = Float.parseFloat(labelCambio.getText().replace("$", ""));
            if (cambio < 0) {
                throw new Exception("El cambio no puede ser negativo!!");
            }
            // Guardar el resultado exitoso
            resultadoCobro = new ResultadoCobro(total, pagoCon, cambio, true);
            cobroExitoso = true;
            cancelar(event);

        } catch (Exception e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error al Cobrar!!");
            alerta.setHeaderText("Hay un error aal cobrar");
            alerta.setContentText(e.getMessage() + "\n" + e.getCause());
            alerta.show();
        }

    }

    @FXML
    void eliminar(ActionEvent event) {
        if (input.length() > 0) {
            input.deleteCharAt(input.length() - 1);
            tFieldCantidad.setText(input.toString());
            calcularCambio();
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
        calcularCambio();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tFieldCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || newValue.startsWith("0")) {
                tFieldCantidad.setText(oldValue); // Si no es un número válido o empieza con 0, vuelve al valor anterior
            }
        });
    }

    public void totalVenta(Float venta) {
        labelTotal.setText(formatoMoneda.format(venta));
        tFieldCantidad.setText(formatoMoneda.format(venta));
        labelCambio.setText("$0");
    }

    private void calcularCambio() {
        try {
            float efectivo = Float.parseFloat(tFieldCantidad.getText());
            float total = Float.parseFloat(labelTotal.getText());
            float cambio = efectivo - total;
            labelCambio.setText("$" + formatoMoneda.format(cambio));
        } catch (Exception e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error Calculo!!");
            alerta.setHeaderText("Hay un error aL PARECER NUMERICO");
            alerta.setContentText(e.getMessage() + "\n" + e.getCause());
            alerta.show();
        }
    }

    public ResultadoCobro getResultadoCobro() {
        return resultadoCobro;
    }
}
