package principal;

import java.awt.Menu;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;

import javax.security.auth.login.LoginContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.quimnv.modelo.Perfiles;
import com.quimnv.modelo.Sesion;

import utils.Utilidades;
import utils.Utilidades.*;

/**
 * Circo
 * 
 * @author Quim Navarro Vaquero
 * @version 1.0
 */
public class Main {

	private static Scanner leer = new Scanner(System.in);

	public static void main(String[] args) {

		Sesion actual = new Sesion("invitado", Perfiles.INVITADO);
		boolean confirmarsalir = false;
		int opcion = -1;

		do {
			System.out.println("\n=== Programa de gestión del circo ===\n-------------------------------------");
			System.out.println("Hola "+actual.getNombre()+". Su perfil es de tipo: "+actual.getPerfil());
			opcion = seleccionarOpcion(actual);
			switch (opcion) {
			case 0:
				if (actual.getPerfil().equals(Perfiles.INVITADO)){
					System.out.println("Ha elegido SALIR. ¿Está seguro?");
					confirmarsalir = Utilidades.leerBoolean();
					if(confirmarsalir) {
						System.out.println("¡Hasta la próxima!");
					}
					break;
				} else {
					System.out.println("Ha elegido CERRAR SESIÓN. ¿Está seguro?");
					confirmarsalir = Utilidades.leerBoolean();
					if (confirmarsalir) {
						actual = new Sesion("invitado", Perfiles.INVITADO);
						confirmarsalir = false;
						break;
					}
				}
				break;
			case 1: 
			default:
				actual = submenuSesion(actual, opcion);
			}
		} while (!confirmarsalir);

	}

	private static Sesion submenuSesion(Sesion actual, int opcion) {
		Sesion ret = null;
		switch (actual.getPerfil()) {
		case INVITADO:
			switch (opcion) {
			case 1:
				System.out.println("Ver espectáculos del circo:");
				break;
			case 2:
				System.out.println("\n--- Entrando en el menú de acceso al usuario\n");
				ret = Login();
				break;
			default:
				break;
			}
			break;
		case ADMIN:
			switch (opcion) {
			case 1:
				System.out.println("\n--- Entrando en el menú de gestión de personas y credenciales...\n");
				menuPersonas();
				ret = actual;
				break;

			default:
				break;
			}
		default:
			break;
		}

		return ret;
	}

	/**
	 * Método de login con la entrada de datos del usuario y la comprobación de sus datos
	 * 
	 * @return Sesion del Perfil y Nombre facilitados
	 */
	private static Sesion Login() {
		Sesion ret = new Sesion();
		System.out.println("=== Bienvenido al menú de acceso ===\n-------------------------------------");
		boolean valido=false;
		do {
			System.out.print("Por favor, introduce tu usuario:");
			String user = Utilidades.leerString();
			System.out.print("Por favor, introduce tu contraseña:");
			String contrassenya = Utilidades.leerString();
			System.out.println("contraseña introducida:"+contrassenya);
			Properties propiedades = new Properties();
			try (FileInputStream entrada = new FileInputStream("src/main/resources/application.properties")){
				propiedades.load(entrada);
			} catch (IOException e) {
				System.err.println("Error de Excepción de tipo IOException al cargar el fichero ");
				e.printStackTrace();
			}

			// Comprobamos los datos del admin primero
			String usuarioAdmin = propiedades.getProperty("usuarioAdmin");
			String contrassenyaAdmin = propiedades.getProperty("passwordAdmin");


			if (user.equals(usuarioAdmin) && contrassenya.equals(contrassenyaAdmin)) {
				ret.setNombre(usuarioAdmin);
				ret.setPerfil(Perfiles.ADMIN);
				System.out.println("\n--- ¡Bienvenido! Se ha autenticado usted como administrador. ---\n");

				valido = true;
				break;  // Salimos del bucle si las credenciales son correctas
			}

			// Comprobamos el archivo de credenciales
			String ruta = propiedades.getProperty("ficherocredenciales");
			FileReader lector = null;
			File fichero = new File(ruta);
			BufferedReader br = null;

			try {
				lector = new FileReader(fichero);
				br = new BufferedReader(lector);
				String linea;

				while ((linea = br.readLine()) != null) {
					String[] campos = linea.split("\\|");
					if (campos.length < 7) {
						continue;//Saltamos las líneas si contienen menos campos de los que deberían
					}

					String nombreUsuario = campos[1]; // Índice 1: nombre_usuario
					String password = campos[2];     // Índice 2: password
					String perfilLogin = campos[6].toUpperCase();

					// El fichero credenciales.txt sigue esta plantilla para cada línea:
					//  idpersonal|nombre_usuario|password|email|nombre_persona
					if(user.equals(nombreUsuario) && contrassenya.equals(password)) {
						ret.setNombre(nombreUsuario);
						ret.setPerfil(Perfiles.valueOf(perfilLogin));
						System.out.println("\n--- ¡Bienvenido! Se ha autenticado usted como" +perfilLogin+ ". ---\n");
						valido = true;
						break;
					}

				}
			} catch (Exception e) {
				System.err.println("Error al leer el archivo de credenciales: " + e.getMessage());
				e.printStackTrace();
			}
			System.out.println("Los datos introducidos no pertenecen a ningún usuario, inténtelo de nuevo.");

		} while (!valido);

		return ret;

	}

