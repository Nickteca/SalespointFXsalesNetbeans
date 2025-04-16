package com.salespointfxsales.www.controller.modal;

import com.salespointfxsales.www.model.Folio;
import com.salespointfxsales.www.model.MovimientoInventarioDetalle;
import com.salespointfxsales.www.model.Sucursal;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MovimientoDetalleController {

    @FXML
    private ChoiceBox<Folio> cBoxFolio;

    @FXML
    private ChoiceBox<Sucursal> cBoxSucursal;

    @FXML
    private TableColumn<MovimientoInventarioDetalle, ?> columnCantidad;

    @FXML
    private TableColumn<MovimientoInventarioDetalle, ?> columnId;

    @FXML
    private TableColumn<MovimientoInventarioDetalle, ?> columnProducto;

    @FXML
    private TextField tFieldDescripcion;

    @FXML
    private TextField tFieldFolioCompuesto;

    @FXML
    private TextField tFieldId;

    @FXML
    private TableView<MovimientoInventarioDetalle> tViewMovimientoDetalle;

}
