package controlador;

import java.time.LocalTime;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import modelo.Dia;

public class diaController {
	// Fondo
	@FXML private AnchorPane Fondo;
	// Datos varios
	@FXML private Label Fecha;
	@FXML private Label EstadoCielo;
	@FXML private Label ProbabilidadPrecipitacion;
	@FXML private Label CotaNieve;
	// Temperatura
	@FXML private Label TempActual;
	@FXML private Label TempMax;
	@FXML private Label TempMin;
	// Sens Térmica
	@FXML private Label SensTermicaMax;
	@FXML private Label SensTermicaMin;
	// Humedad
	@FXML private Label HumedadMax;
	@FXML private Label HumedadMin;
	@FXML private Label UVmax;
	@FXML private Label DireccionViento;
	@FXML private Label VelocidadViento;
	// Imágenes
	@FXML private ImageView EstadoImg;
	@FXML private ImageView dirVientoImg;
	@FXML private ImageView imgTermometro;
	@FXML private ImageView imgLluvia;
	@FXML private ImageView imgHumedad;
	@FXML private ImageView imgUv;
	@FXML private ImageView imgSensTermica;
	@FXML private ImageView imgCotaNieve;

	public void refrescarDatos(Dia dia) {

		String categoria = "";
		
		Fecha.setText(dia.getFecha());		
		EstadoCielo.setText(dia.getEstadoCielo());

		ProbabilidadPrecipitacion.setText(dia.getProbPrecipitacion() + "%");
		
		// Indice UV
		if (dia.getUvMax() <= 0) {
			categoria = " - Sin datos";
		}
		else if (dia.getUvMax() <= 2) {
			categoria = " - Bajo";
		}
		else if (dia.getUvMax() >= 3 && dia.getUvMax() <= 5) {
			categoria = " - Moderado";
		}
		else if (dia.getUvMax() >= 6 && dia.getUvMax() <= 7) {
			categoria = " - Alto";
		}
		else if (dia.getUvMax() >= 8 && dia.getUvMax() <= 10) {
			categoria = " - Muy alto";
		}
		else if (dia.getUvMax() >= 11) {
			categoria = " - Extremadamente alto";
		}
		
		UVmax.setText(dia.getUvMax() + categoria);
		CotaNieve.setText(dia.getCotaNieve() + "");
		
		HumedadMax.setText(dia.getHumedad().getMaxima() + "%");
		HumedadMin.setText(dia.getHumedad().getMinima() + "%");

		TempActual.setText(dia.getTemperatura().getMedia() + "º");
		TempMax.setText(dia.getTemperatura().getMaxima() + "º");
		TempMin.setText(dia.getTemperatura().getMinima() + "º");
		
		SensTermicaMax.setText(dia.getSensTermica().getMaxima() + "º");
		SensTermicaMin.setText(dia.getSensTermica().getMinima() + "º");

		VelocidadViento.setText(dia.getViento().getVelocidad() + "km/h");
		DireccionViento.setText(dia.getViento().getDireccion());

		// Cargar Imágenes
		try {
			LocalTime horaActual = LocalTime.now();
			String estadoCielo;
			// Cambiar imagenes según si es dia o noche (noche = de 21 a 6)
			boolean esNoche = horaActual.getHour() >= 21 || horaActual.getHour() < 6;
			
			// EstadoCielo
			estadoCielo = esNoche ? dia.getEstadoCielo() + " noche" : dia.getEstadoCielo();

			// Si la imagen no tiene versión de noche
			try {
				EstadoImg.setImage(cargarImagen(estadoCielo));

			} catch (NullPointerException e) {

				EstadoImg.setImage(cargarImagen(dia.getEstadoCielo()));
			}

			// VientoDireccion
			String vientoDir = dia.getViento().getDireccion();
			switch (vientoDir) {
			case "E" : dirVientoImg.setImage(cargarImagen("Del este (" 	   + vientoDir + ")"));
			case "NE": dirVientoImg.setImage(cargarImagen("Del nordeste (" + vientoDir + ")"));
			case "N" : dirVientoImg.setImage(cargarImagen("Del norte ("    + vientoDir + ")"));
			case "NO": dirVientoImg.setImage(cargarImagen("Del noroeste (" + vientoDir + ")"));
			case "O" : dirVientoImg.setImage(cargarImagen("Del oeste ("    + vientoDir + ")"));
			case "SO": dirVientoImg.setImage(cargarImagen("Del suroeste (" + vientoDir + ")"));
			case "S" : dirVientoImg.setImage(cargarImagen("Del sur (" 	   + vientoDir + ")"));
			case "SE": dirVientoImg.setImage(cargarImagen("Del sudeste ("  + vientoDir + ")"));
			case "C" : dirVientoImg.setImage(cargarImagen("En calma"));
			}

		} catch (NullPointerException e) {
			// nanai cargar imagenes si no se puede
		}

		mostrarDatos(true);
	}

	private Image cargarImagen(String valor) {

		Image imagen = new Image(getClass().getResourceAsStream("/vista/img/" + valor + ".png"));

		return imagen;
	}

	public void mostrarDatos(boolean activo) {

		this.Fondo.setVisible(activo);
		this.Fecha.setVisible(activo);
		this.EstadoCielo.setVisible(activo);
		this.ProbabilidadPrecipitacion.setVisible(activo);
		this.CotaNieve.setVisible(activo);
		this.TempActual.setVisible(activo);
		this.TempMax.setVisible(activo);
		this.TempMin.setVisible(activo);
		this.SensTermicaMax.setVisible(activo);
		this.SensTermicaMin.setVisible(activo);
		this.UVmax.setVisible(activo);
		this.HumedadMax.setVisible(activo);
		this.HumedadMin.setVisible(activo);
		this.DireccionViento.setVisible(activo);
		this.VelocidadViento.setVisible(activo);
		this.imgTermometro.setVisible(activo);
		this.imgHumedad.setVisible(activo);
		this.imgLluvia.setVisible(activo);
		this.imgUv.setVisible(activo);
		this.imgSensTermica.setVisible(activo);
		this.imgCotaNieve.setVisible(activo);
	}
}