	/**
	 * Método para gestionar los menús principales de cada tipo de usuario
	 * 
	 * @param actual
	 * @return un int que luego se gestiona desde el método submenuSesion de entrada para entrar en los submenús
	 */
	private static int seleccionarOpcion(Sesion actual) {
		// TODO Auto-generated method stub
		int seleccion=-1;
		do {
			switch (actual.getPerfil()) {
			case INVITADO:{
				System.out.println("\nIntroduzca el número de la opción deseada:");
				System.out.println("\t1.- Ver espectáculos");
				System.out.println("\t2.- Iniciar sesión.");
				System.out.println("\t0.- Salir");
				seleccion = Utilidades.leerEntero();
				if (seleccion < 0 || seleccion > 2) {
					System.out.println("Opción no disponible. Por favor seleccione una opción.");
					seleccion = -1;
				}
			}
			break;
			case ARTISTA: {
				System.out.println("\n=== Menú de Artista en construcción ===\n-------------------------------------\nIntroduzca el número de la opción deseada:");
				System.out.println("\t1.- Gestionar personas y credenciales.");
				System.out.println("\t2.- Gestionar espectáculos.");
				System.out.println("\t0.- Salir");
			}
			break;
			case COORDINADOR: {
				System.out.println("\n=== Menú de Coordinador en construcción ===\n-------------------------------------\ndIntroduzca el número de la opción deseada:");
			}
			break;
			case ADMIN: {
				System.out.println("\n=== Menú de administración ===\n------------------------------\"\nIntroduzca el número de la opción deseada:");
				System.out.println("\t1.- Gestión de personas y credenciales.");
				System.out.println("\t2.- Gestión de espectáculos.");
				System.out.println("\t0.- Salir.");
				int opcion = Utilidades.leerEntero();
				if (opcion < 0 || opcion > 2) {
					System.out.println("Opción no disponible. Por favor seleccione una opción.");
					opcion = -1;
				}
				seleccion = opcion;
			}
			break;
			default:
				break;
			}
		} while (seleccion == -1);



		return seleccion;
	}


	/**
	 * Menú de gestión de personas que vuelve a la 
	 */
	public static void menuPersonas() {
		boolean valido = false;
		do {
			System.out.println("\n-----------------------------------------------------------\n=== Bienvenido a la gestión de personas y credenciales. ===\n-----------------------------------------------------------\"\nIntroduzca el número de la opción deseada:");
			System.out.println("\t1.- Registrar persona");
			System.out.println("\t2.- Asignar perfil y credenciales");
			System.out.println("\t0.- Salir.");
			int opcion = Utilidades.leerEntero();
			if (opcion <0 || opcion >2) {
				System.err.println("\nOpción no disponible o no válida, introduzca de nuevo su opción.\n");
				valido = false;
				break;
			}
			switch (opcion) {
			case 0:
				System.out.println("\n--- Volviendo al menú de administración.");
				valido=true;
				break;
			case 1:
				registrarPersona();
				break;


			}
		} while (!valido);
	}

