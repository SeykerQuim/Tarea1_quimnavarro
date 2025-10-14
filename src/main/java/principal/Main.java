package principal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.PrivateKey;
import java.util.Properties;
import java.util.Scanner;

import com.quimnv.modelo.Perfiles;
import com.quimnv.modelo.Sesion;

import utils.Utilidades;

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
			System.out.println("\n\nBIENVENID@ al...");
			System.out.println("Hola "+actual.getNombre()+". Su perfil es...");
			opcion = seleccionarOpcion(actual);
			switch (opcion) {
			case 0:
				if (actual.getPerfil().equals(Perfiles.INVITADO)){
					System.out.println("Ha elegido SALIR. ¿Es correcto?");
					confirmarsalir = Utilidades.leerBoolean();
					break;
				} else {
					System.out.println("Ha elegido CERRAR SESIÓN. ¿Es correcto?");
					confirmarsalir = Utilidades.leerBoolean();

				}
			}
		} while (confirmarsalir);

	}

	private static int seleccionarOpcion(Sesion actual) {
		// TODO Auto-generated method stub
		int seleccion=-1;
		try {
			System.out.println("\n\tIntroduzca el número de la opción deseada:");
			System.out.println("\t\t\t1.- Ver espectáculos");
			System.out.println("\t\t\t2.- Iniciar sesión.");
			System.out.println("\t\t\t0.- Salir");
			seleccion = leer.nextInt();
		} catch (Exception e) {
			System.err.println("Ha introducido un carácter no válido, inténtelo de nuevo");
		}
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
