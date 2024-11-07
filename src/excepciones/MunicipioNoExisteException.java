package excepciones;

public class MunicipioNoExisteException extends Exception {
	
	private static final long serialVersionUID = -7168462879804232626L;

	public String error() {
		
		return "El municipio introducido no existe.";
	}
}
