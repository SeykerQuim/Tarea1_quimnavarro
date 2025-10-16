/**
 * Clase Utilidades.java
 * 
 * @author QUIM NAVARRO VAQUERO
 * @version 1.0
 */

package utils;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Utilidades {
	
	
	public static boolean leerBoolean() {
		boolean ret;
		Scanner in;
		char respuesta;
		do {
			System.out.println("Pulse S para Sí o N para No.");
			in = new Scanner(System.in, "ISO-8859-1");
			in.reset();
			respuesta = in.nextLine().charAt(0);
			if (respuesta != 's' && respuesta != 'S' && respuesta != 'n' & respuesta != 'N') {
				System.out.println("Valor introducido incorrecto.");
			}
		} while (respuesta != 's' && respuesta != 'S' && respuesta != 'n' & respuesta != 'N');
		if (respuesta == 's' || respuesta == 'S') {
			ret = true;
		} else {
			ret = false;
		}
		return ret;
	}
	
	public static int leerEntero() {
		int ret = -1;
		do {
			Scanner in = new Scanner(System.in);
			try {
				ret = in.nextInt();
			} catch (Exception e) {
				System.out.println("No ha introducido un entero.");
			}
		} while (ret == -1);
		return ret;
	}
	
	public static double leerDouble() {
		double ret = 0.0;
		boolean correcto = false;
		Scanner in;
		do {
			System.out.println("Introduzca un valor decimal (xx,xx)");
			in = new Scanner(System.in,"ISO-8859-1");
			try {
				ret = in.nextDouble();
				correcto = true;
			} catch (InputMismatchException ime) {
				System.out.println("Formato introducido incorrecto.");
				correcto = false;
			}
		} while (!correcto);
		return ret;
	}
	
	public static java.time.LocalDate leerFecha() {
		LocalDate ret = null;
		int dia=-1, mes=-1, any=-1;
		boolean correcto = false;
		Scanner in = new Scanner(System.in, "ISO-8859-1");
		do {
			do {
				correcto=false;
				System.out.println("Introduzca un valor para el día (número del día):");
				try {
					dia = in.nextInt();
					correcto = true;
				} catch (InputMismatchException e) {
					System.out.println("No ha introducido un valor entero.");
				}
			} while (!correcto);
			do {
				correcto = false;
				System.out.println("Introduzca un valor para el mes (número de mes):");
				try {
					mes = in.nextInt();
					correcto = true;
				} catch (InputMismatchException e) {
					System.out.println("No ha introducido un valor entero válido.");
				}
			} while (!correcto);
		} while (!correcto);
		return ret;
	}
	
	public static String leerString() {
		String ret = null;
		Scanner in = new Scanner(System.in, "ISO-8859-1");
		
		do {
			try {
				ret = in.nextLine();
			} catch (Exception e) {
				// TODO: handle exception
			}
		} while (ret == null && ret.isEmpty());
	
				
		return ret;
	}

}
