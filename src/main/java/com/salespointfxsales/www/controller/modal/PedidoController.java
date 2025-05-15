package com.salespointfxsales.www.controller.modal;

import com.salespointfxsales.www.model.SucursalPedidoDetalle;
import com.salespointfxsales.www.model.SucursalProducto;
import com.salespointfxsales.www.service.SucursalProductoService;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PedidoController implements Initializable {

    private final SucursalProductoService sps;

    @FXML
    private Button buttonCancelar;

    @FXML
    private Button buttonEnviar;

    @FXML
    private ListView<SucursalProducto> lViewProductos;
    private ObservableList<SucursalProducto> olsp;
    @FXML
    private ListView<SucursalPedidoDetalle> lViewPedido;
    private ObservableList<SucursalPedidoDetalle> olspd = FXCollections.observableArrayList();

    @FXML
    void cancelar(ActionEvent event) {

    }

    @FXML
    void enviar(ActionEvent event) {

    }

    @FXML
    void agregarProducto(MouseEvent event) {
        if (event.getClickCount() == 2) {
            SucursalProducto sp = lViewProductos.getSelectionModel().getSelectedItem();
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Entrada de Cantidad");
            dialog.setHeaderText(sp + ":");
            dialog.setContentText("Cantidad:");
            // Mostrar el diálogo y esperar la respuesta
            Optional<String> result = dialog.showAndWait();
            result.ifPresentOrElse(cantidad -> {
                try {
                    int canti = Integer.parseInt(cantidad);
                    agregarProductoPedido(sp, canti);
                } catch (NumberFormatException e) {
                    showErrorDialog("Error numerico", "erro en la cantidad: " + e.getMessage());
                } catch (Exception e) {
                    showErrorDialog("Error Desconocido", e.getMessage());
                }
            }, () -> {
                showErrorDialog("Error", "Cantidad Vacia");
            });
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarProductos();
    }

    private void cargarProductos() {
        try {
            olsp = FXCollections.observableArrayList(sps.findBySucursalEstatusSucursalTrueAndProductoEsPaqueteFalse());
            lViewProductos.setItems(olsp);
        } catch (Exception e) {
            showErrorDialog("Error", e.getMessage());
        }
    }

    private void agregarProductoPedido(SucursalProducto sp, int cantidad) {
        try {
            boolean existe = olspd.stream().anyMatch(detalle -> detalle.getSucursalProducto().equals(sp));
            if (existe) {
                showErrorDialog("Producto repetido", "El producto ya está en el pedido.");
                return;
            }
            SucursalPedidoDetalle spd = new SucursalPedidoDetalle();
            spd.setCantidad(cantidad);
            spd.setSucursalProducto(sp);
            spd.setIdSucursalPedidoDetalle(null);

            olspd.add(spd);
            lViewPedido.setItems(olspd);
        } catch (Exception e) {
            showErrorDialog("Error", e.getMessage());
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
