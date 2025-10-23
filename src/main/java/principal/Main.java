package principal;

import java.awt.Menu;
import java.awt.event.KeyAdapter;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.PrivateKey;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import javax.security.auth.login.LoginContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.quimnv.modelo.Artista;
import com.quimnv.modelo.Coordinador;
import com.quimnv.modelo.Especialidad;
import com.quimnv.modelo.Espectaculo;
import com.quimnv.modelo.Numero;
import com.quimnv.modelo.Perfiles;
import com.quimnv.modelo.Persona;
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

	/**
	 * Método para gestionar las opciones tomadas en el submenú de cada sesión
	 * @param actual
	 * @param opcion
	 * @return Sesion, devuelve la sesión correspondiente tras loguear o navegar
	 */
	private static Sesion submenuSesion(Sesion actual, int opcion) {
		Sesion ret = null;
		switch (actual.getPerfil()) {
		case INVITADO:
			switch (opcion) {
			case 1:
				System.out.println("Ver espectáculos del circo:");
				verEspectaculos();
				ret = actual;
				break;
			case 2:
				System.out.println("\n--- Entrando en el menú de acceso al usuario\n");
				ret = Login();
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
			case 2:
				System.out.println("\n--- Entrando en el menú de gestión de espectáculos...\n");
				menuEspectaculos(actual);
				ret = actual;
				break;
			}
		case COORDINADOR:
			switch (opcion) {
			case 1:
				System.out.println("Ver espectáculos del circo:");
				verEspectaculos();
				ret = actual;
				break;
			case 2:
				System.out.println("\n--- Entrando en el menú de gestión de espectáculos...\n");
				menuEspectaculos(actual);
				ret=actual;
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

			// Comprobamos los datos del admin primero
			String usuarioAdmin = obtenerPropiedad("usuarioAdmin");
			String contrassenyaAdmin = obtenerPropiedad("passwordAdmin");

			if (user.equals(usuarioAdmin) && contrassenya.equals(contrassenyaAdmin)) {
				ret.setNombre(usuarioAdmin);
				ret.setPerfil(Perfiles.ADMIN);
				System.out.println("\n--- ¡Bienvenido! Se ha autenticado usted como administrador. ---\n");

				valido = true;
				break;  // Salimos del bucle si las credenciales son correctas
			}

			// Comprobamos el archivo de credenciales
			FileReader lector = null;
			File fichero = new File(obtenerPropiedad("ficherocredenciales"));
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
					String nom = campos[4];     // Índice 2: password
					String perfilLogin = campos[6].toUpperCase();

					// El fichero credenciales.txt sigue esta plantilla para cada línea:
					//  idpersonal|nombre_usuario|password|email|nombre_persona
					if(user.equals(nombreUsuario) && contrassenya.equals(password)) {
						ret.setNombre(nombreUsuario);
						ret.setPerfil(Perfiles.valueOf(perfilLogin));
						System.out.println("\n--- ¡Bienvenido "+nom+"! Se ha autenticado usted como" +perfilLogin+ ". ---\n");
						valido = true;
						break;
					} else {
						System.out.println("Los datos introducidos no pertenecen a ningún usuario, inténtelo de nuevo.");

					}

				}
			} catch (Exception e) {
				System.err.println("Error al leer el archivo de credenciales: " + e.getMessage());
				e.printStackTrace();
			}

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
				System.out.println("\n=== Menú de Artista ===\n-------------------------------------\nIntroduzca el número de la opción deseada:");
				System.out.println("\t1.- Ver su ficha de artista.");
				System.out.println("\t2.- Ver espectáculos.");
				System.out.println("\t0.- Salir");
				seleccion = Utilidades.leerEntero();
				if (seleccion < 0 || seleccion > 2) {
					System.out.println("Opción no disponible. Por favor seleccione una opción correcta.");
					seleccion = -1;
				}
			}
			break;
			case COORDINADOR: {
				System.out.println("\n=== Menú de Coordinador ===\n-------------------------------------\ndIntroduzca el número de la opción deseada:");
				System.out.println("\t1.- Ver espectáculos.");
				System.out.println("\t2.- Gestionar espectáculos");
				System.out.println("\t0.- Salir");
				seleccion = Utilidades.leerEntero();
				if (seleccion < 0 || seleccion > 2) {
					System.out.println("Opción no disponible. Por favor seleccione una opción correcta.");
					seleccion = -1;
				}
			}
			break;
			case ADMIN: {
				System.out.println("\n=== Menú de administración ===\n------------------------------\"\nIntroduzca el número de la opción deseada:");
				System.out.println("\t1.- Gestión de personas y credenciales.");
				System.out.println("\t2.- Gestión de espectáculos.");
				System.out.println("\t0.- Salir.");
				seleccion = Utilidades.leerEntero();
				if (seleccion < 0 || seleccion > 2) {
					System.out.println("Opción no disponible. Por favor seleccione una opción correcta.");
					seleccion = -1;
				}
			}
			break;
			}
		} while (seleccion == -1);



		return seleccion;
	}


	/**
	 * Menú de gestión de personas que vuelve al menú anterior al salir
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
				Persona registrado = registrarPersona();
				break;
			case 2: 
				System.out.println("Opción no implementada, disculpe las molestias.");
				break;
			}
		} while (!valido);
	}
	
	/**
	 * Menú de gestión de espectáculos, de momento solo permite verlos y crearlos.
	 * @param actual
	 */
	public static void menuEspectaculos(Sesion actual) {
		boolean valido = false;
		do {
			System.out.println("\n-----------------------------------------------------------\n=== Bienvenido a la gestión de espectáculos. ===\n-----------------------------------------------------------\"\nIntroduzca el número de la opción deseada:");
			System.out.println("\t1.- Ver espectáculos.");
			System.out.println("\t2.- Crear o modificar espectáculos.");
			System.out.println("\t3.- Asignar artistas (no disponible).");
			System.out.println("\t4.- Crear o modificar números (no disponible).");
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
				verEspectaculos();
				break;
			case 2:
				gestionarEspectaculo(actual);
				break;
			case 3: 
				System.out.println("Opción no implementada, disculpe las molestias.");
				break;
			case 4: 
				System.out.println("Opción no implementada, disculpe las molestias.");
				break;
			}
		} while (!valido);

	}

	/**
	 * Método para registrar una persona con los datos facilitados por el usuario
	 * @return Persona generada
	 */
	public static Persona registrarPersona() {
		String apodoReg = "";
		Set<Especialidad> especialidadesReg = new HashSet<>();
		Boolean seniorReg = false;
		LocalDate fechaSeniorReg = null;
		Persona ret = new Persona();

		System.out.println("-------------------------\n=== Registrar Persona===\n-------------------------");
		boolean valido = false;
		do {
			System.out.print("----------------------\n--- Datos de Persona---\n----------------------\nIntroduce el nombre completo:");
			String nombre = Utilidades.leerString();
			System.out.print("Seleccione su nacionalidad introduciendo el ID de país (por ejemplo ES para España):");


			String nacionalidad=seleccionarPais();



			// Sección de coordinación o artista
			System.out.println("Seleccione el tipo de persona:\n\t1.- Artista\n\t2. Coordinador");

			Perfiles perfilReg = null;
			do {
				int perfilOpcion = Utilidades.leerEntero();
				if (perfilOpcion < 0 || perfilOpcion > 2) {
					System.out.println("Opción no disponible, por favor introduzca una opción válida.");
				}
				switch (perfilOpcion) {
				case 1: 
					System.out.println("Ha seleccionado artista.");
					System.out.println("¿Tiene apodo? Introduzcalo a continuación o presione enter.");
					apodoReg = Utilidades.leerString();
					System.out.println("¿Cuales son sus especialidades?\nIntroduzca los números y presione 0 para salir.");
					especialidadesReg = new HashSet<>();
					Boolean especialidadesValido = false;
					do {
						System.out.println("1.- Acrobacia\n2.- Humor\n3.- Magia\n4.- Equilibrismo\n5.- Malabarismo\n0.- Terminar");
						int seleccion = Utilidades.leerEntero();
						switch (seleccion) {
						case 1: especialidadesReg.add(Especialidad.ACROBACIA);
						break;
						case 2: especialidadesReg.add(Especialidad.HUMOR);
						break;
						case 3: especialidadesReg.add(Especialidad.MAGIA);
						break;
						case 4: especialidadesReg.add(Especialidad.EQUILIBRISMO);
						break;
						case 5: especialidadesReg.add(Especialidad.MALABARISMO);
						break;
						case 0: if (especialidadesReg.isEmpty()) {
							System.out.println("Por favor, introduzca al menos una especialidad.");
						} else {
							System.out.println("Ha introducido las siguientes especialidades:");
							for (Especialidad especialidad : especialidadesReg) {
								System.out.println(especialidad.toString().toLowerCase());
							}
							especialidadesValido = true;
						}
						break;
						default: System.out.println("Por favor, introduzca una opción válida.");
						break;
						}
					} while (!especialidadesValido);
					perfilReg=Perfiles.ARTISTA;
					break;
				case 2: 
					System.out.println("Ha seleccionado coordinador.");
					System.out.print("¿Es un coordinador senior?");
					seniorReg = Utilidades.leerBoolean();
					if (seniorReg) {
						System.out.println("¿Desde qué fecha es senior?");
						fechaSeniorReg = Utilidades.leerFecha();
					}
					perfilReg=Perfiles.COORDINADOR;
				}
			} while (perfilReg == null);


			System.out.println("Introduce el nombre de usuario del nuevo registro\n(solo se aceptan minúsculas, sin tildes ni carácteres especiales):");
			String user = Utilidades.leerStringFormato();
			System.out.print("Introduce la contraseña del nuevo usuario:");
			String contrassenya = Utilidades.leerString();
			System.out.print("Introduce el correo eléctronico del usuario:");
			String correo = Utilidades.leerString();
			Boolean registrovalido = comprobarRegistroExistente(valido, user, correo);

			if (registrovalido) {
				System.out.println("¿Quiere confirmar el registro?");
				boolean confirmarRegistro = esValido();
				if (confirmarRegistro) {
					System.out.println("Registrado nuevo usuario");
					Long nuevoId = obtenerUltimoId();
					escribirEnArchivo(nuevoId, user, contrassenya, correo, nombre, nacionalidad, perfilReg);
					if (perfilReg.equals(Perfiles.ARTISTA)) {
						ret = new Artista(Long.valueOf(nuevoId), correo, nombre, nacionalidad, Long.valueOf(nuevoId), apodoReg, especialidadesReg);
						valido = true;
					} else if (perfilReg.equals(Perfiles.COORDINADOR)) {
						ret = new Coordinador(Long.valueOf(nuevoId),correo,nombre,nacionalidad,Long.valueOf(nuevoId),seniorReg, fechaSeniorReg);
						valido = true;
					} 
				}

			}



		} while (!valido);
		return ret;
	}

	/**
	 * Método para la selección de país que lee las nacionalidades de paises.xml y las muestra para que el usuario
	 * la seleccione según su ID de país.
	 * @return String de nacionalidad para la creación de persona
	 */
	private static String seleccionarPais() {
		String ret="";
		File paisesArchivo = new File(obtenerPropiedad("ficherosnacionalidades"));
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
						paises.put(getNodo("id", elemento), getNodo("nombre", elemento));
						System.out.println(getNodo("id", elemento)+" "+getNodo("nombre", elemento));
					}
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException ex) {
			System.err.println("Error: "+ex.getMessage());
		}
		boolean nacionalidadvalida = false;
		do {
			System.out.print("Código: ");
			String id = Utilidades.leerString();
			for (Entry<String, String> entry : paises.entrySet()) {
				if (id.equalsIgnoreCase(entry.getKey())) {
					ret = entry.getValue();
					System.out.println("Nacionalidad establecida como "+entry.getValue());
					nacionalidadvalida = true;
				}
			}
			if(!nacionalidadvalida) {
				System.err.println("Por favor, introduzca un código válido para nacionalidad. Inténtelo de nuevo.");
			}
		} while (!nacionalidadvalida);
		return ret;
	}

	/**
	 * Método para conseguir el valor de los nodos de un XML
	 * @param etiqueta
	 * @param elem
	 * @return String del valor del Nodo
	 */
	private static String getNodo(String etiqueta, Element elem){
		NodeList nodo=elem.getElementsByTagName(etiqueta).item(0).getChildNodes();
		Node valorNodo = (Node) nodo.item(0);
		return valorNodo.getNodeValue();
	}

	/**
	 * Método para comprobar que el registro de nueva persona no esté ya disponible en el fichero
	 * de credenciales
	 * @param valido
	 * @param user
	 * @param correo
	 * @return Booleano indicando validez de los parámetros
	 */
	private static boolean comprobarRegistroExistente(boolean valido,
			String user, String correo) {
		FileReader lector = null;
		File fichero = new File(obtenerPropiedad("ficherocredenciales"));
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
				} else {
					valido= true;
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
	public static boolean esValido() {
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
	
	/**
	 * Método para obtener el último ID de las personas registradas en el fichero de credenciales
	 * @return Long del ID incrementado
	 */
	private static long obtenerUltimoId() {
		Long ultimoId = 0L;
		File fichero = new File(obtenerPropiedad("ficherocredenciales"));

		if (!fichero.exists()) {
			return 1; // Si el archivo no existe, el primer ID será 1
		}

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero))) {
			while (true) {
				try {
					Espectaculo espectaculo = (Espectaculo) ois.readObject();
					if (espectaculo.getIdEspectaculo() > ultimoId) {
						ultimoId = espectaculo.getIdEspectaculo();
					}
				} catch (EOFException e) {
					break; // Fin del archivo
				}
			}
		} catch (IOException e) {
			return 1;
		} catch (ClassNotFoundException e) {
			System.err.println("Error al leer el archivo de espectáculos: " + e.getMessage());
			e.printStackTrace();
		}

		return ultimoId + 1; // Incrementamos el ID en 1
	}


	/**
	 * Método para escribir en el archivo credenciales los datos de la persona registrada
	 * @param id
	 * @param user
	 * @param contrassenya
	 * @param correo
	 * @param nombre
	 * @param nacionalidad
	 * @param perfilReg
	 */
	private static void escribirEnArchivo(Long id, String user, String contrassenya, String correo, String nombre, String nacionalidad, Perfiles perfilReg) {

		String ruta = obtenerPropiedad("ficherocredenciales");

		try (FileWriter writer = new FileWriter(ruta, true)) {

			String linea = String.format("%n%d|%s|%s|%s|%s|%s|%s",
					id,
					user,
					contrassenya,
					correo,
					nombre,
					nacionalidad,
					perfilReg.toString().toLowerCase(),""
					);

			writer.write(linea);
			System.out.println("Registro guardado correctamente.");
		} catch (IOException e) {
			System.err.println("Error al escribir en el archivo de credenciales: " + e.getMessage());
			e.printStackTrace();
		}
	}

	
	/**
	 * Método para ver los espectáculos registrados
	 */
	public static void verEspectaculos() {
		List<Espectaculo> espectaculos = new ArrayList<>();

		File espectaculosArchivo = new File(obtenerPropiedad("ficheroespectaculos"));



		if (!espectaculosArchivo.exists()) {
			System.out.println("El archivo de espectáculos no existe.");
			return;
		}

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(espectaculosArchivo))) {
			while (true) {
				try {
					Espectaculo espectaculo = (Espectaculo) ois.readObject();
					espectaculos.add(espectaculo);
				} catch (EOFException e) {
					break; // Fin del archivo
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error al leer el archivo de espectáculos: " + e.getMessage());
			e.printStackTrace();
		}

		if (espectaculos.isEmpty()) {
			System.out.println("No hay espectáculos registrados.");
		} else {
			System.out.println("Lista de espectáculos:");
			System.out.println("--------------------------------------------------");
			System.out.printf("%-10s %-20s %-15s %-15s%n", "ID", "Nombre", "Fecha Inicio", "Fecha Fin");
			System.out.println("--------------------------------------------------");

			for (Espectaculo espectaculo : espectaculos) {
				System.out.printf("%-10d %-20s %-15s %-15s%n",
						espectaculo.getIdEspectaculo(),
						espectaculo.getNombre(),
						espectaculo.getFechaini(),
						espectaculo.getFechafin());
			}
		}
	}

	
	/**
	 * Método para obtener los datos del archivo aplication.properties deseados. Se introduce el nombre de la propiedad
	 * Y devuelve un String con la ruta o el dato.
	 * 
	 * @param propiedad
	 * @return String con el dato o ruta
	 */
	private static String obtenerPropiedad(String propiedad) {
		Properties propiedades = new Properties();
		try (FileInputStream entrada = new FileInputStream("src/main/resources/application.properties")){
			propiedades.load(entrada);
		} catch (IOException e) {
			System.err.println("Error de Excepción de tipo IOException al cargar el fichero ");
			e.printStackTrace();
		}

		String propiedadDato = propiedades.getProperty(propiedad);
		return propiedadDato;
	}

	
	/**
	 * Método para definir cual es el último ID de espectáculo y así definir al siguiente
	 * @return Long para ID del espectáculo
	 */
	private static long obtenerUltimoIdEspectaculo() {
		long ultimoId = 0;
		File espectaculosArchivo = new File(obtenerPropiedad("ficheroespectaculos")) ;

		if (!espectaculosArchivo.exists()) {
			return 1; // Si el archivo no existe, el primer ID será 1
		}

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(espectaculosArchivo))) {
			while (true) {
				try {
					Espectaculo espectaculo = (Espectaculo) ois.readObject();
					if (espectaculo.getIdEspectaculo() > ultimoId) {
						ultimoId = espectaculo.getIdEspectaculo();
					}
				} catch (EOFException e) {
					break; // Fin del archivo
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error al leer el archivo de espectáculos: " + e.getMessage());
			e.printStackTrace();
		}

		return ultimoId + 1; // Incrementamos el ID en 1
	}

	/**
	 * Método para validar que el nombre del espectáculo que se está creando no existe ya y cumple con los
	 * requisitos de tamaño máximo.
	 * @param nombre
	 * @return Booleano que verifica si es válido o no.
	 */
	private static boolean validarNombreEspectaculo(String nombre) {
		if (nombre.length() > 25) {
			System.err.println("El nombre del espectáculo no puede superar los 25 caracteres.");
			return false;
		}

		File archivo = new File(obtenerPropiedad("ficheroespectaculos"));

		if (!archivo.exists()) {
			return true; // Si el archivo no existe, el nombre es válido
		}

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
			while (true) {
				try {
					Espectaculo espectaculo = (Espectaculo) ois.readObject();
					if (espectaculo.getNombre().equalsIgnoreCase(nombre)) {
						System.err.println("El nombre del espectáculo ya existe.");
						return false;
					}
				} catch (EOFException e) {
					break; 
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error al leer el archivo de espectáculos: " + e.getMessage());
			e.printStackTrace();
		}

		return true;
	}
	
	/**
	 * Método para validar que las fechas del espectáculo cumplen con el tiempo máximo entre inicio
	 * y final de 1 año
	 * @param fechaIni
	 * @param fechaFin
	 * @return Booleano con la validación
	 */
	private static boolean validarFechasEspectaculo(LocalDate fechaIni, LocalDate fechaFin) {
		if (fechaIni.isAfter(fechaFin)) {
			System.err.println("La fecha de inicio no puede ser posterior a la fecha de fin.");
			return false;
		}

		if (fechaFin.isAfter(fechaIni.plusYears(1))) {
			System.err.println("El periodo de fechas no puede superar 1 año.");
			return false;
		}

		return true;
	}

	/**
	 * Método para seleccionar el coordinador de entre los existentes.
	 * @return Long del id del coordinador
	 */
	private static Long seleccionarCoordinador() {
		List<Coordinador> coordinadores = new ArrayList<>();
		System.out.println("Selección de coordinador");

		FileReader lector = null;
		File fichero = new File(obtenerPropiedad("ficherocredenciales"));
		BufferedReader br = null;

		try {
			lector = new FileReader(fichero);
			br = new BufferedReader(lector);
			String linea;
			int i = 0;
			while ((linea = br.readLine()) != null) {

				String[] campos = linea.split("\\|");
				if (campos.length < 7) {
					continue;//Saltamos las líneas si contienen menos campos de los que deberían
				}				
				Long id = Long.valueOf(campos[0]);
				String nombreUsuario = campos[1]; // Índice 1: nombre_usuario
				String email = campos[2];
				String nombre = campos[3];
				String nacionalidad = campos[4];
				Long idCord = id;
				Boolean senior = false;
				LocalDate fechasenior = null;
				String perfilLogin = campos[6].toUpperCase();
				Coordinador coordinador = new Coordinador(id,nombreUsuario,email,nombre,idCord,senior,fechasenior);

				if(perfilLogin.equals("COORDINADOR")) {
					i++;

					coordinadores.add(coordinador);
					System.out.println(i+".- "+nombre);
				}

			}
		} catch (Exception e) {
			System.err.println("Error al leer el archivo de credenciales: " + e.getMessage());
			e.printStackTrace();
		}


		int opcion = Utilidades.leerEntero();
		if (opcion < 1 || opcion > coordinadores.size()) {
			System.err.println("Opción no válida.");
			return null;
		}

		return coordinadores.get(opcion - 1).getId();
	}

	/**
	 * Método para la entrada de un nuevo espectáculo, pide los datos, los valida y crea el Espectáculo,
	 * llamando a métodos para escribirlo en su archivo
	 * @param actual
	 */
	public static void gestionarEspectaculo(Sesion actual) {
		System.out.println("------------------------------\n=== Gestionar Espectáculo ===\n------------------------------");

		System.out.print("Introduce el nombre del espectáculo (máximo 25 caracteres): ");
		String nombre = Utilidades.leerString();

		if (!validarNombreEspectaculo(nombre)) {
			return;
		}

		System.out.println("\nIntroduce la fecha de inicio: ");
		LocalDate fechaIni = Utilidades.leerFecha();

		System.out.println("\nIntroduce la fecha de fin: ");
		LocalDate fechaFin = Utilidades.leerFecha();

		if (!validarFechasEspectaculo(fechaIni, fechaFin)) {
			return;
		}

		Long idCoord;
		if (actual.getPerfil() == Perfiles.ADMIN) {
			idCoord = seleccionarCoordinador();
			if (idCoord == null) {
				return;
			}
		} else {

			idCoord = idCoordinadorActual(actual); // Asignar el ID del coordinador actual
		}

		long nuevoId = obtenerUltimoIdEspectaculo();
		Espectaculo nuevoEspectaculo = new Espectaculo(nuevoId, nombre, fechaIni, fechaFin, idCoord);

		// Guardar el espectáculo en el archivo
		guardarEspectaculo(nuevoEspectaculo);

		System.out.println("Espectáculo creado con éxito. ID: " + nuevoId);
	}


	/**
	 * Método para obtener el ID del coordinador que está en la Sesión actual
	 * @param actual
	 * @return Long con ID del coordinador en sesión
	 */
	private static Long idCoordinadorActual(Sesion actual) {
		Long ret = 0L;

		FileReader lector = null;
		File fichero = new File(obtenerPropiedad("ficherocredenciales"));
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
				Long id = Long.valueOf(campos[0]);
				String nombreUsuario = campos[1]; // Índice 1: nombre_usuario

				if(actual.getNombre().equals(nombreUsuario)) {
					ret = id;
					break;
				}

			}
		} catch (Exception e) {
			System.err.println("Error al leer el archivo de credenciales: " + e.getMessage());
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * Método para guardar el Espectáculo creado en su archivo correspondiente
	 * @param espectaculo
	 */
	private static void guardarEspectaculo(Espectaculo espectaculo) {
		File archivo = new File(obtenerPropiedad("ficheroespectaculos"));

		// Crear el directorio si no existe
		archivo.getParentFile().mkdirs();

		List<Espectaculo> espectaculos = new ArrayList<>();

		// Leer los espectáculos existentes
		if (archivo.exists()) {
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
				while (true) {
					try {
						espectaculos.add((Espectaculo) ois.readObject());
					} catch (EOFException e) {
						break; // Fin del archivo
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				System.err.println("Error al leer el archivo de espectáculos: " + e.getMessage());
				e.printStackTrace();
			}
		}

		// Añadir el nuevo espectáculo
		espectaculos.add(espectaculo);

		// Guardar todos los espectáculos en el archivo
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
			for (Espectaculo esp : espectaculos) {
				oos.writeObject(esp);
			}
		} catch (IOException e) {
			System.err.println("Error al guardar el espectáculo: " + e.getMessage());
			e.printStackTrace();
		}
	}


}
