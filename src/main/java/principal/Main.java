package principal;

import java.awt.Menu;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.PrivateKey;
import java.util.Properties;
import java.util.Scanner;

import javax.security.auth.login.LoginContext;

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
			System.out.println("\n\n<<<\tPrograma de gestión del circo\t>>>");
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
//				actual = submenuSesion(actual, opcion);
			}
		} while (!confirmarsalir);

	}
	
//	private static Sesion submenuSesion(Sesion actual, int opcion) {
//		switch (actual.getPerfil()) {
//		case INVITADO:
//			switch (opcion) {
//			case 1:
//				System.out.println("Ver espectáculos del circo:");
//				break;
//			case 2:
//				System.out.println("\n<<<\tEntrando en el menú de acceso al usuario\t>>>");
//				actual = Login();
//				break;
//			default:
//				break;
//			}
//			break;
//
//		default:
//			break;
//		}
//		
//		return ret;
//	}

	private static Sesion Login() {
		Sesion ret = new Sesion();
		System.out.println("\t<<<\tBienvenido al menú de acceso\t>>>");
		boolean valido=false;
		do {
			System.out.println("\t\t\tPor favor, introduce tu usuario:");
			String user = Utilidades.leerString();
			System.out.println("\t\t\tPor favor, introduce tu contraseña:");
			String contrassenya = Utilidades.leerString();
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
				System.out.println("\n\t<<<\t¡Bienvenido! Se ha autenticado usted como administrador.\t>>>\n");
				
				valido = true;
			}
			
			// Comprobamos el archivo de credenciales
			String ruta = propiedades.getProperty("ficherocredenciales");
			FileReader lector = null;
			File fichero = new File(ruta);
			BufferedReader br = null;
			
			try {
				lector = new FileReader(fichero);
				br = new BufferedReader(br);
				String linea;
				
				while ((linea = br.readLine()) != null) {
					String[] campos = linea.split("\\|");
					if (campos.length < 7) {
						continue;
					}
					// El fichero credenciales.txt sigue esta plantilla para cada línea:
				    //  idpersonal|nombre_usuario|password|email|nombre_persona
					
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
		} while (!valido);

		return ret;
		
	}
	
	private static int seleccionarOpcion(Sesion actual) {
		// TODO Auto-generated method stub
		int seleccion=-1;
		do {
			switch (actual.getPerfil()) {
			case INVITADO:{
				System.out.println("\n\tIntroduzca el número de la opción deseada:");
				System.out.println("\t\t\t1.- Ver espectáculos");
				System.out.println("\t\t\t2.- Iniciar sesión.");
				System.out.println("\t\t\t0.- Salir");
				seleccion = Utilidades.leerEntero();
				if (seleccion < 0 || seleccion > 2) {
					System.out.println("Opción no disponible. Por favor seleccione una opción.");
					seleccion = -1;
				}
			}
				break;
			case ARTISTA: {
				System.out.println("\n<<<  Menú de Artista en construcción  >>>\nIntroduzca el número de la opción deseada:");
				System.out.println("\t\t\t1.- Gestionar personas y credenciales.");
				System.out.println("\t\t\t2.- Gestionar espectáculos.");
				System.out.println("\t\t\t0.- Salir");
			}
				break;
			case COORDINADOR: {
				System.out.println("\n<<< Menú de Coordinador en construcción >>>\nIntroduzca el número de la opción deseada:");
			}
				break;
			case ADMIN: {
				System.out.println("\n<<< Menú de Admin en construcción >>>\nIntroduzca el número de la opción deseada:");
				
			}
				break;
			default:
				break;
			}
		} while (seleccion == -1);


			
		return seleccion;
	}



	
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
