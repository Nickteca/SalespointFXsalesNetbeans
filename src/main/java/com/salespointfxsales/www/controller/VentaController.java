package com.salespointfxsales.www.controller;

import com.salespointfxsales.www.model.Categoria;
import com.salespointfxsales.www.model.SucursalProducto;
import com.salespointfxsales.www.service.CategoriaService;
import com.salespointfxsales.www.service.SucursalProductoService;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VentaController implements Initializable {

    private final SucursalProductoService sps;
    private final CategoriaService cs;

    @FXML
    private AnchorPane aPaneContenidoProducto;
    @FXML
    private AnchorPane aPaneCategorias;
    @FXML
    private HBox hBoxCategorias;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarCategrias();
        cargarProdutos();
    }

    private void cargarCategrias() {

        for (Categoria c : cs.findAll()) {
            Button bc = new Button(c.getNombreCategoria());
            bc.setId(c.getIdCategoria() + "");
            bc.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            bc.getStyleClass().add("botonesCategorias");
            bc.setWrapText(true);
            bc.setOnAction(e -> cargarProductosPorCategoria(c));
            hBoxCategorias.getChildren().add(bc);
        }
        //aPaneCategorias.getChildren().add(hBoxCategorias);
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
                //cantidadSatge("/fxml/cantidad.fxml", Integer.parseInt(btn.getId()));

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
}
