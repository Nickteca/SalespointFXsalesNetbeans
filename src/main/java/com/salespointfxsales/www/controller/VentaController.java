package com.salespointfxsales.www.controller;

import com.salespointfxsales.www.controller.modal.CantidadController;
import com.salespointfxsales.www.model.Categoria;
import com.salespointfxsales.www.model.SucursalProducto;
import com.salespointfxsales.www.model.VentaDetalle;
import com.salespointfxsales.www.service.CategoriaService;
import com.salespointfxsales.www.service.SucursalProductoService;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VentaController implements Initializable {

    private final SucursalProductoService sps;
    private final CategoriaService cs;

    @FXML
    private AnchorPane aPaneCategorias;

    @FXML
    private AnchorPane aPaneContenidoProducto;

    @FXML
    private Button buttonCancelar;

    @FXML
    private Button buttonCobrar;

    @FXML
    private Button buttonEliminar;

    @FXML
    private TableColumn<VentaDetalle, Integer> columnId;

    @FXML
    private TableColumn<VentaDetalle, Float> columnPrecio;

    @FXML
    private TableColumn<VentaDetalle, SucursalProducto> columnProducto;

    @FXML
    private TableColumn<VentaDetalle, Float> columnSubtotal;

    @FXML
    private TableColumn<VentaDetalle, Short> columnUnidades;

    @FXML
    private HBox hBoxCategorias;

    @FXML
    private Label labelFolio;

    @FXML
    private Label labelTotal;

    @FXML
    private TableView<VentaDetalle> tviewVentaDetalle;
    private ObservableList<VentaDetalle> olvd;

    @FXML
    void cancelar(ActionEvent event) {

    }

    @FXML
    void cobrar(ActionEvent event) {

    }

    @FXML
    void eliminar(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarCategrias();
        cargarProdutos();
        iniciarTabla();
    }

    private void iniciarTabla() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("idVentaDetalle"));
        columnId.prefWidthProperty().bind(tviewVentaDetalle.widthProperty().multiply(0.1));

        columnPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnPrecio.prefWidthProperty().bind(tviewVentaDetalle.widthProperty().multiply(0.2));

        columnProducto.setCellValueFactory(new PropertyValueFactory<>("sucursalProducto"));
        columnProducto.prefWidthProperty().bind(tviewVentaDetalle.widthProperty().multiply(0.4));

        columnSubtotal.setCellValueFactory(new PropertyValueFactory<>("subTotal"));
        columnSubtotal.prefWidthProperty().bind(tviewVentaDetalle.widthProperty().multiply(0.2));

        columnUnidades.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        columnUnidades.prefWidthProperty().bind(tviewVentaDetalle.widthProperty().multiply(0.1));
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

    }

    private void cargarCategrias() {

        for (Categoria c : cs.findAll()) {
            Button bc = new Button(c.getNombreCategoria());
            bc.setId(c.getIdCategoria() + "");
            bc.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            bc.getStyleClass().add("botonCategoria");
            bc.setWrapText(true);
            bc.setOnAction(e -> cargarProductosPorCategoria(c));
            hBoxCategorias.getChildren().add(bc);
        }
    }

    private void cargarProdutos() {
        List<SucursalProducto> lsp = sps.findBySucursalEsttusSucursalTrueAndVendibleTrue();
        GridPane gp = new GridPane();
        gp.setPadding(new Insets(10)); // Espaciado alrededor de la cuadrícula
        gp.setHgap(5); // Espaciado horizontal entre columnas
        gp.setVgap(5); // Espaciado vertical entre filas
        gp.setAlignment(Pos.CENTER);
        // Configurar restricciones de columna y fila
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.ALWAYS); // Permitir que las columnas crezcan
        columnConstraints.setFillWidth(true); // Llenar todo el ancho de la columna

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.ALWAYS); // Permitir que las filas crezcan
        rowConstraints.setFillHeight(true); // Llenar toda la altura de la fila

        int numColumns = 3; // Número de columnas en la cuadrícula

        for (int j = 0; j < numColumns; j++) {
            gp.getColumnConstraints().add(columnConstraints);
        }

        for (int j = 0; j <= lsp.size() / numColumns; j++) {
            gp.getRowConstraints().add(rowConstraints);
        }
        for (int i = 0; i < lsp.size(); i++) {
            String producto = lsp.get(i).getProducto().getNombreProducto();
            Button btn = new Button(producto);
            btn.setId(lsp.get(i).getIdSucursalProducto() + "");
            btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Ajustar al tamaño máximo de la celda
            //btn.getStyleClass().add("botonesProductos");
            /* agregamos eventos a los botones com oescuchadoes */
            btn.setOnAction(event -> {
                cantidadSatge("/fxml/modal/cantidad.fxml", sps.findByIdSucursalProducto(Short.parseShort(btn.getId())));

            });
            Tooltip tooltip = new Tooltip("Precio: " + lsp.get(i).getPrecio());
            Tooltip.install(btn, tooltip); // Asociar el tooltip al botón

            // Calcular fila y columna
            int row = i / numColumns;
            int col = i % numColumns;
            // Habilitar ajuste de texto
            btn.setWrapText(true);
            // Agregamos estilos
            btn.getStyleClass().add("botonesProductos");

            // Agregar el botón al GridPane
            gp.add(btn, col, row);
        }
        ScrollPane scrollPane = new ScrollPane(gp);
        scrollPane.setFitToWidth(true); // Ajustar el ancho del contenido al tamaño del ScrollPane
        aPaneContenidoProducto.getChildren().clear();
        aPaneContenidoProducto.getChildren().add(scrollPane);
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
    }

    private void cargarProductosPorCategoria(Categoria categoria) {
        List<SucursalProducto> lsp = sps.findByCategoriaAndSucursalEstatusSucursalTrueAndVendibleTrue(categoria);
        // Este método lo debes tener en tu service/repository.

        GridPane gp = new GridPane();
        gp.setPadding(new Insets(10));
        gp.setHgap(5);
        gp.setVgap(5);
        gp.setAlignment(Pos.CENTER);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.ALWAYS);
        columnConstraints.setFillWidth(true);

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.ALWAYS);
        rowConstraints.setFillHeight(true);

        int numColumns = 3;
        for (int j = 0; j < numColumns; j++) {
            gp.getColumnConstraints().add(columnConstraints);
        }

        for (int j = 0; j <= lsp.size() / numColumns; j++) {
            gp.getRowConstraints().add(rowConstraints);
        }

        for (int i = 0; i < lsp.size(); i++) {
            SucursalProducto sp = lsp.get(i);
            Button btn = new Button(sp.getProducto().getNombreProducto());
            btn.setId(sp.getIdSucursalProducto() + "");
            btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            btn.setWrapText(true);
            btn.getStyleClass().add("botonesProductos");
            btn.setOnAction(event -> {
                //cantidadSatge("/fxml/cantidad.fxml", Integer.parseInt(btn.getId()));
            });
            Tooltip tooltip = new Tooltip("Precio: " + sp.getPrecio());
            Tooltip.install(btn, tooltip);

            int row = i / numColumns;
            int col = i % numColumns;
            gp.add(btn, col, row);
        }

        ScrollPane scrollPane = new ScrollPane(gp);
        scrollPane.setFitToWidth(true);
        aPaneContenidoProducto.getChildren().clear();
        aPaneContenidoProducto.getChildren().add(scrollPane);
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
    }

    public void cantidadSatge(String fxmlPath, SucursalProducto sp) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modal/cantidad.fxml"));
            Parent root = loader.load();

            CantidadController controller = loader.getController();
            controller.setOnCantidadConfirmada(cantidad -> {
                // Aquí recibes la cantidad y haces lo que necesites
                System.out.println("Cantidad elegida: " + cantidad);
                //agregarProductoATabla(sp, cantidad);
            });

            Stage stage = new Stage();
            stage.setTitle("Ingresar cantidad");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

}
