package com.salespointfxsales.www.controller;

import com.salespointfxsales.www.controller.modal.CantidadController;
import com.salespointfxsales.www.controller.modal.CobrarController;
import com.salespointfxsales.www.model.Categoria;
import com.salespointfxsales.www.model.Folio;
import com.salespointfxsales.www.model.dto.ResultadoCobro;
import com.salespointfxsales.www.model.SucursalProducto;
import com.salespointfxsales.www.model.Venta;
import com.salespointfxsales.www.model.VentaDetalle;
import com.salespointfxsales.www.service.CategoriaService;
import com.salespointfxsales.www.service.FolioService;
import com.salespointfxsales.www.service.SucursalProductoService;
import com.salespointfxsales.www.service.VentaService;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.ShortStringConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VentaController implements Initializable {

    private final FolioService fs;
    private final VentaService vs;

    private static final DecimalFormat formatoMoneda = new DecimalFormat("#");

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
    private ObservableList<VentaDetalle> olvd = FXCollections.observableArrayList();

    ;

    @FXML
    void cancelar(ActionEvent event) {
        try {
            if (!olvd.isEmpty()) {
                olvd.clear();
                labelTotal.setText("0");
            }
        } catch (Exception e) {
            showErrorDialog("Erro al limpiar la tabla!!", e.getMessage() + "\n" + e.getCause());
        }
    }

    @FXML
    void cobrar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modal/cobrar.fxml"));
            Parent root = loader.load();

            CobrarController cobrarController = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Cobrar");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            float totalVenta;
            try {
                totalVenta = Float.parseFloat(labelTotal.getText().replace("$", ""));
            } catch (NumberFormatException ex) {
                showErrorDialog("Error al convertir total", "El total no es un número válido.");
                return;
            }

            cobrarController.totalVenta(totalVenta);
            stage.showAndWait();

            ResultadoCobro resultado = cobrarController.getResultadoCobro();
            if (resultado != null && resultado.isExito()) {
                Venta v = new Venta();
                v.setFolio(labelFolio.getText());
                v.setTotalVenta(totalVenta);

                List<VentaDetalle> lvd = new ArrayList<>();
                for (VentaDetalle vd : olvd) {
                    VentaDetalle ventadetalle = new VentaDetalle(
                            vd.getIdVentaDetalle(), vd.getCantidad(), vd.getPrecio(),
                            vd.getSubTotal(), vd.getSucursalProducto(), v
                    );
                    lvd.add(ventadetalle);
                }
                v.setListVentaDetalle(lvd);

                try {
                    Venta guardada = vs.save(v, resultado,(Folio) labelFolio.getUserData());
                    if (guardada != null) {
                        olvd.clear();
                        tviewVentaDetalle.refresh();
                        labelTotal.setText("0");
                        actualizarFolio();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace(); // útil para desarrollo
                    showErrorDialog("Error al guardar la venta", ex.getMessage());
                }

            } else {
                showErrorDialog("Error al cobrar", "No se completó el cobro.");
            }

        } catch (IOException ex) {
            ex.printStackTrace(); // útil para desarrollo
            showErrorDialog("Error al abrir la ventana de cobro", ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            showErrorDialog("Error inesperado", ex.getMessage());
        }
    }

    @FXML
    void eliminar(ActionEvent event) {
        try {
            VentaDetalle vd = tviewVentaDetalle.getSelectionModel().getSelectedItem();
            if (vd != null) {
                olvd.remove(vd);
                labelTotal.setText(formatoMoneda.format(calcularTotal()));
            }
        } catch (Exception e) {
            showErrorDialog("Erro al eliminar", e.getMessage() + "\n" + e.getCause());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarCategrias();
        cargarProdutos();
        iniciarTabla();
        olvd.clear();
        actualizarFolio();
    }

    public void actualizarFolio() {
        // Supón que fs.getFolioVenta() retorna un objeto Folio con la información actual.
        Folio folioActual = fs.getFolioVenta();
        // Formatea el folio según el formato deseado, por ejemplo: "VEN-11-1"
        labelFolio.setText(folioActual.folioCompuesto());
        labelFolio.setUserData(folioActual);
        /*String folioFormateado = String.format("%s-%d-%d",
                folioActual.getAcronimoFolio().replace("-", ""),
                folioActual.getSucursalIdSucursal().getIdSucursal(),
                folioActual.getNumeroFolio());
        // Actualiza la propiedad en el ViewModel
        vvm.setFolio(folioFormateado);
        lblFoliio.setUserData(folioActual);*/
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

        // Detectamos un clic en la celda de cantidad y abrimos el modal
        columnUnidades.setCellFactory(param -> {
            TableCell<VentaDetalle, Short> cell = new TableCell<VentaDetalle, Short>() { // Asegúrate de usar Short aquí
                @Override
                public void updateItem(Short item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        setText(item != null ? item.toString() : "");
                        setOnMouseClicked(event -> {
                            if (!isEmpty()) {
                                VentaDetalle ventaDetalle = getTableRow().getItem();
                                cantidadSatge(ventaDetalle);  // Abre el modal para editar la cantidad
                            }
                        });
                    } else {
                        setText(null);
                    }
                }
            };
            return cell;
        });
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
        tviewVentaDetalle.setItems(olvd);
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
                //cantidadSatge("/fxml/modal/cantidad.fxml", sps.findByIdSucursalProducto(Short.parseShort(btn.getId())));
                agregarProducto(sps.findByIdSucursalProducto(Short.parseShort(btn.getId())));
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
                agregarProducto(sps.findByIdSucursalProducto(Short.parseShort(btn.getId())));
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

    public void agregarProducto(SucursalProducto producto) {
        for (VentaDetalle vd : olvd) {
            if (vd.getSucursalProducto().getIdSucursalProducto().equals(producto.getIdSucursalProducto())) {
                vd.setCantidad((short) (vd.getCantidad() + 1));
                vd.setSubTotal(vd.getCantidad() * vd.getPrecio());
                tviewVentaDetalle.refresh();
                labelTotal.setText(formatoMoneda.format(calcularTotal()));
                //labelTotal.setText("$" + calcularTotal());
                return;
            }
        }

        VentaDetalle nuevo = new VentaDetalle();
        nuevo.setSucursalProducto(producto);
        nuevo.setCantidad((short) 1);
        nuevo.setPrecio(producto.getPrecio());
        nuevo.setSubTotal(producto.getPrecio());

        olvd.add(nuevo);
        labelTotal.setText(formatoMoneda.format(calcularTotal()));
        //labelTotal.setText("$" + calcularTotal());
        tviewVentaDetalle.scrollTo(nuevo);
        tviewVentaDetalle.getSelectionModel().select(nuevo);

    }

    public void cantidadSatge(VentaDetalle vd) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modal/cantidad.fxml"));
            Parent root = loader.load();

            // Obtener el controlador del modal
            CantidadController cantidadController = loader.getController();
            cantidadController.setVentaDetalle(vd);  // Pasar el VentaDetalle para que se edite

            Stage stage = new Stage();
            stage.setTitle("Ingresar cantidad");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

            tviewVentaDetalle.refresh();
            labelTotal.setText("$" + calcularTotal());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private float calcularTotal() {
        float total = 0;
        for (VentaDetalle vd : olvd) {
            total += vd.getSubTotal();
        }
        return total;
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

}
