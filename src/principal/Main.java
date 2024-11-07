package principal;
	
import javafx.application.Application;
import javafx.stage.Stage;
import utiles.Ficheros;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		
		String fxml = "/vista/principal.fxml";
		String css = "/vista/css/principal.css";
		String icon = "/vista/icon/aemet-logo.png";
		String title = "AEMET - Previsión del clima";
		
		try {
			// Cargar fxml y crear escena
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource(fxml));
			Scene scene = new Scene(root);
			
			// Cargar estilo, titulo y icono
			scene.getStylesheets().add(getClass().getResource(css).toExternalForm());
			primaryStage.setTitle(title);
			primaryStage.getIcons().add(new Image(getClass().getResource(icon).toExternalForm()));
			
			// Cargar escena
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop() {
		// Borrar ficheros al salir de la aplicación
		borrarCache();
	}
	
	private void borrarCache() {

		Ficheros.borrarCache();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
