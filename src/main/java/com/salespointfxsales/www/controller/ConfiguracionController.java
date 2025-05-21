package com.salespointfxsales.www.controller;

import com.salespointfxsales.www.model.Configuracion;
import com.salespointfxsales.www.model.Sucursal;
import com.salespointfxsales.www.model.SucursalProducto;
import com.salespointfxsales.www.service.ConfiguracionService;
import com.salespointfxsales.www.service.SucursalService;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConfiguracionController implements Initializable {

    private final ConfiguracionService cs;
    private final SucursalService ss;

    @FXML
    private Button buttonEliminar;

    @FXML
    private Button buttonGuardar;

    @FXML
    private Button buttonNuevo;

    @FXML
    private TableColumn<Configuracion, String> columnClave;

    @FXML
    private TableColumn<Configuracion, String> columnDescripcion;

    @FXML
    private TableColumn<Configuracion, Integer> columnId;

    @FXML
    private TableColumn<Configuracion, Sucursal> columnSucursal;

    @FXML
    private TableColumn<Configuracion, String> columnValor;

    @FXML
    private TableView<Configuracion> tViewConfiguracion;
    private ObservableList<Configuracion> olc;

    @FXML
    void eliminar(ActionEvent event) {
        try {
            Configuracion seleccionada = tViewConfiguracion.getSelectionModel().getSelectedItem();
            if (seleccionada != null) {
                cs.delete(seleccionada);
                olc.remove(seleccionada);
                tViewConfiguracion.refresh();
            }
        } catch (Exception e) {
            mostrarAlertaError("Error", e.getMessage());
        }
    }

    @FXML
    void guardar(ActionEvent event) {
        try {
            olc = tViewConfiguracion.getItems();
            List<Configuracion> actualizados = new ArrayList<>();

            for (int i = 0; i < olc.size(); i++) {
                Configuracion original = olc.get(i);
                Configuracion guardada = cs.save(original);
                cs.save(guardada);
            }

            // Refresca la tabla con los IDs actualizados
            tViewConfiguracion.setItems(FXCollections.observableArrayList(actualizados));
            tViewConfiguracion.refresh();
        } catch (Exception e) {
            mostrarAlertaError("Error", e.getMessage());
        }
    }

    @FXML
    void nuevo(ActionEvent event) {
        try {
            Configuracion nueva = new Configuracion(null, "nueva_clave", "nuevo_valor", "nueva_descripción", ss.findByEstatusSucursalTrue());
            tViewConfiguracion.getItems().add(nueva);
        } catch (Exception e) {
            mostrarAlertaError("Error", e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        iiciarTablaConfiguracion();
    }

    private void iiciarTablaConfiguracion() {
        try {
            columnId.setCellValueFactory(new PropertyValueFactory<>("idConfiguracion"));
            columnId.prefWidthProperty().bind(tViewConfiguracion.widthProperty().multiply(0.1));

            columnValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
            columnValor.prefWidthProperty().bind(tViewConfiguracion.widthProperty().multiply(0.1));
            columnValor.setCellFactory(TextFieldTableCell.forTableColumn());
            columnValor.setOnEditCommit(event -> {
                Configuracion config = event.getRowValue();
                config.setValor(event.getNewValue());
                cs.save(config);
            });

            columnClave.setCellValueFactory(new PropertyValueFactory<>("clave"));
            columnClave.prefWidthProperty().bind(tViewConfiguracion.widthProperty().multiply(0.3));
            //columnClave.prefWidthProperty().bind(tViewConfiguracion.widthProperty().multiply(0.1));

            columnDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
            columnDescripcion.prefWidthProperty().bind(tViewConfiguracion.widthProperty().multiply(0.3));

            columnSucursal.setCellValueFactory(new PropertyValueFactory<>("sucursal"));
            columnSucursal.prefWidthProperty().bind(tViewConfiguracion.widthProperty().multiply(0.2));

            olc = FXCollections.observableArrayList(cs.findAll());
            tViewConfiguracion.setItems(olc);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
