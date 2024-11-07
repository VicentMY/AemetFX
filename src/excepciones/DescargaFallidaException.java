package excepciones;

public class DescargaFallidaException extends Exception {

	private static final long serialVersionUID = -8423857635284744837L;

	public String error() {
		
		return "Ha ocurrido un fallo al descargar la información.";
	}
}
