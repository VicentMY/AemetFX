package utiles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import excepciones.DescargaFallidaException;

public class Ficheros {

	// Atributos
	public TreeMap<String, String> mapaMunicipios;
	public TreeMap<String, String> mapaComunidades;
	public TreeMap<String, String> mapaProvincias;
	public TreeMap<String, String> mapaProvinciasComunidades;
	public TreeMap<String, String> mapaMunicipiosProvincias;

	// Constructor
	public Ficheros() {

		leerFicheroMunicipios();
		leerFicheroComunidadesProvincias();
	}

	// Métodos
	private String[] arreglarNombres(String[] v) {

		String[] vInvertir;

		// Arreglar el fallo de lectura causado por la mania del INE de invertir los nombres
		for (int i = 0; i < v.length; i++) {

			if (v[i].contains(", ")) {

				vInvertir = v[i].split(", ");
				v[i] = vInvertir[1] + " " + vInvertir[0];
			}
		}
		return v;
	}

	public void leerFicheroMunicipios() {

		TreeMap<String, String> map = new TreeMap<String, String>();

		File diccionario = new File("ficheros/diccionario24.csv");

		try {
			Scanner sc = new Scanner(diccionario);
			String[] v;
			String municipio;
			String codigo;

			sc.nextLine();
			sc.nextLine();

			while (sc.hasNext()) {
				// EJEMPLO
				// CODAUTO;CPRO;CMUN;DC;NOMBRE
				// 10,46,029,1,Algemesí
				// Municipio = v[4]
				// Código = v[1] + v[2]

				v = sc.nextLine().split(";");

				arreglarNombres(v);

				municipio = v[4];
				codigo = v[1] + v[2];

				map.put(municipio, codigo);
			}

			sc.close();

		} catch (FileNotFoundException e) {

			System.err.println("No se ha podido leer el fichero " + diccionario);
		}

		this.mapaMunicipios = map;
	}

	public void leerFicheroComunidadesProvincias() {

		File fichero = new File("ficheros/comunProvinCodigos.csv");

		TreeMap<String, String> mapaComunidad = new TreeMap<String, String>();
		TreeMap<String, String> mapaProvincia = new TreeMap<String, String>();

		TreeMap<String, String> mapaProvinciaComunidad = new TreeMap<String, String>();
		TreeMap<String, String> mapaMunicipioProvincia = new TreeMap<String, String>();

		try {
			Scanner sc = new Scanner(fichero);
			String[] v;
			String comunidad;
			String codComunidad;
			String provincia;
			String codProvincia;

			sc.nextLine();

			while (sc.hasNext()) {
				// EJEMPLO
				// CODAUTO,Comunidad Autónoma,CPRO,Provincia
				// 1,Andalucía,4,Almería

				v = sc.nextLine().split(";");

				arreglarNombres(v);

				codComunidad = v[0];
				comunidad = v[1];
				codProvincia = v[2];
				provincia = v[3];

				mapaComunidad.put(comunidad, codComunidad);
				mapaProvincia.put(provincia, codProvincia);

				mapaProvinciaComunidad.put(provincia, comunidad);
				
				for (Map.Entry<String, String> entry : this.mapaMunicipios.entrySet()) {
					String key = entry.getKey();
					String val = entry.getValue();
					
					if (val.substring(0,2).equals(codProvincia.length() == 1 ? "0" + codProvincia : codProvincia)) {
						
						mapaMunicipioProvincia.put(key, provincia);
					}
				}

			}

			sc.close();

		} catch (FileNotFoundException e) {

			System.err.println("No se ha podido leer el fichero " + fichero);
		}

		this.mapaComunidades = mapaComunidad;
		this.mapaProvincias = mapaProvincia;
		this.mapaProvinciasComunidades = mapaProvinciaComunidad;
		this.mapaMunicipiosProvincias = mapaMunicipioProvincia;

	}

	public static void descargarFicheroXML(String codigo) throws DescargaFallidaException {

		try {
			// URL donde se encuentra el fichero xml
			URL url = new URL("https://www.aemet.es/xml/municipios/localidad_" + codigo + ".xml");

			Scanner sc = new Scanner(url.openStream());

			// Crear el fichero local
			String fichero = "ficheros/localidad.xml";
			PrintWriter pw = new PrintWriter(new File(fichero));

			String linea;

			while (sc.hasNext()) {

				linea = sc.nextLine();

				pw.println(linea);
			}

			pw.close();

		} catch (IOException e) {

			throw new DescargaFallidaException();
		}
	}

	public static void borrarCache() {

		File archivo = new File("ficheros/localidad.xml");

		archivo.delete();
	}

}