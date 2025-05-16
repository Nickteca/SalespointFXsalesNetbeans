package com.salespointfxsales.www.controller.modal;

import com.salespointfxsales.www.model.SucursalPedido;
import com.salespointfxsales.www.model.SucursalPedidoDetalle;
import com.salespointfxsales.www.model.SucursalProducto;
import com.salespointfxsales.www.service.SucursalPedidoService;
import com.salespointfxsales.www.service.SucursalProductoService;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
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
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PedidoController implements Initializable {

    private final SucursalProductoService sps;
    private final SucursalPedidoService spedidos;

    @FXML
    private Button buttonCancelar;

    @FXML
    private Button buttonEnviar;

    @FXML
    private ListView<SucursalProducto> lViewProductos;
    private ObservableList<SucursalProducto> olsp;
    @FXML
    private ListView<SucursalPedidoDetalle> lViewPedido;
    private ObservableList<SucursalPedidoDetalle> olspd;

    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage) buttonCancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void enviar(ActionEvent event) {
        try {
            if (olspd.isEmpty()) {
                showErrorDialog("Error", "Lista Vacia");
                return;
            }
            SucursalPedido spedido = new SucursalPedido();
            List<SucursalPedidoDetalle> lspedidod = new ArrayList<>();
            for (SucursalPedidoDetalle detalle : olspd) {
                detalle.setSucursalPedido(spedido);
                lspedidod.add(detalle);
            }
            spedido.setListSucursalpedidoDetalle(lspedidod);
            if (spedidos.save(spedido) != null) {
                buttonCancelar.fire();
            }
        } catch (Exception e) {
            showErrorDialog("Error", "Exception: " + e.getMessage());
        }
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
                    if(canti<=0){
                        throw new IllegalArgumentException("La cantodad debe ser maypor a 0");
                    }
                    agregarProductoPedido(sp, canti);
                }  catch (NumberFormatException e) {
                    showErrorDialog("Error numerico", "Error en la cantidad: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    showErrorDialog("Error numerico", "Error Ilegal: " + e.getMessage());
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
        olspd = FXCollections.observableArrayList();

        // Mostrar productos de forma personalizada
        lViewProductos.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(SucursalProducto item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    DecimalFormat df = new DecimalFormat("#.##");
                    setText(item.getProducto().getNombreProducto() + " - " + df.format(item.getInventario()));
                }
            }
        });

        // Mostrar detalles del pedido de forma personalizada
        lViewPedido.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(SucursalPedidoDetalle item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getCantidad() + " x " + item.getSucursalProducto().getProducto().getNombreProducto());
                }
            }
        });
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
