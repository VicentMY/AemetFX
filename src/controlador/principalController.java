package controlador;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import org.controlsfx.control.textfield.TextFields;

import excepciones.DescargaFallidaException;
import excepciones.MunicipioNoExisteException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.ElTiempo;
import utiles.Ficheros;

public class principalController implements Initializable {

	@FXML private Label NombreMunicipio;
	@FXML private TextField CampoTexto;
	@FXML private Button botonBuscar;
	@FXML private Button botonAcercaDe;
	@FXML private ComboBox<String> comboComunidad;
	@FXML private ComboBox<String> comboProvincia;
	@FXML private ComboBox<String> comboMunicipio;

	@FXML private diaController dia_1Controller;
	@FXML private diaController dia_2Controller;
	@FXML private diaController dia_3Controller;
	@FXML private diaController dia_4Controller;
	@FXML private diaController dia_5Controller;
	@FXML private diaController dia_6Controller;
	@FXML private diaController dia_7Controller;

	private ElTiempo app;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

		// Ocultar los dias antes de insertar info
		ocultarDias();

		Ficheros mapas = new Ficheros();
		HashSet<String> autocompletado = new HashSet<String>();

		// ComboBox

		ObservableList<String> comunidades = FXCollections.observableArrayList(obtenerNombres(mapas.mapaComunidades));		

		comboComunidad.setItems(comunidades);
		
		// Añadir Listener a los comboBoxes para filtrar
		comboComunidad.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

			if (newSelection != null) {
				
				// Limpiar los comboBoxes y el campo de texto
				comboProvincia.getSelectionModel().clearSelection();
				comboMunicipio.getSelectionModel().clearSelection();
				comboMunicipio.setDisable(true);
				CampoTexto.setText("");
				
				actualizarSiguienteComboBox(newSelection, comboProvincia, mapas.mapaProvinciasComunidades);
			}
		});
		comboProvincia.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

			if (newSelection != null) {

				actualizarSiguienteComboBox(newSelection, comboMunicipio, mapas.mapaMunicipiosProvincias);
			}
		});

		// Desactivar comboboxes
		comboProvincia.setDisable(true);
		comboMunicipio.setDisable(true);

		// Filtrar y reactivar ComboBoxes
		comboComunidad.setOnAction((event) -> comboProvincia.setDisable(false)); 
		comboProvincia.setOnAction((event) -> comboMunicipio.setDisable(false));
		comboMunicipio.setOnAction((event) -> CampoTexto.setText(comboMunicipio.getSelectionModel().getSelectedItem() == null ? "" : comboMunicipio.getSelectionModel().getSelectedItem().toString()));

		// Autocompletar
		for (Map.Entry<String, String> entry : mapas.mapaMunicipios.entrySet()) {
			String key = entry.getKey();

			autocompletado.add(key);
		}
		TextFields.bindAutoCompletion(CampoTexto, autocompletado);

		// Botones
		botonBuscar.setOnMouseClicked((event) -> refrescar());
		botonAcercaDe.setOnMouseClicked((event) -> mostrarVentana("/vista/AcercaDe.fxml", "Acerca de la aplicación", true));
		// Campo de texto
		CampoTexto.setOnKeyPressed((event) -> {
			if (event.getCode() == KeyCode.ENTER) {
				refrescar();
			}
		});
	}

	private void refrescar() {

		try {
			this.app = new ElTiempo(this.CampoTexto.getText());

			if (!app.semana.isEmpty()) {

				this.NombreMunicipio.setText(CampoTexto.getText().toUpperCase());
				this.NombreMunicipio.setVisible(true);
				this.dia_1Controller.refrescarDatos(app.semana.get(0));
				this.dia_2Controller.refrescarDatos(app.semana.get(1));
				this.dia_3Controller.refrescarDatos(app.semana.get(2));
				this.dia_4Controller.refrescarDatos(app.semana.get(3));
				this.dia_5Controller.refrescarDatos(app.semana.get(4));
				this.dia_6Controller.refrescarDatos(app.semana.get(5));
				this.dia_7Controller.refrescarDatos(app.semana.get(6));
			}

		} catch (MunicipioNoExisteException e) {

			// Sacar error de MunicipioNoExiste
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error al buscar el municipio");
			alert.setHeaderText(e.error());
			alert.showAndWait();
		}
		catch (DescargaFallidaException e) {
			// Sacar error de DescargaFallida
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error al descargar la información");
			alert.setHeaderText(e.error());
			alert.showAndWait();
		}
	}

	private void actualizarSiguienteComboBox(String nombre, ComboBox<String> combo, TreeMap<String, String> mapa) {

		ObservableList<String> listaFiltrada = FXCollections.observableArrayList(filtrarProvinciasYMunicipios(nombre, mapa));

		// Asignar la lista filtrada al comboBox
		combo.setItems(listaFiltrada);

		// Limpiar la selección del comboBox
		combo.getSelectionModel().clearSelection();
	}

	private ArrayList<String> filtrarProvinciasYMunicipios(String nombre, TreeMap<String, String> map) {

		ArrayList<String> filtrado = new ArrayList<String>();

		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String val = entry.getValue();

			if (val.equals(nombre)) {
				// Añadir la provincia / el municipio al ArrayList filtrado
				filtrado.add(key);
			}
		}
		return filtrado;
	}

	private String[] obtenerNombres(TreeMap<String, String> map) {

		int cont = 0;
		int tamanyo = map.size();
		String[] lista = new String[tamanyo];

		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();

			// Insertar el nombre obtenido en la posición del vector correspondiente
			lista[cont] = key;
			cont++;
		}

		return lista;
	}

	private void ocultarDias() {

		this.NombreMunicipio.setVisible(false);
		this.dia_1Controller.mostrarDatos(false);
		this.dia_2Controller.mostrarDatos(false);
		this.dia_3Controller.mostrarDatos(false);
		this.dia_4Controller.mostrarDatos(false);
		this.dia_5Controller.mostrarDatos(false);
		this.dia_6Controller.mostrarDatos(false);
		this.dia_7Controller.mostrarDatos(false);
	}
	
	//Mostrar otra ventana
		private void mostrarVentana(String rutaFXML, String titulo, boolean modal) {

			String icon = "/vista/icon/aemet-logo.png";
			try{
				//Léeme el source del archivo que te digo fxml y te pongo el path
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(rutaFXML));
				Parent root = (Parent) fxmlLoader.load();

				//Creame un nuevo Stage (una nueva ventana vacía)
				Stage stage = new Stage();

				//Asignar al Stage la escena que anteriormente hemos leído y guardado en root
				stage.setTitle(titulo);
				stage.setResizable(false);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image(getClass().getResource(icon).toExternalForm()));

				//Mostrar el Stage (ventana)
				stage.show();
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
}
