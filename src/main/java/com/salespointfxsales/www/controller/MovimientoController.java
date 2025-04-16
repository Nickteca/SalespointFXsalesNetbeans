package com.salespointfxsales.www.controller;

import com.salespointfxadmin.www.model.enums.Naturaleza;
import com.salespointfxsales.www.model.Folio;
import com.salespointfxsales.www.model.MovimientoInventario;
import com.salespointfxsales.www.model.Sucursal;
import com.salespointfxsales.www.service.FolioService;
import com.salespointfxsales.www.service.MovimientoInventarioService;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovimientoController implements Initializable {

    private final FolioService fs;
    private final MovimientoInventarioService mis;

    @FXML
    private Button buttonBuscar;

    @FXML
    private ChoiceBox<Folio> cBoxFolio;
    private ObservableList<Folio> olf;

    @FXML
    private TableColumn<MovimientoInventario, String> columnDescripcion;

    @FXML
    private TableColumn<MovimientoInventario, LocalDateTime> columnFecha;

    @FXML
    private TableColumn<MovimientoInventario, String> columnFolio;

    @FXML
    private TableColumn<MovimientoInventario, Integer> columnId;

    @FXML
    private TableColumn<MovimientoInventario, Naturaleza> columnNMaturaleza;

    @FXML
    private TableColumn<MovimientoInventario, Sucursal> columnSucursalDestino;

    @FXML
    private DatePicker dPickerInicio;

    @FXML
    private DatePicker dPicketFin;

    @FXML
    private TableView<MovimientoInventario> tViewMovimiento;
    private ObservableList<MovimientoInventario> olmi;

    @FXML
    void buscar(ActionEvent event) {
        try {
            LocalDate inicio = dPickerInicio.getValue();
            LocalDate fin = dPicketFin.getValue();
            if (inicio == null || fin == null) {
                showMensages("Fechas vacias", "Debes seleccionar am bas fechas","");
                return;
            }

            if (fin.isBefore(inicio)) {
                 showMensages("Rango incorrecto", "La fecha fin no pyuede ser menor a la de inicio","");
                return;
            }
            Folio f = cBoxFolio.getSelectionModel().getSelectedItem();
            List<MovimientoInventario> lmi = mis.findBySucursalEstatusSucursalTrueAndCreatedAtBetweenAndNombreFolio(inicio,
                    fin, f.getNombreFolio());
            if (lmi.isEmpty()) {
                throw new Exception("Esta vacia la lista");
            }
            olmi = FXCollections.observableArrayList(lmi);
            tViewMovimiento.setItems(olmi);

        } catch (Exception e) {
            showMensages("Error en buscar movimientos", "Hay un erro en el movimiento", e.getMessage()+"\n"+e.getCause());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dPickerInicio.setValue(LocalDate.now());
        dPicketFin.setValue(LocalDate.now());
        iniciarTablaMovmientos();
        iniciarChoiceBox();
    }

    private void iniciarTablaMovmientos() {
        /* SE INICIA TODO DE LOS PRODUCTO SUCURSAL */
        columnId.setCellValueFactory(new PropertyValueFactory<>("idMovimientoInventario"));
        columnId.prefWidthProperty().bind(tViewMovimiento.widthProperty().multiply(0.1));

        columnDescripcion.setCellValueFactory(new PropertyValueFactory<>("decripcion"));
        columnDescripcion.prefWidthProperty().bind(tViewMovimiento.widthProperty().multiply(0.3));

        columnFecha.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        columnFecha.prefWidthProperty().bind(tViewMovimiento.widthProperty().multiply(0.2));

        columnFolio.setCellValueFactory(new PropertyValueFactory<>("folioCompuesto"));
        columnFolio.prefWidthProperty().bind(tViewMovimiento.widthProperty().multiply(0.1));

        columnNMaturaleza.setCellValueFactory(new PropertyValueFactory<>("naturaleza"));
        columnNMaturaleza.prefWidthProperty().bind(tViewMovimiento.widthProperty().multiply(0.2));

        columnSucursalDestino.setCellValueFactory(new PropertyValueFactory<>("sucursalDestino"));
        columnSucursalDestino.prefWidthProperty().bind(tViewMovimiento.widthProperty().multiply(0.1));

        tViewMovimiento.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Doble clic
                MovimientoInventario mis = tViewMovimiento.getSelectionModel().getSelectedItem();
                if (mis != null) {
                    //mostrarMovimiento(mis);
                }
            }
        });
    }

    private void iniciarChoiceBox() {
        olf = FXCollections.observableArrayList(fs.findBySucursalEstatusSucursalTrue());
        olf.remove(0);
        cBoxFolio.setItems(olf);
        cBoxFolio.getSelectionModel().selectFirst();

    }

    private void showMensages(String titile, String mensage, String errors) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle(titile);
        error.setHeaderText(mensage);
        error.setContentText(errors);
        error.show();
    }
}
