package com.salespointfxsales.www.controller.modal;

import com.salespointfxsales.www.model.Billete;
import com.salespointfxsales.www.model.SucursalRecoleccion;
import com.salespointfxsales.www.model.SucursalRecoleccionDetalle;
import com.salespointfxsales.www.model.enums.BilleteValor;
import com.salespointfxsales.www.service.BilleteService;
import com.salespointfxsales.www.service.SucursalRecoleccionService;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecoleccionController implements Initializable {
    private final BilleteService bs;
    private final SucursalRecoleccionService srs;

    @FXML
    private Label label1;

    @FXML
    private Label label10;

    @FXML
    private Label label100;

    @FXML
    private Label label1000;

    @FXML
    private Label label2;

    @FXML
    private Label label20;

    @FXML
    private Label label200;

    @FXML
    private Label label5;

    @FXML
    private Label label50;

    @FXML
    private Label label500;

    @FXML
    private TextField tField1;

    @FXML
    private TextField tField10;

    @FXML
    private TextField tField100;

    @FXML
    private TextField tField1000;

    @FXML
    private TextField tField2;

    @FXML
    private TextField tField20;

    @FXML
    private TextField tField200;

    @FXML
    private TextField tField5;

    @FXML
    private TextField tField50;

    @FXML
    private TextField tField500;

    @FXML
    private TextField tFieldTotal;

    private TextField[] textFields;
    private final int[] valores = {1000, 500, 200, 100, 50, 20, 10, 5, 2, 1};
    private final String[] denominacion = { "$1000", "$500", "$200", "$100", "$50", "$20", "$10", "$5", "$2", "$1" };
    private Label[] labels;

    @FXML
    void cantidad(KeyEvent event) {
        actualizarTotal();
    }

    @FXML
    void registrar(ActionEvent event) {
        try {
            int total = Integer.parseInt(tFieldTotal.getText().replace("$", ""));
            if (total <= 0) {
                showErrorDialog("Erro numerico!!!", "El total no debe ser 0", "");
                return;
            }
            SucursalRecoleccion sr = new SucursalRecoleccion();
            sr.setTotalRecoleccion(total);
            List<SucursalRecoleccionDetalle> lsrd = new ArrayList<SucursalRecoleccionDetalle>();
            for (int i = 0; i < textFields.length; i++) {
                if (!textFields[i].getText().isEmpty() && textFields[i].getText() != "0") {
                    short cantidad = Short.parseShort(textFields[i].getText());
                    int subtotal = cantidad * valores[i];
                    total += subtotal;
                    SucursalRecoleccionDetalle srd = new SucursalRecoleccionDetalle(null, cantidad, subtotal ,bs.findByBillete(BilleteValor.valueOf(denominacion[i])), sr);
                    lsrd.add(srd);
                }
            }
            sr.setListSucursalRecoleccionDetalle(lsrd);
            srs.save(sr);
        } catch (NumberFormatException e) {
            showErrorDialog("Error numerico", "Al parcer hay un error numerico", e.getMessage());
        } catch (IllegalArgumentException e) {
            showErrorDialog("error en el Service", "", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Error desconocido", "no sabes el erro que est apasando", e.getMessage());
        } finally {
            cerarModal();
        }
    }

    @FXML
    void seleccionar(MouseEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        numerosTexfiel();
        seleccinarContenidoTextField();
        textFields = new TextField[]{tField1000, tField500, tField200, tField100, tField50, tField20, tField10, tField5, tField2, tField1};
        labels = new Label[]{label1000, label500, label200, label100, label50, label20, label10, label5, label2, label1};
    }

    private void numerosTexfiel() {
        UnaryOperator<TextFormatter.Change> numericFilter = change -> {
            // Asegura que la entrada solo contenga números (0-9) y se permita el cambio
            String text = change.getText();
            if (text.matches("[0-9]*")) { // Solo acepta dígitos del 0 al 9
                return change; // Si el texto es numérico, permite el cambio
            }
            return null; // Si no es numérico, no permite el cambio
        };

        // Crear un TextFormatter con el filtro
        // Aplicar el filtro a los TextFields ya definidos
        tField1.setTextFormatter(new TextFormatter<>(numericFilter));
        tField10.setTextFormatter(new TextFormatter<>(numericFilter));
        tField100.setTextFormatter(new TextFormatter<>(numericFilter));
        tField1000.setTextFormatter(new TextFormatter<>(numericFilter));
        tField2.setTextFormatter(new TextFormatter<>(numericFilter));
        tField200.setTextFormatter(new TextFormatter<>(numericFilter));
        tField5.setTextFormatter(new TextFormatter<>(numericFilter));
        tField50.setTextFormatter(new TextFormatter<>(numericFilter));
        tField500.setTextFormatter(new TextFormatter<>(numericFilter));
    }

    private void seleccinarContenidoTextField() {
        tField1.setOnMouseClicked(event -> tField1.selectAll());
        tField10.setOnMouseClicked(event -> tField10.selectAll());
        tField100.setOnMouseClicked(event -> tField100.selectAll());
        tField1000.setOnMouseClicked(event -> tField1000.selectAll());
        tField2.setOnMouseClicked(event -> tField2.selectAll());
        tField20.setOnMouseClicked(event -> tField20.selectAll());
        tField200.setOnMouseClicked(event -> tField200.selectAll());
        tField5.setOnMouseClicked(event -> tField5.selectAll());
        tField50.setOnMouseClicked(event -> tField50.selectAll());
        tField500.setOnMouseClicked(event -> tField500.selectAll());
    }

    private void actualizarTotal() {
        //TextField[] textFields = { tField1000, tField500, tField200, tField100, tField50, tField20, tField10, tField5, tField2, tField1 };
        int total = 0;
        for (int i = 0; i < textFields.length; i++) {
            try {
                int cantidad = Integer.parseInt(textFields[i].getText());
                int subtotal = cantidad * valores[i];
                labels[i].setText("$" + subtotal); // Actualizar cada Label
                total += subtotal;
            } catch (NumberFormatException e) {
                labels[i].setText("$0"); // Si no es un número, pone $0
                textFields[i].setText("0");
                showErrorDialog("ErrorNumerico", "Al parecer hay un error con un numero", e.getMessage());
            }
        }
        tFieldTotal.setText("$" + total);
    }
    
    private void cerarModal(){
        Stage stage = (Stage) tField1.getScene().getWindow();
        stage.close();
    }

    private void showErrorDialog(String title, String message, String cause) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(title);
        alert.setHeaderText(cause);
        alert.showAndWait();
    }
}
