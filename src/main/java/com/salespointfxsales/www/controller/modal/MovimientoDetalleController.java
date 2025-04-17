package com.salespointfxsales.www.controller.modal;

import com.salespointfxsales.www.model.Folio;
import com.salespointfxsales.www.model.MovimientoInventario;
import com.salespointfxsales.www.model.MovimientoInventarioDetalle;
import com.salespointfxsales.www.model.Sucursal;
import com.salespointfxsales.www.model.SucursalGasto;
import com.salespointfxsales.www.model.SucursalProducto;
import com.salespointfxsales.www.repo.SucursalProductoRepo;
import com.salespointfxsales.www.service.FolioService;
import com.salespointfxsales.www.service.MovimientoInventarioDetalleService;
import com.salespointfxsales.www.service.SucursalService;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovimientoDetalleController implements Initializable{
    private final FolioService fs;
    private final SucursalService ss;
    private final MovimientoInventarioDetalleService mids;

    @FXML
    private ChoiceBox<Folio> cBoxFolio;
    private ObservableList<Folio> olf;

    @FXML
    private ChoiceBox<Sucursal> cBoxSucursal;
    private ObservableList<Sucursal> ols;

    @FXML
    private TableColumn<MovimientoInventarioDetalle, Float> columnCantidad;

    @FXML
    private TableColumn<MovimientoInventarioDetalle, Integer> columnId;

    @FXML
    private TableColumn<MovimientoInventarioDetalle, SucursalProducto> columnProducto;

    @FXML
    private TextField tFieldDescripcion;

    @FXML
    private TextField tFieldFolioCompuesto;

    @FXML
    private TextField tFieldId;

    @FXML
    private TableView<MovimientoInventarioDetalle> tViewMovimientoDetalle;
    private ObservableList<MovimientoInventarioDetalle> olmid;

    public void cargarProductosMovimiento(MovimientoInventario mi) {
        tFieldDescripcion.setText(mi.getDecripcion());
        cBoxSucursal.getSelectionModel().select(mi.getSucursalDestino());
        cBoxFolio.getSelectionModel().select(mi.getFolio());
        tFieldId.setText(mi.getIdMovimientoInventario() + "");
        tFieldFolioCompuesto.setText(mi.getFolioCompuesto());
        cBoxFolio.setDisable(true);
        //System.out.println(mi.getListMovimientoInventarioDetalle().getFirst().getUnidades());
        //List<MovimientoInventarioDetalle> lmid = mids.findByMovimientoInventario(mi);
        olmid = FXCollections.observableArrayList(mids.findByMovimientoInventario(mi));
        tViewMovimientoDetalle.setItems(olmid);
        for (MovimientoInventarioDetalle mid : mids.findByMovimientoInventario(mi)) {
            System.out.println(mid.getSucursalProducto());
        }
            
            /*// HBox para contener el producto y la cantidad
            HBox hbox = new HBox(10);
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.getStyleClass().add("item-hbox");

            // Estilo opcional para el HBox (bordes visibles para depuración)
            // hbox.setStyle("-fx-padding: 5; -fx-border-color: lightgray;");
            Label labelId = new Label(mid.getIdMovimientoInventarioDetalle() + "");
            labelId.setMinWidth(20);
            labelId.setPrefWidth(20);
            labelId.setMaxWidth(20);

            Label label = new Label(mid.getSucursalProducto().getProducto().getNombreProducto());
            label.getStyleClass().add("item-label");
            label.setPrefWidth(100);
            TextField cantidadTextField = new TextField(); // Cantidad por defecto
            cantidadTextField.getStyleClass().add("item-textfield");

            cantidadTextField.setOnMouseClicked(event -> {
                cantidadTextField.selectAll(); // Selecciona todo el texto al hacer clic
            });
            cantidadTextField.setText(formato.format(mid.getUnidades()));
            cantidadTextField.setEditable(true);
            cantidadTextField.setPrefWidth(50);

            Button eliminarBtn = new Button("❌");
            eliminarBtn.getStyleClass().add("button-eliminar");
            eliminarBtn.setOnAction(e -> {
                vBoxProductosSeleccionados.getChildren().remove(hbox);
                eliminarProducto((labelId.getText() != null && !labelId.getText().trim().isEmpty()) ? Integer.parseInt(labelId.getText()) : null);
            });

            hbox.getChildren().add(labelId);
            hbox.getChildren().add(label);
            hbox.getChildren().add(cantidadTextField);
            hbox.getChildren().add(eliminarBtn);

            vBoxProductosSeleccionados.getChildren().add(hbox);*/
        //}
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        iniciarTabal();
        olf = FXCollections.observableArrayList(fs.findBySucursalEstatusSucursalTrue());
        olf.remove(0);
        cBoxFolio.setItems(olf);
        
        ols = FXCollections.observableArrayList(ss.findAll());
        cBoxSucursal.setItems(ols);
    }
    
    private void iniciarTabal(){
        columnId.setCellValueFactory(new PropertyValueFactory<>("idMovimientoInventarioDetalle"));
        columnId.prefWidthProperty().bind(tViewMovimientoDetalle.widthProperty().multiply(0.2));

        columnCantidad.setCellValueFactory(new PropertyValueFactory<>("unidades"));
        columnCantidad.prefWidthProperty().bind(tViewMovimientoDetalle.widthProperty().multiply(0.4));
        columnCantidad.setCellFactory(col -> {
            return new TableCell<MovimientoInventarioDetalle, Float>() {
                @Override
                protected void updateItem(Float item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        // Formatear el precio como moneda
                        DecimalFormat df = new DecimalFormat("#.##");
                        setText( df.format(item));
                    } else {
                        setText(null);
                    }
                }
            };
        });

        columnProducto.setCellValueFactory(new PropertyValueFactory<>("sucursalProducto"));
        columnProducto.prefWidthProperty().bind(tViewMovimientoDetalle.widthProperty().multiply(0.4));

        
    }

}
