package modelo;

public class Viento {

	// Atributos
	private String direccion;
	private int velocidad;
	
	// Constructor
	public Viento(String direccion, int velocidad) {
		
		this.direccion = direccion;
		this.velocidad = velocidad;
	}

	// Getters
	public String getDireccion() {
		return direccion;
	}

	public int getVelocidad() {
		return velocidad;
	}

	// Métodos Sobreescritos
	@Override
	public String toString() {
//		return "Viento [direccion=" + direccion + ", velocidad=" + velocidad + "]";
		return String.format("%ndireccion -> %s %n"
						   + "velocidad -> %d %n",
						   this.direccion, this.velocidad);
	}
}
