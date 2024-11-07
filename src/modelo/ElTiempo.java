package modelo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import excepciones.DescargaFallidaException;
import excepciones.MunicipioNoExisteException;
import utiles.Ficheros;

public class ElTiempo {

	// Atributos
	private Dia dia;
	private String codigoMunicipio;
	public ArrayList<Dia> semana;

	// DEBUG MAIN
//	public static void main(String[] args) throws MunicipioNoExisteException {
//
//		ElTiempo tiempo = new ElTiempo("Algemesí");
//
//		for (int i = 0; i < tiempo.semana.size(); i++) {
//
//			Dia dia = tiempo.semana.get(i);
//
//			System.out.println(dia);
//		}
//	}

	// Constructores

	public ElTiempo(String municipio) throws MunicipioNoExisteException, DescargaFallidaException {

		this.semana = new ArrayList<Dia>();

		Ficheros mapas = new Ficheros();

		this.codigoMunicipio = mapas.mapaMunicipios.get(municipio);
		
		if (mapas.mapaMunicipios.get(municipio) != null) {
			// Si el municipio es correcto
			Ficheros.descargarFicheroXML(this.codigoMunicipio);
			LeerFicheroXML("ficheros/localidad.xml");
		}
		else {
			// Si el municipio no es correcto
			throw new MunicipioNoExisteException();
		}
	}

	// Getters
	public Dia getDia() {
		return dia;
	}

	// Métodos
	public void LeerFicheroXML(String ruta) {

		try {
			// Java DOM
			Document doc;
			NodeList listaDias;
			Node nodoDia;
			Element elementoDia;
			NodeList listaCielo;

			// Clásicas
			String fecha;
			String estadoCielo;
			int probPrecipitacion;
			int cotaNieve;
			int uvMax;

			// Objetos
			LocalDate lDate;
			Viento viento;
			Temperatura temp;
			SensTermica sterm;
			Humedad humd;


			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			// Importar el XML
			doc = dBuilder.parse(ruta);

			// Normalizar el XML
			doc.getDocumentElement().normalize();

			// Obtener dias
			listaDias = doc.getElementsByTagName("dia");

			for (int i = 0; i < listaDias.getLength(); i++) {

				// Obtener fecha
				nodoDia = listaDias.item(i);
				elementoDia = (Element) nodoDia;

				lDate = LocalDate.parse(elementoDia.getAttribute("fecha"));

				fecha = lDate.format(DateTimeFormatter.ofPattern("EEEE',' dd 'de' MMMM"));

				// Obtener probabilidad de precipitación
				probPrecipitacion = obtenerInfoNodo("prob_precipitacion", elementoDia);

				// Obtener Cota nieve probable
				cotaNieve = obtenerInfoNodo("cota_nieve_prov", elementoDia);

				// Obtener estado cielo
				listaCielo = elementoDia.getElementsByTagName("estado_cielo");

				estadoCielo = sacarMediaDatosString(listaCielo, "descripcion");

				// Obtener UV
				uvMax = obtenerInfoNodo("uv_max", elementoDia);

				// Obtener viento
				viento = (Viento) obtenerInfoViento(elementoDia);

				// Obtener temperatura, sensación térmica y humedad
				temp = (Temperatura) obtenerInfoObjeto("temperatura", elementoDia);
				sterm = (SensTermica) obtenerInfoObjeto("sens_termica", elementoDia);
				humd = (Humedad) obtenerInfoObjeto("humedad_relativa", elementoDia);

				fecha = fecha.substring(0, 1).toUpperCase() + fecha.substring(1);

				// Crear el objeto Dia
				this.dia = new Dia(fecha, probPrecipitacion, cotaNieve, estadoCielo, uvMax, temp, sterm, humd, viento);

				// Añadir el objeto Dia a la lista de dias
				this.semana.add(this.dia);
			}

		} catch (IOException | SAXException | ParserConfigurationException e) {

			e.printStackTrace();
		}
	}

