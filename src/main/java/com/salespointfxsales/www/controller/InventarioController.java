package com.salespointfxsales.www.controller;

import com.salespointfxsales.www.model.Categoria;
import com.salespointfxsales.www.model.Producto;
import com.salespointfxsales.www.model.SucursalProducto;
import com.salespointfxsales.www.model.VentaDetalle;
import com.salespointfxsales.www.service.SucursalProductoService;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventarioController implements Initializable {

    private final SucursalProductoService sps;

    @FXML
    private TableColumn<SucursalProducto, Categoria> columnCategoria;
    @FXML
    private TableColumn<SucursalProducto, Short> columnId;
    @FXML
    private TableColumn<SucursalProducto, Float> columnInventario;
    @FXML
    private TableColumn<SucursalProducto, Float> columnPrecio;
    @FXML
    private TableColumn<SucursalProducto, Producto> columnProducto;
    @FXML
    private TableView<SucursalProducto> tViewSucursalProducto;
    private ObservableList<SucursalProducto> olsp = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mostrarSucursalProdcto();
    }

    private void mostrarSucursalProdcto() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("idSucursalProducto"));
        columnId.prefWidthProperty().bind(tViewSucursalProducto.widthProperty().multiply(0.1));

        columnPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnPrecio.prefWidthProperty().bind(tViewSucursalProducto.widthProperty().multiply(0.2));

        columnProducto.setCellValueFactory(new PropertyValueFactory<>("producto"));
        columnProducto.prefWidthProperty().bind(tViewSucursalProducto.widthProperty().multiply(0.4));

        columnCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        columnCategoria.prefWidthProperty().bind(tViewSucursalProducto.widthProperty().multiply(0.2));

        columnInventario.setCellValueFactory(new PropertyValueFactory<>("inventario"));
        columnInventario.prefWidthProperty().bind(tViewSucursalProducto.widthProperty().multiply(0.1));

        // Agregar el formato para mostrar el precio como moneda
        columnInventario.setCellFactory(col -> {
            return new TableCell<SucursalProducto, Float>() {
                @Override
                protected void updateItem(Float item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        // Formatear el precio como moneda
                        DecimalFormat df = new DecimalFormat("#.##");
                        setText(df.format(item));
                    } else {
                        setText(null);
                    }
                }
            };
        });
        // Agregar el formato para mostrar el precio como moneda
        columnPrecio.setCellFactory(col -> {
            return new TableCell<SucursalProducto, Float>() {
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
        olsp.setAll(sps.findByInventariable());
        tViewSucursalProducto.setItems(olsp);
    }
}
