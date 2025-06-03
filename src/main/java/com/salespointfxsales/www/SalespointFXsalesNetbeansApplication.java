package com.salespointfxsales.www;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SalespointFXsalesNetbeansApplication {

    public static void main(String[] args) {
        Application.launch(MainApp.class, args); // Lanzamos JavaFX
    }
}