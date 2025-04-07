package com.salespointfxsales.www.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import org.springframework.stereotype.Component;

@Component
public class StarterController implements Initializable {


    @FXML
    private AnchorPane anchorFondo;

    @FXML
    private AnchorPane anchorfondo2;

    @FXML
    private Label labelCerarCaja;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        
    }

}
