package com.salespointfxsales.www.controller.modal;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class CantidadController implements Initializable {

    // Bandera para saber si es la primera edición.
    private boolean primeraEdicion = true;

    @FXML
    private Button btn0;

    @FXML
    private Button btn1;

    @FXML
    private Button btn2;

    @FXML
    private Button btn3;

    @FXML
    private Button btn4;

    @FXML
    private Button btn5;

    @FXML
    private Button btn6;

    @FXML
    private Button btn7;

    @FXML
    private Button btn8;

    @FXML
    private Button btn9;

    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnBorrar;

    @FXML
    private Button btnCancelar;

    @FXML
    private TextField textCantidad;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private int productoId;

    // Método para establecer el ID del producto
    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Manejador para la tecla ENTER en el TextField
        textCantidad.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // Ejecuta la acción de aceptar
                btnAceptar.fire();
                // Evita que se propague el evento
                event.consume();
                // Se solicita el foco nuevamente en el TextField (por si se pierde)
                textCantidad.requestFocus();
            }
        });

        // Manejo de teclado: si es la primera edición y se presiona una tecla numérica,
        // se limpia el TextField.
        textCantidad.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            // Verificamos que sea un dígito (puedes ampliar la validación según tus
            // necesidades)
            if (primeraEdicion && event.getCharacter().matches("[0-9]")) {
                textCantidad.clear();
                primeraEdicion = false;
            }
        });
        // Filtro para solo aceptar números y evitar que empiece con 0
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[1-9][0-9]*") || newText.isEmpty()) {
                return change;
            }
            return null; // Rechaza la entrada si no cumple la condición
        };

        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        textCantidad.setTextFormatter(textFormatter);
        textCantidad.requestFocus();
        // Capturamos el cierre de la ventana (por la X) para reiniciar la bandera
        Platform.runLater(() -> {
            Stage stage = (Stage) textCantidad.getScene().getWindow();
            stage.setOnCloseRequest(e -> {
                primeraEdicion = true;
            });
        });
    }

    @FXML
    void cerrar(ActionEvent event) {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
        primeraEdicion = true;
    }

    @FXML
    void agregarCantidad(ActionEvent event) {
        // Obtener el botón que disparó el evento
        /*
		 * Button sourceButton = (Button) event.getSource();
		 * textCantidad.appendText(sourceButton.getText());
         */
        handleNumero(textCantidad, ((Button) event.getSource()).getText());
    }

    /**
     * Manejador para los botones numéricos. Si es la primera edición, se borra
     * el valor inicial ("1"). Luego se agrega el dígito presionado al
     * TextField.
     */
    private void handleNumero(TextField textField, String digito) {
        if (primeraEdicion) {
            textField.clear();
            primeraEdicion = false;
        }
        textField.appendText(digito);
        textField.requestFocus();
        // Posicionar el caret al final para evitar la selección completa.
        textField.positionCaret(textField.getLength());
    }

    @FXML
    void aceptar(ActionEvent event) {
        try {
            int cantidad = Integer.parseInt(textCantidad.getText());
            System.out.println("🔥 Evento disparado: cantidadSeleccionada -> " + cantidad);
            support.firePropertyChange("cantidadSeleccionada", null, new Object[]{productoId, cantidad});
            cerrar(event);

        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error numerico");
            alert.setHeaderText(e.getMessage() + " " + e.getCause());
            alert.showAndWait();
        }
    }

    @FXML
    void borrar(ActionEvent event) {
        String text = textCantidad.getText();
        if (!text.isEmpty()) {
            textCantidad.setText(text.substring(0, text.length() - 1));
        } else {
            textCantidad.requestFocus();
        }
    }

}
