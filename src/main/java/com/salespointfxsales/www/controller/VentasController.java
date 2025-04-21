package com.salespointfxsales.www.controller;

import com.salespointfxsales.www.model.SucursalProducto;
import com.salespointfxsales.www.model.VentaDetalle;
import com.salespointfxsales.www.service.VentaDetalleService;
import com.salespointfxsales.www.service.VentaService;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VentasController implements Initializable {

    private final VentaDetalleService vds;

    private static final DecimalFormat formatoMoneda = new DecimalFormat("#");

    @FXML
    private TableColumn<VentaDetalle, Float> columnPrecio;

    @FXML
    private TableColumn<VentaDetalle, SucursalProducto> columnProducto;

    @FXML
    private TableColumn<VentaDetalle, Float> columnSubtotal;

    @FXML
    private TableColumn<VentaDetalle, Short> columnUnidades;

    @FXML
    private Label labelTotal;

    @FXML
    private TableView<VentaDetalle> tvVentaDetalle;
    private ObservableList<VentaDetalle> olvd = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        llenarTabla();
    }

    private void llenarTabla() {
        try {
            columnPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
            columnPrecio.prefWidthProperty().bind(tvVentaDetalle.widthProperty().multiply(0.2));

            columnProducto.setCellValueFactory(new PropertyValueFactory<>("sucursalProducto"));
            columnProducto.prefWidthProperty().bind(tvVentaDetalle.widthProperty().multiply(0.4));

            columnSubtotal.setCellValueFactory(new PropertyValueFactory<>("subTotal"));
            columnSubtotal.prefWidthProperty().bind(tvVentaDetalle.widthProperty().multiply(0.2));

            columnUnidades.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
            columnUnidades.prefWidthProperty().bind(tvVentaDetalle.widthProperty().multiply(0.1));

            // Agregar el formato para mostrar el precio como moneda
            columnPrecio.setCellFactory(col -> {
                return new TableCell<VentaDetalle, Float>() {
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
            // Agregar el formato para mostrar el subtotal como moneda
            columnSubtotal.setCellFactory(col -> {
                return new TableCell<VentaDetalle, Float>() {
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
            List<VentaDetalle> lvd = vds.ventasXsucursalXactivasXcorte();
            float granTotal = lvd.stream()
                    .map(VentaDetalle::getSubTotal)
                    .reduce(0f, Float::sum);
            labelTotal.setText(formatoMoneda.format(granTotal));
            olvd.setAll(lvd);
            tvVentaDetalle.setItems(olvd);
        } catch (IllegalStateException ise) {
            mostrarAlerta("Advertencia", ise.getMessage(), Alert.AlertType.WARNING);
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al cargar los datos: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace(); // Esto sí lo dejamos para depuración en consola
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