	public static void registrarPersona() {
		
		System.out.println("-------------------------\n=== Registrar Persona===\n-------------------------");
		boolean valido = false;
		do {
			System.out.print("----------------------\n--- Datos de Persona---\n----------------------\nIntroduce el nombre completo:");
			String nombre = Utilidades.leerString();
			System.out.print("Seleccione su nacionalidad introduciendo el ID de país (por ejemplo ES para España):");

			// Cargamos desde propiedades el archivo
			Properties propiedades = new Properties();
			try (FileInputStream entrada = new FileInputStream("src/main/resources/application.properties")){
				propiedades.load(entrada);
			} catch (IOException e) {
				System.err.println("Error de Excepción de tipo IOException al cargar el fichero ");
				e.printStackTrace();
			}
			String ficheropaises = propiedades.getProperty("ficherosnacionalidades");
			File paisesArchivo = new File(ficheropaises);
			Map<String, String> paises = new HashMap<String, String>();

			try {
				//Creo una factoria que permita usar un parser:
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				//Crea un builder que permite crear documentos DOM usando un parser:
				Document documento = builder.parse(paisesArchivo);
				//Los nodos de texto adyacentes los fusiona
				documento.getDocumentElement().normalize();
				System.out.println("Elemento raiz:"
						+documento.getDocumentElement().getNodeName());
				//Crea una lista con todos los nodos cliente:
				NodeList paisesList = documento.getElementsByTagName("pais");
				//Recorro la lista:
				for (int i = 0; i < paisesList.getLength(); i++) {
					Node paisNodo = paisesList.item(i);
					if(paisNodo.getNodeType() == Node.ELEMENT_NODE){
						Element elemento = (Element) paisNodo;//Obtenemos los elementos del nodo
						if(paisNodo.getNodeType() == Node.ELEMENT_NODE){
							String paisString = getNodo("id", elemento)+" "+getNodo("nombre", elemento);
							paises.put(getNodo("id", elemento), getNodo("nombre", elemento));
							System.out.println(paisString);
						}
					}
				}
			} catch (ParserConfigurationException | SAXException | IOException ex) {
				System.err.println("Error: "+ex.getMessage());
			}
			boolean nacionalidadvalida = false;
			do {
				System.out.print("Código: ");
				String id = Utilidades.leerString();;
				String nacionalidad = "";
				for (Entry<String, String> entry : paises.entrySet()) {
					if (id.equalsIgnoreCase(entry.getKey())) {
						nacionalidad = entry.getValue();
						System.out.println("Nacionalidad establecida como "+entry.getValue());
						nacionalidadvalida = true;
					}
				}
				if(!nacionalidadvalida) {
					System.err.println("Por favor, introduzca un código válido para nacionalidad. Inténtelo de nuevo.");
				}
			} while (!nacionalidadvalida);

			System.out.println();
			
			// Sección de coordinación o artista
			



			System.out.println("Introduce el nombre de usuario del nuevo registro\n(solo se aceptan minúsculas, sin tildes ni carácteres especiales):");
			String user = Utilidades.leerStringFormato();
			System.out.print("Introduce la contraseña del nuevo usuario:");
			String contrassenya = Utilidades.leerString();
			System.out.print("Introduce el correo eléctronico del usuario:");
			String correo = Utilidades.leerString();
			valido = comprobarRegistroExistente(valido, user, correo);


		} while (!valido);
	}

	private static String getNodo(String etiqueta, Element elem){
		NodeList nodo=elem.getElementsByTagName(etiqueta).item(0).getChildNodes();
		Node valorNodo = (Node) nodo.item(0);
		return valorNodo.getNodeValue();
	}

	private static boolean comprobarRegistroExistente(boolean valido,
			String user, String correo) {
		Properties propiedades = new Properties();
		try (FileInputStream entrada = new FileInputStream("src/main/resources/application.properties")){
			propiedades.load(entrada);
		} catch (IOException e) {
			System.err.println("Error de Excepción de tipo IOException al cargar el fichero ");
			e.printStackTrace();
		}
		// Comprobamos el archivo de credenciales
		String ruta = propiedades.getProperty("ficherocredenciales");
		FileReader lector = null;
		File fichero = new File(ruta);
		BufferedReader br = null;
		try {
			lector = new FileReader(fichero);
			br = new BufferedReader(lector);
			String linea;

			while ((linea = br.readLine()) != null) {
				String[] campos = linea.split("\\|");
				if (campos.length < 7) {
					continue;//Saltamos las líneas si contienen menos campos de los que deberían
				}

				String nombreUsuario = campos[1]; // Índice 1: nombre_usuario
				String correoUsuario = campos[3];     // Índice : Correo electrónico

				if(user.equals(nombreUsuario) || correo.equals(correoUsuario)) {
					System.err.println("Correo electrónico y/o usuario ya registrado. Por favor, introduzca un usuario nuevo.");
					valido = false;
					break;
				}

			}
		} catch (Exception e) {
			System.err.println("Error al leer el archivo de credenciales: " + e.getMessage());
			e.printStackTrace();
		}
		return valido;
	}



	/**
	 * Método para confirmar acciones que requieran de una revisión del usuario
	 * 
	 * @return el booleano esvalido para confirmar o negar la elección.
	 */

	public boolean esValido() {
		while (true) {
			System.out.println("¿Quiere confirmar?");
			System.out.println("SI/NO");
			String opcion = leer.nextLine();
			if (opcion.equalsIgnoreCase("si")) {
				return true;
			} else if (opcion.equalsIgnoreCase("no")) {
				return false;
			} else {
				System.out.println("Opción no válida. Inténtelo de nuevo.");
			}
		}
	}


}
