package com.salespointfxsales.www.controller.modal;

import com.salespointfxsales.www.model.Gasto;
import com.salespointfxsales.www.model.SucursalGasto;
import com.salespointfxsales.www.model.SucursalProducto;
import com.salespointfxsales.www.service.SucursalGastoService;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GastoController implements Initializable {
    private final SucursalGastoService sgs;

    @FXML
    private ChoiceBox<Gasto> cBoxGasto;

    @FXML
    private TableColumn<SucursalGasto, String> columnContrato;

    @FXML
    private TableColumn<SucursalGasto, LocalDateTime> columnFecha;

    @FXML
    private TableColumn<SucursalGasto, Gasto> columnGasto;

    @FXML
    private TableColumn<SucursalGasto, Integer> columnId;

    @FXML
    private TableColumn<SucursalGasto, Float> columnMonto;

    @FXML
    private TableColumn<SucursalGasto, String> columnObservaciones;

    @FXML
    private DatePicker dPickerInicio;

    @FXML
    private DatePicker dPicketFin;

    @FXML
    private TextArea tAreaObsevaciones;

    @FXML
    private TextField tFieldContrato;

    @FXML
    private TextField tFieldMonto;

    @FXML
    private TableView<SucursalGasto> tViewSucursalGastos;
    private ObservableList<SucursalGasto> olsg = FXCollections.observableArrayList();

    @FXML
    void buscar(ActionEvent event) {

    }

    @FXML
    void cancelar(ActionEvent event) {

    }

    @FXML
    void registrar(ActionEvent event) {

    }

    @FXML
    void reimprimir(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        iniciardPickers();
        iniciarTablaSucursalGasto();
    }

    private void iniciarTablaSucursalGasto() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("idSucursalGasto"));
        columnId.prefWidthProperty().bind(tViewSucursalGastos.widthProperty().multiply(0.1));

        columnContrato.setCellValueFactory(new PropertyValueFactory<>("contrato"));
        columnContrato.prefWidthProperty().bind(tViewSucursalGastos.widthProperty().multiply(0.1));

        columnFecha.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        columnFecha.prefWidthProperty().bind(tViewSucursalGastos.widthProperty().multiply(0.1));

        columnGasto.setCellValueFactory(new PropertyValueFactory<>("gasto"));
        columnGasto.prefWidthProperty().bind(tViewSucursalGastos.widthProperty().multiply(0.3));

        columnMonto.setCellValueFactory(new PropertyValueFactory<>("montoGasto"));
        columnMonto.prefWidthProperty().bind(tViewSucursalGastos.widthProperty().multiply(0.1));

        columnObservaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
        columnObservaciones.prefWidthProperty().bind(tViewSucursalGastos.widthProperty().multiply(0.3));

        // Agregar el formato para mostrar el precio como moneda
        columnFecha.setCellFactory(col -> {
            return new TableCell<SucursalGasto, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        // Formatear el precio como moneda
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                        //DecimalFormat df = new DecimalFormat("#.##");
                        setText(formatter.format(item));
                    } else {
                        setText(null);
                    }
                }
            };
        });
        // Agregar el formato para mostrar el precio como moneda
        columnMonto.setCellFactory(col -> {
            return new TableCell<SucursalGasto, Float>() {
                @Override
                protected void updateItem(Float item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        // Formatear el precio como moneda
                        DecimalFormat df = new DecimalFormat("#.##");
                        setText("$" + df.format(item));
                    } else {
                        setText(null);
                    }
                }
            };
        });
        olsg.setAll(sgs.findBySucursalEstatusSucursalTrueAndCreatedAtBetween(dPickerInicio.getValue(), dPicketFin.getValue()));
        tViewSucursalGastos.setItems(olsg);
    }

    private void iniciardPickers() {
        try {
            dPickerInicio.setValue(LocalDate.now());
            dPicketFin.setValue(LocalDate.now());
        } catch (Exception e) {
        }
    }

}
