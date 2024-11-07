package controlador;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Alert.AlertType;

public class AcercaDeController implements Initializable {

	@FXML private Hyperlink EnlaceFlaticon;
	@FXML private Hyperlink EnlaceFreepik;
	@FXML private Hyperlink EnlaceAEMET;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

		EnlaceFlaticon.setOnMouseClicked((event) -> asignarEnlace("https://www.flaticon.es/"));
		EnlaceFreepik.setOnMouseClicked((event) -> asignarEnlace("https://www.freepik.es/"));
		EnlaceAEMET.setOnMouseClicked((event) -> asignarEnlace("https://www.aemet.es/es/portada"));
	}


	private void asignarEnlace(String enlace) {

		try {
			// Abrir enlace en el navegador si la instancia del escritorio es soportada
			Desktop.getDesktop().browse(new URI(enlace));

		} catch (IOException | URISyntaxException e) {

			// Si no sacar error de URL no encontrada
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error al abrir el navegador");
			alert.setHeaderText("Escritorio no soportado.");
			alert.setContentText("No se ha podido abir el enlace.");
			alert.showAndWait();
		}
	}
}
