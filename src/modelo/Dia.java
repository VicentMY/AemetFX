package modelo;

public class Dia {

	// Atributos
	private String fecha;
	private String estadoCielo;
	private int probPrecipitacion;
	private int cotaNieve;	
	private int uvMax;
	private Datos temperatura;
	private Datos sensTermica;
	private Datos humedad;
	private Viento viento;
	
	
	// Constructor
	public Dia(String fecha, int probPrecipitacion, int cotaNieve, String estadoCielo, int uvMax,
			Datos temperatura, Datos sensTermica, Datos humedad, Viento viento) {
		
		this.fecha = fecha;
		this.probPrecipitacion = probPrecipitacion;
		this.cotaNieve = cotaNieve;
		this.estadoCielo = estadoCielo;
		this.uvMax = uvMax;
		this.temperatura = temperatura;
		this.sensTermica = sensTermica;
		this.humedad = humedad;
		this.viento = viento;
	}
	
	// Getters
	public String getFecha() {
		return fecha;
	}

	public int getProbPrecipitacion() {
		return probPrecipitacion;
	}

	public int getCotaNieve() {
		return cotaNieve;
	}

	public String getEstadoCielo() {
		return estadoCielo;
	}

	public int getUvMax() {
		return uvMax;
	}

	public Datos getTemperatura() {
		return temperatura;
	}

	public Datos getSensTermica() {
		return sensTermica;
	}

	public Datos getHumedad() {
		return humedad;
	}
	
	public Viento getViento() {
		return viento;
	}
	
	// Metodos sobreescritos
	@Override
	public String toString() {

		return String.format("fecha               -> %s %n"
						   + "estadoCielo         -> %s %n"
						   + "probPrecipitaciones -> %d %n"
						   + "cotaNieve           -> %d %n"
						   + "uvMax               -> %d %n"
						   + "temperatura            %s %n"
						   + "sensTermica            %s %n"
						   + "humedad                %s %n"
						   + "viento                 %s %n",
						   this.fecha, this.estadoCielo, this.probPrecipitacion, this.cotaNieve, this.uvMax,
						   this.temperatura, this.sensTermica, this.humedad, this.viento);
	}
	
	
}
