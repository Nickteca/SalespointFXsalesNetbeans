package com.salespointfxsales.www.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

@Component
public class AbrirCajaController implements Initializable{

    @FXML
    private Button buttonAbrirCaja;

    @FXML
    private Button buttonCancelar;

    @FXML
    private Label labelsucursal;

    @FXML
    private TextField tFieldSaldoAnteriro;

    @FXML
    private TextField tFieldSaldoCaja;

    @FXML
    void abrirCaja(ActionEvent event) {

    }

    @FXML
    void cancelar(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    public void parametros(String sucursal){
        labelsucursal.setText(sucursal);
    }
}