	private String sacarMediaDatosString(NodeList listaNodos, String atributo) {

		HashMap<String, Integer> mediaDatos = new HashMap<String, Integer>();

		String res = "";
		int max = 0;

		Node nodo;
		Element elemento;

		for (int j = 0; j < listaNodos.getLength(); j++) {

			nodo = listaNodos.item(j);
			elemento = (Element) nodo;

			res = elemento.getAttribute(atributo);

			if (!res.equals("")) {
				if (mediaDatos.getOrDefault(res, -1) == -1) {

					mediaDatos.put(res, 1);
				}
				else {

					mediaDatos.put(res, mediaDatos.get(res) +1);
				}
			}
		}

		// Obtener el valor que más aparece como media
		for (Map.Entry<String, Integer> entry : mediaDatos.entrySet()) {
			int val = entry.getValue();

			max = Math.max(max, val);
		}
		for (Map.Entry<String, Integer> entry : mediaDatos.entrySet()) {
			String key = entry.getKey();

			if (mediaDatos.get(key) == max) {

				res = key;
			}
		}

		mediaDatos.clear();
		max = 0;

		return res.trim();
	}

	private Object obtenerInfoViento(Element elementoDia) {

		Object objeto = new Object();
		NodeList listaViento;
		Node nodoViento;
		Element elementoViento;
		HashMap<String, Integer> vientos = new HashMap<String, Integer>();

		String direccion = "";
		String velocidad = "";
		int max = 0;

		listaViento = elementoDia.getElementsByTagName("viento");

		for (int i = 0; i < listaViento.getLength(); i++) {

			nodoViento = listaViento.item(i);
			elementoViento = (Element) nodoViento;

			direccion = elementoViento.getElementsByTagName("direccion").item(0).getTextContent();
			velocidad = elementoViento.getElementsByTagName("velocidad").item(0).getTextContent();

			vientos.put(direccion, Integer.parseInt(velocidad.equals("") ? "0" : velocidad));

			max = 0;

			// Conseguir la velocidad que más aparece
			for (Map.Entry<String, Integer> entry : vientos.entrySet()) {
				Integer val = entry.getValue();

				max = Math.max(max, val);
			}
			// Conseguir la dirección asociada
			for (Map.Entry<String, Integer> entry : vientos.entrySet()) {
				String key = entry.getKey();
				Integer val = entry.getValue();

				if (vientos.get(key) == max) {

					direccion = key;
					velocidad = val + "";
				}
			}
		}

		// Crear el objeto de tipo Viento
		objeto = new Viento(direccion, Integer.parseInt(velocidad));

		return objeto;
	}

	private Object obtenerInfoObjeto(String nombre, Element elementoDia) {

		Object objeto = new Object();

		NodeList listaNodos;
		Node nodo;
		Element elemento;

		// Máxima y mínima
		nodo = elementoDia.getElementsByTagName(nombre).item(0);
		elemento = (Element) nodo;

		int max = Integer.parseInt(elemento.getElementsByTagName("maxima").item(0).getTextContent());
		int min = Integer.parseInt(elemento.getElementsByTagName("minima").item(0).getTextContent());

		// Resto de datos
		listaNodos = elemento.getElementsByTagName("dato");
		int[] datos;

		if (listaNodos.getLength() != 0) {

			datos = new int[listaNodos.getLength()];

			for (int i = 0; i < listaNodos.getLength(); i++) {

				nodo = listaNodos.item(i);
				elemento = (Element) nodo;

				datos[i] = Integer.parseInt(elemento.getTextContent().equals("") ? "0" : elemento.getTextContent());
			}
		}
		else {

			datos = new int[2];
			datos[0] = max;
			datos[1] = min;
		}

		// Crear el objeto
		if (nombre.equals("temperatura")) {

			objeto = new Temperatura(max, min, datos);
		}
		else if (nombre.equals("sens_termica")) {

			objeto = new SensTermica(max, min, datos);
		}
		else if (nombre.equals("humedad_relativa")) {

			objeto = new Humedad(max, min, datos);
		}

		return objeto;
	}

	private int obtenerInfoNodo(String nombre, Element elementoDia) {

		NodeList listaNodos;
		Node nodo;
		Element elemento;

		int media = 0;

		listaNodos = elementoDia.getElementsByTagName(nombre);

		if (listaNodos.getLength() != 0) {

			for (int i = 0; i < listaNodos.getLength(); i++) {

				nodo = listaNodos.item(i);

				elemento = (Element) nodo;

				media += Integer.parseInt(elemento.getTextContent().equals("") ? "0" : elemento.getTextContent());
			}

			media /= listaNodos.getLength();
		}
		else {
			nodo = listaNodos.item(0);

			elemento = (Element) nodo;

			if (nodo == null) {

				media = 0;
			}
			else {

				media = Integer.parseInt(elemento.getTextContent().equals("") ? "0" : elemento.getTextContent());
			}
		}

		return media;
	}
}
